#include <jni.h>
#include <string.h>
#include <android/log.h>
#include "include/nativeInfo.h"
#include "include/nativeMath.h"

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "JniDemo", __VA_ARGS__))

jint addition(JNIEnv *pEnv, jobject jObj, jint x, jint y) {
	return 0;
}

jint substraction(JNIEnv *pEnv, jobject jObj, jint x, jint y) {
	return 0;
}

jint multiplication(JNIEnv *pEnv, jobject jObj, jint x, jint y) {
	return 0;
}

jint division(JNIEnv *pEnv, jobject jObj, jint x, jint y) {
	return 0;
}

jstring getCpuInfo(JNIEnv *pEnv, jobject jObj) {
	return (*pEnv)->NewStringUTF(pEnv, "Not implemented yet");
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	JNIEnv *env;
	
	if((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6)) {
		return -1;
	}
	
	JNINativeMethod methodsMath[4];
	JNINativeMethod methodsInfo[1];

	methodsMath[0].name = "addition";
	methodsMath[0].signature = "(II)I";
	methodsMath[0].fnPtr = addition;
	methodsMath[1].name = "substraction";
	methodsMath[1].signature = "(II)I";
	methodsMath[1].fnPtr = substraction;
	methodsMath[2].name = "multiplication";
	methodsMath[2].signature = "(II)I";
	methodsMath[2].fnPtr = multiplication;
	methodsMath[3].name = "division";
	methodsMath[3].signature = "(II)I";
	methodsMath[3].fnPtr = division;
	
	methodsInfo[1].name = "getCpuInfo";
	methodsInfo[1].signature = "(II)I";
	methodsInfo[1].fnPtr = getCpuInfo;

	jclass clsMath = (*env)->FindClass(env, "com/ndkdev/jnidemo/lib/NativeMath");
	(*env)->RegisterNatives(env, clsMath, methodsMath, 4);

	jclass clsInfo = (*env)->FindClass(env, "com/ndkdev/jnidemo/lib/NativeInfo");
	(*env)->RegisterNatives(env, clsInfo, methodsInfo, 1);	
	
	return JNI_VERSION_1_6;
}







