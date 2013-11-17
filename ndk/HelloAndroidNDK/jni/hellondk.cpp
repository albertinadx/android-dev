#include <jni.h>
#include "include/com_androiddev_hellondk_NativeLib.h"

JNIEXPORT jstring JNICALL Java_com_androiddev_hellondk_NativeLib_getMessage(
		JNIEnv * pEnv, jobject pThis) {
	return pEnv->NewStringUTF("Hello Android NDK");
}

