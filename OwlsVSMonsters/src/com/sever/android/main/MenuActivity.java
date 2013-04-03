package com.sever.android.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.sever.android.main.game.GameView;
import com.sever.android.main.utils.DBWriteUtil;

public class MenuActivity extends Activity {
	private static final int STAGE_COUNT = 4;
	private static MediaPlayer mp1 = null;
	private static MediaPlayer mp2 = null;
	public static boolean soundOn = true;
	private int deviceWidth;
	private int deviceHeight;
	public static boolean MENU2ON;
	private int width;
	private int height;
	private int width2;
	private int height2;
	private ArrayList<Integer> background = new ArrayList<Integer>();
	private StickyHorizontalScrollView scrollView;
	private LinearLayout menu2Item;
	public static int level;
	public static int stage;
	public static int highscore;
	public static int score;
	public static int star;

	public void recallDeviceMetrics() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
		} catch (Exception e) {
		}
	}

	private Timer timerAnimation;
	public final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		recallDeviceMetrics();
		// menu
		setContentView(R.layout.menu);

		background.clear();
		background.add(R.drawable.back01);
		background.add(R.drawable.back02);
		background.add(R.drawable.back03);
		background.add(R.drawable.back04);

	}

	private void clearFirstMenu() {
		try {
			LinearLayout layoutHorizontal = (LinearLayout) scrollView.getChildAt(0);
			int cnt = layoutHorizontal.getChildCount();
			for (int i = 0; i < cnt; i++) {
				LinearLayout menuItem = (LinearLayout) layoutHorizontal.getChildAt(i);
				ImageButton imageButton = (ImageButton) menuItem.findViewById(R.id.imageButton1);
				imageButton.setBackgroundDrawable(null);

			}
			scrollView = null;
		} catch (Exception e) {
		}
	}

	private void recreateFirstMenu() {
		float bmpPercentage = 0.25f;
		scrollView = new StickyHorizontalScrollView(this);
		LinearLayout layoutHorizontal = new LinearLayout(this);
		layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
		layoutHorizontal.setBackgroundColor(Color.TRANSPARENT);
		scrollView.addView(layoutHorizontal);
		scrollView.setBackgroundColor(Color.TRANSPARENT);
		scrollView.setHorizontalFadingEdgeEnabled(false);
		scrollView.setHorizontalScrollBarEnabled(false);

		LinearLayout menuItem;
		ImageButton imageButton;

		menuItem = (LinearLayout) getLayoutInflater().inflate(R.layout.menu_item, null);
		menuItem.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * 0.25f), deviceHeight));
		imageButton = (ImageButton) menuItem.findViewById(R.id.imageButton1);
		imageButton.setVisibility(View.INVISIBLE);
		layoutHorizontal.addView(menuItem);

		menuItem = (LinearLayout) getLayoutInflater().inflate(R.layout.menu_item, null);
		menuItem.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * 0.5f), deviceHeight));
		imageButton = (ImageButton) menuItem.findViewById(R.id.imageButton1);
		imageButton.setBackgroundDrawable(getStageBitmap(0));
		imageButton.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * bmpPercentage), (int) ((deviceWidth * height2 / width2) * bmpPercentage)));
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(5);
				if (randomInt == 0) {
					simClick();
				} else {
					startMenuButtonSound();
					drawSecondMenu();
				}

			}
		});
		layoutHorizontal.addView(menuItem);
		menuItem = (LinearLayout) getLayoutInflater().inflate(R.layout.menu_item, null);
		menuItem.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * 0.5f), deviceHeight));
		imageButton = (ImageButton) menuItem.findViewById(R.id.imageButton1);
		imageButton.setBackgroundDrawable(getStageBitmap(1));
		imageButton.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * bmpPercentage), (int) ((deviceWidth * height2 / width2) * bmpPercentage)));
		layoutHorizontal.addView(menuItem);

		menuItem = (LinearLayout) getLayoutInflater().inflate(R.layout.menu_item, null);
		menuItem.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * 0.5f), deviceHeight));
		imageButton = (ImageButton) menuItem.findViewById(R.id.imageButton1);
		imageButton.setBackgroundDrawable(getStageBitmap(2));
		imageButton.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * bmpPercentage), (int) ((deviceWidth * height2 / width2) * bmpPercentage)));
		layoutHorizontal.addView(menuItem);

		menuItem = (LinearLayout) getLayoutInflater().inflate(R.layout.menu_item, null);
		menuItem.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * 0.5f), deviceHeight));
		imageButton = (ImageButton) menuItem.findViewById(R.id.imageButton1);
		imageButton.setBackgroundDrawable(getStageBitmap(3));
		imageButton.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * bmpPercentage), (int) ((deviceWidth * height2 / width2) * bmpPercentage)));
		layoutHorizontal.addView(menuItem);

		menuItem = (LinearLayout) getLayoutInflater().inflate(R.layout.menu_item, null);
		menuItem.setLayoutParams(new LinearLayout.LayoutParams((int) (deviceWidth * 0.25f), deviceHeight));
		imageButton = (ImageButton) menuItem.findViewById(R.id.imageButton1);
		imageButton.setVisibility(View.INVISIBLE);
		layoutHorizontal.addView(menuItem);

		scrollView.addStickyPosition((int) (deviceWidth * 0.0f)); // set sticky
		scrollView.addStickyPosition((int) (deviceWidth * 0.5f)); // set sticky
		scrollView.addStickyPosition((int) (deviceWidth * 1.0f)); // set sticky
		scrollView.addStickyPosition((int) (deviceWidth * 1.5f)); // set sticky
	}

	private void drawFirstMenu() {
		MENU2ON = false;
		if (scrollView == null) {
			recreateFirstMenu();
		}
		LinearLayout layout = (LinearLayout) findViewById(R.id.menulinearLayout1);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.removeAllViews();
		layout.addView(scrollView, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setBackgroundColor(Color.TRANSPARENT);
	}

	protected void drawSecondMenu() {
		MENU2ON = true;
		if (menu2Item == null) {
			recreateSecondMenu();
		}

		LinearLayout layout = (LinearLayout) findViewById(R.id.menulinearLayout1);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.removeAllViews();
		layout.addView(menu2Item, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setBackgroundColor(Color.TRANSPARENT);
	}

	private void clearSecondMenu() {
		try {
			ImageButton imageButton = (ImageButton) menu2Item.findViewById(R.id.imageButton1);
			imageButton.setBackgroundDrawable(null);
			ImageButton imageButton1 = (ImageButton) menu2Item.findViewById(R.id.ImageButton01);
			imageButton1.setBackgroundDrawable(null);
			ImageButton imageButton2 = (ImageButton) menu2Item.findViewById(R.id.ImageButton02);
			imageButton2.setBackgroundDrawable(null);
			ImageButton imageButton3 = (ImageButton) menu2Item.findViewById(R.id.ImageButton03);
			imageButton3.setBackgroundDrawable(null);
			ImageButton imageButton4 = (ImageButton) menu2Item.findViewById(R.id.ImageButton04);
			imageButton4.setBackgroundDrawable(null);
			ImageButton imageButton5 = (ImageButton) menu2Item.findViewById(R.id.ImageButton05);
			imageButton5.setBackgroundDrawable(null);
			ImageButton imageButton6 = (ImageButton) menu2Item.findViewById(R.id.ImageButton06);
			imageButton6.setBackgroundDrawable(null);
			ImageButton imageButton7 = (ImageButton) menu2Item.findViewById(R.id.ImageButton07);
			imageButton7.setBackgroundDrawable(null);
			ImageButton imageButton8 = (ImageButton) menu2Item.findViewById(R.id.ImageButton08);
			imageButton8.setBackgroundDrawable(null);
			ImageButton imageButton9 = (ImageButton) menu2Item.findViewById(R.id.ImageButton09);
			imageButton9.setBackgroundDrawable(null);
			menu2Item = null;
		} catch (Exception e) {
		}
	}

	private void recreateSecondMenu() {
		menu2Item = (LinearLayout) getLayoutInflater().inflate(R.layout.menu2_item, null);
		ImageButton imageButton = (ImageButton) menu2Item.findViewById(R.id.imageButton1);
		stage = 1;
		level = 1;
		imageButton.setBackgroundDrawable(getLevelBitmap());
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 1;
				startGameActivity();
			}
		});
		ImageButton imageButton1 = (ImageButton) menu2Item.findViewById(R.id.ImageButton01);
		level = 2;
		imageButton1.setBackgroundDrawable(getLevelBitmap());
		imageButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 2;
				startGameActivity();
			}
		});
		ImageButton imageButton2 = (ImageButton) menu2Item.findViewById(R.id.ImageButton02);
		level = 3;
		imageButton2.setBackgroundDrawable(getLevelBitmap());
		imageButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 3;
				startGameActivity();
			}
		});
		ImageButton imageButton3 = (ImageButton) menu2Item.findViewById(R.id.ImageButton03);
		level = 4;
		imageButton3.setBackgroundDrawable(getLevelBitmap());
		imageButton3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 4;
				startGameActivity();
			}
		});
		ImageButton imageButton4 = (ImageButton) menu2Item.findViewById(R.id.ImageButton04);
		level = 5;
		imageButton4.setBackgroundDrawable(getLevelBitmap());
		imageButton4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 5;
				startGameActivity();
			}
		});
		level = 6;
		ImageButton imageButton5 = (ImageButton) menu2Item.findViewById(R.id.ImageButton05);
		imageButton5.setBackgroundDrawable(getLevelBitmap());
		imageButton5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 6;
				startGameActivity();
			}
		});
		level = 7;
		ImageButton imageButton6 = (ImageButton) menu2Item.findViewById(R.id.ImageButton06);
		imageButton6.setBackgroundDrawable(getLevelBitmap());
		imageButton6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 7;
				startGameActivity();
			}
		});
		level = 8;
		ImageButton imageButton7 = (ImageButton) menu2Item.findViewById(R.id.ImageButton07);
		imageButton7.setBackgroundDrawable(getLevelBitmap());
		imageButton7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 8;
				startGameActivity();
			}
		});
		level = 9;
		ImageButton imageButton8 = (ImageButton) menu2Item.findViewById(R.id.ImageButton08);
		imageButton8.setBackgroundDrawable(getLevelBitmap());
		imageButton8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 9;
				startGameActivity();
			}
		});
		level = 10;
		ImageButton imageButton9 = (ImageButton) menu2Item.findViewById(R.id.ImageButton09);
		imageButton9.setBackgroundDrawable(getLevelBitmap());
		imageButton9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				level = 10;
				startGameActivity();
			}
		});
		clearFirstMenu();
	}

	protected void startGameActivity() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(5);
		if (randomInt == 0) {
			simClick();
		} else {
			GameView.waveCount = GameView.waveCountList[level - 1];
			ContentValues cv = StartActivity.dbDBWriteUtil.getScore("" + stage, "" + level);
			if (cv == null) {
				return;
			} else {
				highscore = cv.getAsInteger(DBWriteUtil.scoreColumn);
			}
			Intent intent = new Intent(MenuActivity.this, GameGameActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void simClick() {
		Thread t = new Thread() {
			public void run() {
				new Test("com.sever.android.main", MenuActivity.class).simulateClick();
			}
		};
		t.start();
	}

	public static void refreshLevelScore() {
		ContentValues cv = StartActivity.dbDBWriteUtil.getScore("" + stage, "" + level);
		star = -1;
		if (cv != null) {
			highscore = cv.getAsInteger(DBWriteUtil.scoreColumn);
			star = cv.getAsInteger(DBWriteUtil.scoreStarColumn);
		}
	}

	private Drawable getLevelBitmap() {
		ContentValues cv = StartActivity.dbDBWriteUtil.getScore("" + stage, "" + level);
		star = -1;
		if (cv != null) {
			highscore = cv.getAsInteger(DBWriteUtil.scoreColumn);
			star = cv.getAsInteger(DBWriteUtil.scoreStarColumn);
		}
		Bitmap bm = Bitmap.createBitmap(StartActivity.levelBmp, (star + 1) * width, (level - 1) * height, width, height);
		Drawable d = new BitmapDrawable(bm);
		return d;
	}

	private Drawable getStageBitmap(int number) {
		Bitmap bm = Bitmap.createBitmap(StartActivity.stageBmp, number * width2, 0, width2, height2);
		Drawable d = new BitmapDrawable(bm);
		return d;
	}

	public void doInfoClick(View v) {
		Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
		startActivity(intent);
	}

	public void doSoundClick(View v) {
		Button button = (Button) findViewById(R.id.Button01sound);
		soundOn = !soundOn;
		if (soundOn) {
			button.setBackgroundResource(R.drawable.sound_on);
			startMenuSound();
		} else {
			button.setBackgroundResource(R.drawable.sound_off);
			stopMenuSound();
		}
	}

	@Override
	public void onBackPressed() {
		System.out.println("onBackPressed:" + this);
		if (MENU2ON) {
			drawFirstMenu();
		} else {
			destroyAd();
			stopMenuSound();
			clearMenu();
			clearBackground();
			Intent intent = new Intent(MenuActivity.this, StartActivity.class);
			startActivity(intent);
		}
		// return;
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		clearFirstMenu();
		clearSecondMenu();
		clearBackground();
		StartActivity.context.releaseBitmapsMenu();
		StartActivity.printMemory();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		createAd();
		try {
			if (soundOn) {
				startMenuSound();
			} else {
				stopMenuSound();
			}
			drawBackground();
		} catch (Exception e) {
		}
		drawMenu();
		StartActivity.printMemory();
	}

	private void drawMenu() {
		final Runnable r = new Runnable() {
			public void run() {
				StartActivity.context.initBitmapsMenu();
				width = StartActivity.levelBmp.getWidth() / 5;
				height = StartActivity.levelBmp.getHeight() / 10;
				width2 = StartActivity.stageBmp.getWidth() / STAGE_COUNT;
				height2 = StartActivity.stageBmp.getHeight() / 1;
				if (!MENU2ON) {
					drawFirstMenu();
				} else {
					drawSecondMenu();
				}
			}
		};
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (GameGameActivity.destroyed) {
					mHandler.post(r);
					timerAnimation.cancel();
				}
			}
		};
		timerAnimation = new Timer();
		timerAnimation.schedule(task, new Date(), 1000);
	}

	private void drawBackground() {
		Button button = (Button) findViewById(R.id.Button01sound);
		if (soundOn) {
			button.setBackgroundResource(R.drawable.sound_on);
		} else {
			button.setBackgroundResource(R.drawable.sound_off);
		}
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.menurelativeLayout1);
		Random rnd = new Random();
		relativeLayout.setBackgroundResource(background.get(rnd.nextInt(background.size())));
	}

	public static void startMenuButtonSound() {
		if (soundOn) {
			mp2 = MediaPlayer.create(StartActivity.context, R.raw.woop_woop);
			mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp2.start();
		}
	}

	public static void startMenuSound() {
		if (mp1 == null) {
			mp1 = MediaPlayer.create(StartActivity.context, R.raw.nightime);
			mp1.setLooping(true);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		if (!mp1.isPlaying())
			mp1.start();
		StartActivity.startIntroSound();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
	}

	private void clearBackground() {
		Button button = (Button) findViewById(R.id.Button01sound);
		button.setBackgroundDrawable(null);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.menurelativeLayout1);
		relativeLayout.setBackgroundDrawable(null);
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
		destroyAd();
		stopMenuSound();
		clearMenu();
		clearBackground();
		StartActivity.printMemory();
	}

	private void clearMenu() {
		clearFirstMenu();
		clearSecondMenu();
	}

	public static void stopMenuSound() {
		mp1.pause();
		StartActivity.stopIntroSound();
	}

	private AdView adView;

	private void createAd() {
		System.out.println("createAd");
		adView = new AdView(this, AdSize.BANNER, "a14fc682faa77f3");
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutAdview);
		layout.removeAllViews();
		layout.addView(adView);
		adView.loadAd(new AdRequest());
	}

	private void destroyAd() {
		System.out.println("destroyAd");
		adView.destroy();
	}
}