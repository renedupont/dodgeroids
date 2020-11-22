package de.games.keepitup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ExceptionActivity extends Activity {

	private TextView error;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_keep_it_up);

		error = (TextView) findViewById(R.id.error);

		error.setText(getIntent().getStringExtra("error"));
	}

}