#include <jni.h>
#include <string.h>

jstring Java_com_ndkdev_hellondk_MainActivity_getMessage(JNIEnv * pEnv, jobject jObj) {
	return (*pEnv)->NewStringUTF(pEnv,"Hello NDK from JNI!");
}

