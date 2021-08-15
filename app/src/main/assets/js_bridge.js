var JSBridge = window.JSBridge || (window.JSBridge = {});

JSBridge.callHandler = function(api, callbackId, handlerName, data) {
    var uri = "jsbridge://" + api + ":" + callbackId + "/" + handlerName + "?" + data;
    var msgIFrames = document.createElement("iframe");
    msgIFrames.style.display = 'none';
    document.documentElement.appendChild(msgIFrames);

    msgIFrames.src = uri;
};

/* android post data is parsed as a object */
JSBridge.callbacks = {};

JSBridge.callbackRegister = function(callbackId, callback) {
    JSBridge.callbacks[callbackId] = callback;
}

function JSBridgeTest() {
    JSBridge.callbackRegister(10, (data) => {
        document.getElementById("demo").innerHTML = data.name + "<br>" + "JNI_Res:" + data.jni_res;
    });

    /* callbackId hasn't been used yet */
    JSBridge.callHandler("NativeTest", 10, "handleData", "name=summer");
}