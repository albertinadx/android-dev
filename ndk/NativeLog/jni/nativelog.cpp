#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <android/log.h>

#include "include/com_intel_androiddev_nativelog_loglib_NativeLogLib.h"

/* Java class and callback */
#define CB_CLASS "com/intel/androiddev/nativelog/loglib/NativeLogLib"
#define CB_CLASS_MSG_CALLBACK "OnLogMessage"
#define CB_CLASS_MSG_SIGNATURE "(Ljava/lang/String;I)V"
#define TAG "nativelog"

int lib_main(int argc, char **argv);
const int getArrayLen(JNIEnv * env, jobjectArray jarray);
void cb_printf(char *format, ...);

static JavaVM *g_VM;
static jclass jNativeLogLibClass;

/**
 * Send a string back to Java
 */
jmethodID mSendStr;

/*
 * Class:     com_intel_androiddev_nativelog_loglib_NativeLogLib
 * Method:    LibMain
 * Signature: ([Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_intel_androiddev_nativelog_loglib_NativeLogLib_LibMain(
		JNIEnv * env, jclass clasz, jobjectArray jargv) {

	__android_log_print(ANDROID_LOG_INFO, TAG,
			"Java_com_intel_androiddev_nativelog_loglib_NativeLogLib_LibMain()");

	// get a global reference to the caller java class
	env->GetJavaVM(&g_VM);

	// extract char ** args from java array
	jsize arrayLen = getArrayLen(env, jargv);

	char * args[(int) arrayLen];
	int i;
	jstring jrow;

	for (i = 0; i < arrayLen; i++) {
		// get the C string from Java String array[i]
		jrow = (jstring) env->GetObjectArrayElement(jargv, i);
		const char *row = env->GetStringUTFChars(jrow, 0);

		args[i] = (char *) malloc(strlen(row) + 1);
		strcpy(args[i], row);

		// print args using the java method
		cb_printf("Main args[%d]=%s", i, args[i]);

		// free java string jrow
		env->ReleaseStringUTFChars(jrow, row);
	}

	// Loads the NativeLogLib java class
	jNativeLogLibClass = env->FindClass(CB_CLASS);

	if (jNativeLogLibClass == 0) {
		cb_printf("Unable to find java class %s", CB_CLASS);
		return -1;
	}

	lib_main(arrayLen, args);

	return 0;
}

/*
 * Class:     com_intel_androiddev_nativelog_loglib_NativeLogLib
 * Method:    getNativeString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_intel_androiddev_nativelog_loglib_NativeLogLib_getNativeString(
		JNIEnv * pEnv, jclass clasz) {
	__android_log_print(ANDROID_LOG_INFO, TAG,
			"Java_com_intel_androiddev_nativelog_loglib_NativeLogLib_getNativeString()");

	return pEnv->NewStringUTF("Android JNI");
}

static void jni_send_str(const char * text, int level) {
	__android_log_print(ANDROID_LOG_DEBUG, TAG, "jni_send_str()");

	JNIEnv *env;

	if (!g_VM) {
		printf("I_JNI-NOVM: %s\n", text);
		return;
	}

	g_VM->AttachCurrentThread(&env, NULL);

	// Loads the NativeLib java class if missing
	if (!jNativeLogLibClass) {
		jNativeLogLibClass = env->FindClass(CB_CLASS);

		if (jNativeLogLibClass == 0) {
			printf("Unable to find java class: %s", CB_CLASS);
			return;
		}
	}

	// Call java method OnLogMessage(String, int)
	if (!mSendStr) {
		// Get a reference to the static java method
		mSendStr = env->GetStaticMethodID(jNativeLogLibClass,
		CB_CLASS_MSG_CALLBACK, CB_CLASS_MSG_SIGNATURE);
	}

	if (mSendStr) {
		jstring jtext = env->NewStringUTF(text);
		// Call the java method
		env->CallStaticVoidMethod(jNativeLogLibClass, mSendStr, jtext,
				(jint) level);
	} else {
		printf("Unable to find method: %s, signature: %s\n",
		CB_CLASS_MSG_CALLBACK, CB_CLASS_MSG_SIGNATURE);
	}
}

/**
 * Printf into the java layer
 * does a varargs printf into a temporary buffer
 * and calls jni_send_str
 */
void cb_printf(char *format, ...) {
	__builtin_va_list argptr;
	static char string[1024];

	va_start(argptr, format);
	vsprintf(string, format, argptr);
	va_end(argptr);

	jni_send_str(string, 0);
}

/**
 * Get java array length
 */
const int getArrayLen(JNIEnv * env, jobjectArray jarray) {
	return env->GetArrayLength(jarray);
}

/**
 * Library main sub
 */
int lib_main(int argc, char **argv) {
	__android_log_print(ANDROID_LOG_DEBUG, TAG, "lib_main()");

	cb_printf("Entering lib_main()");

	for (int i = 0; i < argc; i++) {
		cb_printf("Lib Main argv[%d]=%s", i, argv[i]);
	}

	return 0;
}
