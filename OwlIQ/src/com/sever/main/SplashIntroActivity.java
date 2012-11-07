package com.sever.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashIntroActivity extends Activity {

	static Activity lastStopped = null;
	public final Handler mHandler = new Handler();
	public static SoundPool soundPool;

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

	public ArrayList<Integer> list = new ArrayList<Integer>();
	private MediaPlayer mp1;
	public static int streamID;
	public static int streamIDmain;
	public static int streamIDmain2;
	public static int streamIDwrong;
	public static int streamIDright;
	public static int soundIDintro;
	public static int soundIDmain;
	public static int soundIDmain2;
	public static int soundIDwrong;
	public static int soundIDright;
	public static int CLICK = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		if (SplashIntroActivity.lastStopped != null) {
			Intent intent = new Intent(this, SplashIntroActivity.lastStopped.getClass());
			// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			return;

		}

		setContentView(R.layout.first);
		Typeface face2 = Typeface.createFromAsset(getAssets(), "FEASFBRG.TTF");

		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setTypeface(face2);
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int deviceWidth = metrics.widthPixels;
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, deviceWidth / 18);
		textView1.setTextColor(Color.YELLOW);
		((RelativeLayout) findViewById(R.id.relativeLayout2)).setVisibility(View.GONE);
		try {
			list.add(R.drawable.back3);
			list.add(R.drawable.back4);
			list.add(R.drawable.back5);
			list.add(R.drawable.back6);
			list.add(R.drawable.back7);

			mp1 = MediaPlayer.create(getBaseContext(), R.raw.horned_owl);
			mp1.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

		final Runnable r2 = new Runnable() {
			public void run() {
				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
				linearLayout.setBackgroundDrawable(null);
				RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);
				relativeLayout.setBackgroundResource(R.drawable.cover);

				((RelativeLayout) findViewById(R.id.relativeLayout2)).setVisibility(View.VISIBLE);
				final Runnable r2 = new Runnable() {
					public void run() {
						RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);
						relativeLayout.setBackgroundDrawable(null);
						Intent intent = new Intent(SplashIntroActivity.this, MainScreenActivity.class);
						startActivity(intent);
						finish();
					}
				};
				Thread t = new Thread() {
					public void run() {
						mHandler.postDelayed(r2, 1000 * 5);
					}
				};
				t.start();
			}
		};
		Thread t = new Thread() {
			public void run() {
				mHandler.postDelayed(r2, 1000 * 5);
			}
		};
		t.start();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		try {
			mp1.stop();
			mp1.release();
			RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);
			relativeLayout.setBackgroundDrawable(null);
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
			linearLayout.setBackgroundDrawable(null);

			unbindDrawables(findViewById(R.id.firstLayoutRoot));
			System.gc();
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		setBackGround();
	}

	public static long firstClick;
	public static long lastClick;

	private void setBackGround() {
		firstClick = 0;
		lastClick = 0;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(list.size());
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
		linearLayout.setBackgroundDrawable(null);
		linearLayout.setBackgroundResource(list.get(randomInt));
		linearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (firstClick == 0) {
					firstClick = new Date().getTime();
				} else {
					lastClick = new Date().getTime();
				}
				if (CLICK++ == 10) {
					System.out.println("lastClick - firstClick:" + (lastClick - firstClick));
					if (lastClick - firstClick > 1500) {
						CLICK = 0;
						return;
					}
					CLICK++;
				}
			}
		});
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
			view.setBackgroundDrawable(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			// ((ViewGroup) view).removeAllViews();
		}
	}
}
