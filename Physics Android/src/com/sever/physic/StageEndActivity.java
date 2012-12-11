package com.sever.physic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import com.sever.physics.game.StageEndView;
import com.sever.physics.game.utils.GeneralUtil;

public class StageEndActivity extends Activity {

	private StageEndView stageEndView;
	public static Bitmap bmpIntro;
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

		setContentView(R.layout.stageend);
		RelativeLayout root = (RelativeLayout) findViewById(R.id.stageendViewRelativeLayout);
		stageEndView = new StageEndView(this);
		root.addView(stageEndView);
	}

}
