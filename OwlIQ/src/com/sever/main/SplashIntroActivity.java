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
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sever.main.R;

public class SplashIntroActivity extends Activity {

	public final Handler mHandler = new Handler();
	public static SoundPool soundPool;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		Typeface face2 = Typeface.createFromAsset(getAssets(), "FEASFBRG.TTF");

		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setTypeface(face2);
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int deviceWidth = metrics.widthPixels;
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, deviceWidth / 18);
		textView1.setTextColor(Color.YELLOW);
		((RelativeLayout) findViewById(R.id.relativeLayout2)).setVisibility(View.INVISIBLE);
		try {
			list.add(R.drawable.back3);
			list.add(R.drawable.back4);
			list.add(R.drawable.back5);
			list.add(R.drawable.back6);
			list.add(R.drawable.back7);

			setBackGround();

			mp1 = MediaPlayer.create(getBaseContext(), R.raw.horned_owl);
			mp1.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Runnable r2 = new Runnable() {
			public void run() {
				((RelativeLayout) findViewById(R.id.relativeLayout2)).setVisibility(View.VISIBLE);
				final Runnable r2 = new Runnable() {
					public void run() {
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
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
		}
	}

	@Override
	protected void onDestroy() {
		mp1.stop();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public static long firstClick;
	public static long lastClick;

	private void setBackGround() {
		firstClick = 0;
		lastClick = 0;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(list.size());
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
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

}
