package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivityMainBinding;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'myapplication' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ActivityMainBinding binding;

    private void webViewInit(WebView webView, TextView tv) {
        JSBridge jsBridge = new JSBridge();
        Class clazz = NativeMethodTest.class;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.requestFocus();

        jsBridge.registerJSNativeMethods("NativeTest", clazz);

        JSBridgeWebViewClient client = new JSBridgeWebViewClient(tv, jsBridge);

        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(client);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        Button btn = binding.button;
        WebView webView = binding.webView;

        tv.setText("hello world");
        webViewInit(webView, tv);
    }

}