package com.example.myapplication;

import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class JSBridge {
    private static Map<String, HashMap<String, Method>> apiMaps = new HashMap();

    private HashMap<String, Method> getAllApiMethods(Class<IBridge> clazz) {
        HashMap<String, Method> methodMaps = new HashMap();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            String name;
            int modifier = method.getModifiers();
            if (modifier != (Modifier.PUBLIC | Modifier.STATIC) ||
                    (name = method.getName()).equals("")) {
                continue;
            }
            if (method.getReturnType() != JSONObject.class ||
                    method.getParameterTypes()[0] != JSONObject.class) {
                continue;
            }
            methodMaps.put(name, method);
        }

        return methodMaps;
    }

    public boolean registerJSNativeMethods(String api, Class<IBridge> clazz) {
        if (!apiMaps.containsKey(api)) {
            apiMaps.put(api, getAllApiMethods(clazz));
            return true;
        } else {
            return false;
        }
    }

    /* method params should be Json String */
    public JSONObject callJSNativeMethod(String api, String handlerName, JSONObject params) {

        if (apiMaps.containsKey(api)) {
            try {
                Method method = apiMaps.get(api).get(handlerName);
                return (JSONObject) method.invoke(null, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void callJSNativeMethodWithCallback(String api, String handlerName,
                                               JSONObject params, WebView wv, int callbackId) {
        JSONObject object = callJSNativeMethod(api, handlerName, params);
        Callback cb = new Callback(wv);
        if (object != null)
            cb.apply(callbackId, object);
    }
}
