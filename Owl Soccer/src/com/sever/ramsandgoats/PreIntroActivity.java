package com.sever.ramsandgoats;

import android.app.Activity;
import android.os.Bundle;

public class PreIntroActivity extends Activity {

	@Override
	public void onBackPressed() {
		System.out.println("onBackPressed:" + this);
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		System.out.println("onPostCreate:" + this);
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onPostResume() {
		System.out.println("onPostResume:" + this);
		super.onPostResume();
	}

	@Override
	protected void onRestart() {
		System.out.println("onRestart:" + this);
		super.onRestart();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
	}

	@Override
	protected void onStart() {
		System.out.println("onStart:" + this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
	}
}
