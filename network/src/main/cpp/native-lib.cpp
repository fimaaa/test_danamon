//
// Created by Fiqri Malik Abdul Az on 5/28/2021.
//

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_general_network_di_ExternalDataKt_getBaseUrlAll(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("https://jsonplaceholder.typicode.com/");
}