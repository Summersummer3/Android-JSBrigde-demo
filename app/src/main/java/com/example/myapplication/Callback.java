package com.example.myapplication;

import android.webkit.WebView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class Callback {
    private WeakReference<WebView> mWebViewRef;
    private final String jsCallbackFmt = "javascript: JSBridge.callbacks[%d](%s);";

    Callback(WebView wv) {
        mWebViewRef = new WeakReference<>(wv);
    }

    public void apply(int port, JSONObject cbData) {
        String exec = String.format(jsCallbackFmt, port, cbData.toString());
        if (mWebViewRef != null && mWebViewRef.get() != null && cbData != null) {
            mWebViewRef.get().loadUrl(exec);
        }
    }
}
