package com.ndkdev.hellondk;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView txtvMessage;
	
	static {
		System.loadLibrary("hellondk");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.txtvMessage = (TextView) findViewById(R.id.txtv_message);
	}

	@Override
	protected void onStart() {
		super.onStart();

		final String message = getMessage();

		this.txtvMessage.setText(message);
	}

	public native String getMessage();
}
