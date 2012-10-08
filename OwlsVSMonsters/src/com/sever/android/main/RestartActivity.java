package com.sever.android.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RestartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(RestartActivity.this, GameGameActivity.class);
		startActivity(intent);
		finish();
	}

}
