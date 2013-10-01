package com.intel.androiddev.hellondk;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity {

	private Button btnHello;
	private TextView txtvMessage;
	private NativeLib library;

	static {
		System.loadLibrary("hellondk");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		library = new NativeLib();

		initViews();
	}

	private void initViews() {
		btnHello = (Button) findViewById(R.id.btnHello);
		txtvMessage = (TextView) findViewById(R.id.txtv_message);

		btnHello.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				txtvMessage.setText(library.getMessage());
			}
		});
	}
}
