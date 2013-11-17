LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := nativelog
INCLUDES := 
LOCAL_CFLAGS := -O2 $(INCLUDES) 
LOCAL_LDLIBS := -llog
LOCAL_SRC_FILES := nativelog.cpp

include $(BUILD_SHARED_LIBRARY)
