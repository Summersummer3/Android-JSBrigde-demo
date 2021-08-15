#include <jni.h>
#include <string>
#include <android/log.h>

#define JNI_REG_CLASS "com/example/myapplication/NativeMethodTest"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO  , "ProjectName", __VA_ARGS__)

jstring stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "First dynamic test of JNI";
    return env->NewStringUTF(hello.c_str());
}

static const JNINativeMethod jniNativeMethods[] = {
        {"stringFromJNI", "()Ljava/lang/String;", (void *)(stringFromJNI)},
};

static int registerNativeMethods(
        JNIEnv *env,
        const char *className) {
    jclass clazz;
    jint res = -1;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_ERR;
    }
    if ((res = env->RegisterNatives(clazz, jniNativeMethods, 1)) != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_OK;
}

extern "C"
JNIEXPORT jint JNICALL JNI_OnLoad(
        JavaVM *vm,
        void *rsv) {
    JNIEnv *env = NULL;
    jint res = -1;

    if (vm->GetEnv((void **)&env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    if (registerNativeMethods(env, JNI_REG_CLASS) != JNI_OK) {
        return -1;
    }

    res = JNI_VERSION_1_6;
    return res;
}