package com.intel.androiddev.nativelog;

import com.intel.androiddev.nativelog.loglib.NativeLogLib;
import com.intel.androiddev.nativelog.loglib.OnNativeLogChangeListener;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity implements OnNativeLogChangeListener {

	private TextView txtvLog;

	public static final String TAG = MainActivity.class.getName();

	static {
		System.loadLibrary("nativelog");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtvLog = (TextView) findViewById(R.id.txtv_log);

		NativeLogLib.listener = this;
	}

	@Override
	protected void onResume() {
		super.onResume();

		addText(NativeLogLib.getNativeString() + "\n");

		try {
			final String args[] = { "Hello!", "from", "native", "side",
					"Android!", "Android NDK" };
			NativeLogLib.LibMain(args);
		} catch (final Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public void onNativeLogMessage(String value) {
		addText(value);
	}

	private void addText(final String value) {
		txtvLog.append(value);
	}
}
