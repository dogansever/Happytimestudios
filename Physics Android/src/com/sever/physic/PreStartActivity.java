package com.sever.physic;

import java.util.Random;

import com.sever.physics.game.utils.SoundEffectsManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PreStartActivity extends Activity {

	public final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prestart);
		SoundEffectsManager.startIngameAmbianceSound(PreStartActivity.this);
		final Button yes = (Button) findViewById(R.id.Button03);
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(5);
				if (randomInt == 0) {
					com.sever.physics.game.utils.AdUtil.getAdUtil().simClick();
				} else {
					yes.setVisibility(View.INVISIBLE);
					SoundEffectsManager.getManager().playBUTTON_CLICK(PreStartActivity.this);
					Intent intent = new Intent(PreStartActivity.this, PhysicsActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		clearBackground();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		drawBackground();
		com.sever.physics.game.utils.AdUtil.getAdUtil().createAd(this);
	}

	private void drawBackground() {
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		im.setBackgroundResource(R.drawable.prestartsplash);
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
		clearBackground();
		com.sever.physics.game.utils.AdUtil.getAdUtil().destroyAd();
	}

	private void clearBackground() {
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		im.setBackgroundDrawable(null);
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
		// clearBackground();
	}

}
