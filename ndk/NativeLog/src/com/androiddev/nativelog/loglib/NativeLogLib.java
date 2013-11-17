package com.androiddev.nativelog.loglib;

public class NativeLogLib {

	public static OnNativeLogChangeListener listener;

	/**
	 * Native Android main loop.
	 * 
	 * @param args
	 * @return
	 */
	public static native int LibMain(String[] args);

	public static native String getNativeString();

	/**
	 * Java method that is executed from the Native side.
	 * 
	 * @param text
	 * @param level
	 */
	private static void OnLogMessage(String text, int level) {
		if (listener != null) {
			listener.onNativeLogMessage("text: " + text + " - level: " + level
					+ "\n");
		}
	}

}
