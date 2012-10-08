package com.sever.android.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends Activity {
	public final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		// im.setBackgroundResource(R.drawable.intro_xml);
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
				// TODO Auto-generated method stub
				System.out.println("onAnimationStart");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
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
		// im.startAnimation(hyperspaceJumpAnimation);

		final Runnable r2 = new Runnable() {
			public void run() {
				Intent intent = new Intent(IntroActivity.this, StartActivity.class);
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
}
