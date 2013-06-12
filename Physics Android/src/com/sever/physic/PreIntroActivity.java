package com.sever.physic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.geosophic.service.Geosophic_Activity;
import com.sever.physics.game.utils.LogUtil;
import com.sever.physics.game.utils.SoundEffectsManager;

public class PreIntroActivity extends Geosophic_Activity {

	public final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.log("onCreate:" + this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preintro);
		SoundEffectsManager.startIntroAmbianceSound(PreIntroActivity.this);
		final Runnable r2 = new Runnable() {
			public void run() {
				Intent intent = new Intent(PreIntroActivity.this, IntroActivity.class);
				startActivity(intent);
				finish();
			}
		};
		Thread t = new Thread() {
			public void run() {
				mHandler.postDelayed(r2, 1000 * 6);
			}
		};
		t.start();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		// Animation hyperspaceJumpAnimation =
		// AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
		// hyperspaceJumpAnimation.setStartTime(100);
		// im.setAnimation(hyperspaceJumpAnimation);
		// im.startAnimation(hyperspaceJumpAnimation);
		// AnimationDrawable anim = ((AnimationDrawable) im.getBackground());
		// anim.start();
	}

	@Override
	protected void onDestroy() {
		LogUtil.log("onDestroy:" + this);
		super.onDestroy();
		clearBackground();
	}

	@Override
	protected void onResume() {
		LogUtil.log("onResume:" + this);
		super.onResume();
		drawBackground();
	}

	private void drawBackground() {
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		im.setBackgroundResource(R.drawable.splash);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		// Animation hyperspaceJumpAnimation =
		// AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
		// hyperspaceJumpAnimation.setDuration(10000);
		// hyperspaceJumpAnimation.setFillEnabled(true);
		hyperspaceJumpAnimation.setFillAfter(true);
		// hyperspaceJumpAnimation.setFillBefore(false);
		// hyperspaceJumpAnimation.setRepeatMode(Animation.REVERSE);
		// hyperspaceJumpAnimation.setRepeatCount(3);
		hyperspaceJumpAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				LogUtil.log("onAnimationStart");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				LogUtil.log("onAnimationRepeat");

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				LogUtil.log("onAnimationEnd");
				// ImageView im = (ImageView) findViewById(R.id.imageView1);
				// im.setBackgroundResource(R.drawable.house_full);
				// Animation hyperspaceJumpAnimation =
				// AnimationUtils.loadAnimation(IntroActivity.this,
				// R.anim.alpha);
				// hyperspaceJumpAnimation.setFillAfter(true);
				// im.setAnimation(hyperspaceJumpAnimation);
			}
		});
		im.setAnimation(hyperspaceJumpAnimation);
	}

	@Override
	protected void onStop() {
		LogUtil.log("onStop:" + this);
		super.onStop();
		clearBackground();
	}

	private void clearBackground() {
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		im.setBackgroundDrawable(null);
		im.setAnimation(null);
	}

	@Override
	protected void onPause() {
		LogUtil.log("onPause:" + this);
		super.onPause();
		clearBackground();
//		SoundEffectsManager.stopSound();
	}

}
