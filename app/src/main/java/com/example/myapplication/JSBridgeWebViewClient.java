package com.example.myapplication;

import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class JSBridgeWebViewClient extends WebViewClient {
    private TextView tv;
    private JSBridge jsBridge;

    JSBridgeWebViewClient(TextView tv, JSBridge jsBridge) {
        this.tv = tv;
        this.jsBridge = jsBridge;
    }

    private JSONObject parseQuery(String query) {
        JSONObject object = new JSONObject();
        String[] params = query.split("&");
        for (String param : params) {
            String[] kv = param.split("=");
            try {
                object.put(kv[0], kv[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri uri = request.getUrl();
        int callbackId = uri.getPort();
        JSONObject params = parseQuery(uri.getQuery());

        try {
            params.put("HandleView", this.tv);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (uri.getScheme().equals("jsbridge")) {
            if (callbackId != 0) {
                jsBridge.callJSNativeMethodWithCallback(uri.getHost(),
                        uri.getPath().substring(1), params, view, callbackId);

            } else {
                jsBridge.callJSNativeMethod(uri.getHost(),
                        uri.getPath(), params);
            }

            return false;
        }
        return true;
//        return super.shouldOverrideUrlLoading(view, request);
    }
}
