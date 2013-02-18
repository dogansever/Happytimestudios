package com.sever.physic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class PreIntroActivity extends Activity {

	public final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preintro);
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
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		clearBackground();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
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
				System.out.println("onAnimationStart");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				System.out.println("onAnimationRepeat");

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				System.out.println("onAnimationEnd");
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
		System.out.println("onStop:" + this);
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
		System.out.println("onPause:" + this);
		super.onPause();
		clearBackground();
	}

}
