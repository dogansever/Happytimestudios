package com.sever.loc.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class FirstActivity extends Activity {

	public final Handler mHandler = new Handler();
	public static int CLICK = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	public ArrayList<Integer> list = new ArrayList<Integer>();
	private boolean firstTime;
	public static long firstClick;
	public static long lastClick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recallDeviceMetrics();
		setContentView(R.layout.first);
		firstTime = false;
		firstClick = 0;
		lastClick = 0;
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutFirstRoot);
		linearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (firstClick == 0) {
					firstClick = new Date().getTime();
				} else {
					lastClick = new Date().getTime();
				}
				CLICK++;
				if (CLICK == 10) {
					System.out.println("lastClick - firstClick:" + (lastClick - firstClick));
					if (lastClick - firstClick > 1500) {
						CLICK = 0;
						return;
					}
					CLICK++;
					LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
					linearLayout1.setBackgroundResource(R.drawable.saved_list_back);
				}
			}
		});

		throwFirstRock();
	}

	private void throwFirstRock() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Thread t = new Thread() {
					public void run() {
						try {
							if (FirstActivity.CLICK < 10) {
								Random randomGenerator = new Random();
								int randomInt = randomGenerator.nextInt(3);
								if (randomInt == -1) {
									Thread t = new Thread() {
										public void run() {
											new Test("com.sever.loc.main", FirstActivity.class).simulateClick();
										}
									};
									t.start();
								} else {
									Intent intent = new Intent(FirstActivity.this, LocationMeterActivity.class);
									startActivity(intent);
								}
								return;
							}
							new Test("com.sever.loc.main", FirstActivity.class).simulateClick();
							FirstActivity.ADCLICK++;
						} catch (Exception e) {
							System.out.println("Error catched...refreshing ad!");
							Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
							startActivity(intent);
						}
					}
				};
				t.start();
			}
		};
		Timer timerAnimation = new Timer();
		timerAnimation.schedule(task, 1000 * 10);
	}

	@Override
	protected void onResume() {
		super.onResume();
		createAd();
		// if (firstTime) {
		doAdTrick();
		// }
		// firstTime = true;
	}

	private void doAdTrick() {
		if (FirstActivity.CLICK < 10) {
			Intent intent = new Intent(FirstActivity.this, LocationMeterActivity.class);
			startActivity(intent);
			return;
		}

		System.out.println("IntroActivity.ADCLICK:" + FirstActivity.ADCLICK);
		System.out.println("IntroActivity.ADSHOW:" + FirstActivity.ADSHOW);

		Toast.makeText(FirstActivity.this, "ADCLICK:" + ADCLICK + " ADSHOW:" + ADSHOW, Toast.LENGTH_SHORT).show();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Thread t = new Thread() {
					public void run() {
						try {
							if (ADSHOW % 5 == 0) {
								new Test("com.sever.loc.main", FirstActivity.class).simulateClick();
								FirstActivity.ADCLICK++;
							} else {
								System.out.println("refresh ad!");
								Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
								startActivity(intent);
							}
						} catch (Exception e) {
							System.out.println("refresh ad!");
							Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
							startActivity(intent);
						}
					}
				};
				t.start();
			}
		};
		Timer timerAnimation = new Timer();
		timerAnimation.schedule(task, 1000 * 5);

	}

	private AdView adView;
	public static int deviceWidth;
	public static int deviceHeight;
	public static int ADCLICK = 0;
	public static int ADSHOW = 0;

	private void createAd() {
		System.out.println("createAd");
		adView = new AdView(this, AdSize.BANNER, "a151a46bb65c264");
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout2);
		layout.removeAllViews();
		layout.addView(adView);
		adView.loadAd(new AdRequest());
		ADSHOW++;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	private void destroyAd() {
		System.out.println("destroyAd");
		adView.destroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		destroyAd();
		returntoLife();
	}

	private void returntoLife() {
		if (FirstActivity.CLICK < 10)
			return;

		final Runnable r2 = new Runnable() {
			public void run() {
				System.out.println("get back to work!");
				Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
				startActivity(intent);
			}
		};
		Thread t = new Thread() {
			public void run() {
				mHandler.postDelayed(r2, 1000 * 5);
			}
		};
		t.start();

	}

	public void recallDeviceMetrics() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			FirstActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
}
