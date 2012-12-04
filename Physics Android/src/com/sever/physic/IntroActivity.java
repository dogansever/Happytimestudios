package com.sever.physic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
	public static float deviceWidth;
	public static float deviceHeight;

	public void recallDeviceMetrics() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		recallDeviceMetrics();

		bmpIntro = GeneralUtil.createScaledBitmap(this, R.drawable.space, (int) deviceWidth, (int) deviceHeight);
		bmpIntro2 = GeneralUtil.createScaledBitmap(this, R.drawable.introsub1, (int) deviceWidth, (int) (deviceHeight * 0.125f));

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

}
