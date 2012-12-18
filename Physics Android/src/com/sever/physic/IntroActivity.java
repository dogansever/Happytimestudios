package com.sever.physic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sever.physics.game.IntroView;
import com.sever.physics.game.utils.GeneralUtil;

public class IntroActivity extends Activity {

	private IntroView introView;
	public static Bitmap bmpIntro;
	public static Bitmap bmpIntro2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);

		bmpIntro = GeneralUtil.createScaledBitmap(this, R.drawable.space, (int) PhysicsApplication.deviceWidth, (int) PhysicsApplication.deviceHeight);
		bmpIntro2 = GeneralUtil.createScaledBitmap(this, R.drawable.introsub1, (int) PhysicsApplication.deviceWidth, (int) (PhysicsApplication.deviceHeight * 0.125f));

		setContentView(R.layout.intro);
		RelativeLayout root = (RelativeLayout) findViewById(R.id.introViewRelativeLayout);
		introView = new IntroView(this);
		root.addView(introView);

		final Button start = (Button) findViewById(R.id.Button01);
		start.setVisibility(View.VISIBLE);
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				introView.finishIntro();
				start.setVisibility(View.GONE);
			}
		});

		show(0);
	}

	private void show(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 0:
			showTopScore();
			break;
		case 1:
			showTopScoresLocal();
			break;
		case 2:
			showTopScoresOnline();
			break;

		default:
			break;
		}
	}

	private void showTopScoresOnline() {
		// TODO Auto-generated method stub

	}

	private void showTopScoresLocal() {
		// TODO Auto-generated method stub

	}

	private void showTopScore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
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
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
	}

}
