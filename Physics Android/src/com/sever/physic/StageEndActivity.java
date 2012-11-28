package com.sever.physic;

import android.app.Activity;
import android.os.Bundle;

public class StageEndActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.stageend);
	}

}
