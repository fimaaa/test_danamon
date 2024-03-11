//
// Created by Fiqri Malik Abdul Az on 5/28/2021.
//

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_general_network_di_ExternalDataKt_getJsonPlaceHolderBaseUrl(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("https://jsonplaceholder.typicode.com/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_general_network_di_ExternalDataKt_getBaseAppBaseLocalUrl(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("http://10.147.18.200:3000/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_general_network_di_ExternalDataKt_getPathAuth(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("/api/v1/auth");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_general_network_di_ExternalDataKt_getPathMember(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("/api/v1/member");
}