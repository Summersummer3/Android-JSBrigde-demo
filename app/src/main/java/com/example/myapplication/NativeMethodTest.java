package com.example.myapplication;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class NativeMethodTest implements IBridge{
    /**
     * A native method that is implemented by the 'myapplication' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public static JSONObject handleData(JSONObject object) {
        JSONObject object1 = new JSONObject();
        if (object.has("name")) {
            try {
                object1.put("src_name", object.get("name"));
                object1.put("name", "kyb");
                object1.put("jni_res", stringFromJNI());
                if (object.has("HandleView")) {
                    TextView tv = (TextView) object.get("HandleView");
                    tv.setText("req name is " + object.getString("name"));
                }
                return object1;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
