package com.sever.android.main;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sever.android.main.game.GameLoopThread;
import com.sever.android.main.game.GameView;
import com.sever.android.main.sprite.OwlSprite;
import com.sever.android.main.utils.Operation;

public class GameGameActivity extends Activity implements OnTouchListener {
	private static boolean SHOW_INFO_2X = false;
	private static boolean SHOW_INFO_3X = false;
	private boolean paused = false;
	private boolean[] answer = new boolean[5];
	private boolean[] answerCorrect = new boolean[5];
	private int questionIndex;
	private Dialog dialog;
	public static boolean destroyed = true;
	public boolean prepared = false;
	public static GameGameActivity context;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			System.out.println("ACTION_UP:" + v.getId());
			break;
		case MotionEvent.ACTION_POINTER_UP:
			System.out.println("ACTION_POINTER_UP:" + v.getId());
			break;
		case MotionEvent.ACTION_DOWN:
			// System.out.println("ACTION_DOWN:" + v.getId());
			break;
		case MotionEvent.ACTION_MOVE:
			// System.out.println("ACTION_MOVE:" + v.getId());
			break;
		case MotionEvent.ACTION_MASK:
			// System.out.println("ACTION_MASK:" + v.getId());
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			// System.out.println("ACTION_POINTER_DOWN:" + v.getId());
			break;
		default:
			break;
		}

		if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP) {
			switch (v.getId()) {
			case R.id.button1:
			case R.id.Button01:
			case R.id.Button03:
			case R.id.Button05:
			case R.id.Button07:
				doClickWrong(v);
				break;
			case R.id.button2:
			case R.id.Button02:
			case R.id.Button04:
			case R.id.Button06:
			case R.id.Button08:
				doClickRight(v);
				break;
			default:
				break;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		StartActivity.context.releaseBitmaps();
		getGameView().releaseBitmaps();
		destroyed = true;
		MenuActivity.MENU2ON = true;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);

	}

	private void launchGame() {
		final Runnable r = new Runnable() {
			public void run() {
				StartActivity.context.initBitmaps();
				clearSplash();
				prepareGame();
				prepared = true;
			}
		};
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mHandler.post(r);
			}
		};
		timerAnimation = new Timer();
		timerAnimation.schedule(task, 1000);
	}

	private void clearSplash() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relativeLayout.setBackgroundDrawable(null);
	}

	private void prepareSplash() {
		context = this;
		destroyed = false;
		prepared = false;
		// MenuActivity.stopMenuSound();
		((StartActivity) StartActivity.context).playMusicSound();

		setContentView(R.layout.splash);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relativeLayout.setBackgroundResource(R.drawable.back01);

	}

	float tSizeEquations = 14;
	float tSizeScore = 14;
	float tSizePoint = 14;

	private void prepareGame() {
		setContentView(R.layout.main);
		if (StartActivity.deviceWidth <= 480) {
			tSizeEquations = 40;
			tSizeScore = 20;
			tSizePoint = 20;
		} else if (StartActivity.deviceWidth <= 800) {
			tSizeEquations = 50;
			tSizeScore = 25;
			tSizePoint = 25;
		} else if (StartActivity.deviceWidth <= 1024) {
			tSizeEquations = 60;
			tSizeScore = 30;
			tSizePoint = 30;
		} else if (StartActivity.deviceWidth <= 1280) {
			tSizeEquations = 60;
			tSizeScore = 30;
			tSizePoint = 30;
		}

		// Button pause = (Button) findViewById(R.id.button3);
		// pause.getLayoutParams().height = StartActivity.deviceWidth / 16;
		// pause.getLayoutParams().width = StartActivity.deviceWidth / 16;

		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		linearLayout1.getLayoutParams().width = (int) (315.0f * StartActivity.deviceWidth / 1200.0f);

		TextView tv = (TextView) findViewById(R.id.textView7);// middletext
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeScore);
		tv.setTypeface(StartActivity.context.face);
		tv.setVisibility(View.INVISIBLE);

		TextView textView1 = (TextView) findViewById(R.id.textView1);
		TextView textView2 = (TextView) findViewById(R.id.textView2);
		TextView textView3 = (TextView) findViewById(R.id.textView3);
		TextView textView4 = (TextView) findViewById(R.id.textView4);
		TextView textView5 = (TextView) findViewById(R.id.textView5);
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView1.setTypeface(StartActivity.context.face);
		textView2.setTypeface(StartActivity.context.face);
		textView3.setTypeface(StartActivity.context.face);
		textView4.setTypeface(StartActivity.context.face);
		textView5.setTypeface(StartActivity.context.face);
		textView1 = (TextView) findViewById(R.id.TextView02);
		textView2 = (TextView) findViewById(R.id.TextView01);
		textView3 = (TextView) findViewById(R.id.TextView04);
		textView4 = (TextView) findViewById(R.id.TextView03);
		textView5 = (TextView) findViewById(R.id.TextView05);
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView1.setTypeface(StartActivity.context.face);
		textView2.setTypeface(StartActivity.context.face);
		textView3.setTypeface(StartActivity.context.face);
		textView4.setTypeface(StartActivity.context.face);
		textView5.setTypeface(StartActivity.context.face);
		textView1 = (TextView) findViewById(R.id.TextView07);
		textView2 = (TextView) findViewById(R.id.TextView06);
		textView3 = (TextView) findViewById(R.id.TextView09);
		textView4 = (TextView) findViewById(R.id.TextView08);
		textView5 = (TextView) findViewById(R.id.TextView10);
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView1.setTypeface(StartActivity.context.face);
		textView2.setTypeface(StartActivity.context.face);
		textView3.setTypeface(StartActivity.context.face);
		textView4.setTypeface(StartActivity.context.face);
		textView5.setTypeface(StartActivity.context.face);
		textView1 = (TextView) findViewById(R.id.TextView12);
		textView2 = (TextView) findViewById(R.id.TextView11);
		textView3 = (TextView) findViewById(R.id.TextView14);
		textView4 = (TextView) findViewById(R.id.TextView13);
		textView5 = (TextView) findViewById(R.id.TextView15);
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView1.setTypeface(StartActivity.context.face);
		textView2.setTypeface(StartActivity.context.face);
		textView3.setTypeface(StartActivity.context.face);
		textView4.setTypeface(StartActivity.context.face);
		textView5.setTypeface(StartActivity.context.face);
		textView1 = (TextView) findViewById(R.id.TextView17);
		textView2 = (TextView) findViewById(R.id.TextView16);
		textView3 = (TextView) findViewById(R.id.TextView19);
		textView4 = (TextView) findViewById(R.id.TextView18);
		textView5 = (TextView) findViewById(R.id.TextView20);
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeEquations);
		textView1.setTypeface(StartActivity.context.face);
		textView2.setTypeface(StartActivity.context.face);
		textView3.setTypeface(StartActivity.context.face);
		textView4.setTypeface(StartActivity.context.face);
		textView5.setTypeface(StartActivity.context.face);
		prepareLists();
		prepareButtons();
		prepareEquation(0);
		prepareEquation(1);
		prepareEquation(2);
		prepareEquation(3);
		prepareEquation(4);
		createGame();
	}

	public void refreshShow2x3x() {
		if (GameGameActivity.RIGHTCOUNT >= GameGameActivity.RIGHTCOUNT_3X) {
			if (!SHOW_INFO_3X) {
				showMiddleInfoText2x3x("3X");
				SHOW_INFO_3X = true;
			}
		} else if (GameGameActivity.RIGHTCOUNT >= GameGameActivity.RIGHTCOUNT_2X) {
			if (!SHOW_INFO_2X) {
				showMiddleInfoText2x3x("2X");
				SHOW_INFO_2X = true;
			}
		} else {
			SHOW_INFO_3X = false;
			SHOW_INFO_2X = false;
		}
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		prepareSplash();
		launchGame();
	}

	private void createGame() {
		MenuActivity.score = 0;
		RIGHTCOUNT = 0;
		WRONGCOUNT = 0;
		refreshShow2x3x();
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout3);
		if (relativeLayout.getChildCount() != 0) {
			GameView game = (GameView) relativeLayout.getChildAt(0);
			game.stopGame();
			game.invalidate();
			relativeLayout.removeAllViews();
			relativeLayout.invalidate();
		}
		GameView game = new GameView(this);
		relativeLayout.addView(game);
	}

	public void showMiddleInfoText2x3x(final String text) {
		final Runnable r = new Runnable() {
			public void run() {
				TextView tv = (TextView) findViewById(R.id.textView7);
				tv.setText(text);
				tv.setTextColor(Color.RED);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeScore * 3);
				tv.setVisibility(View.VISIBLE);
				startAnimation(tv, R.anim.alpha);
			}
		};
		mHandler.post(r);
	}

	public void showMiddleInfoTextCombo(final String text) {
		System.out.println("showMiddleInfoTextCombo:" + text);
		final Runnable r = new Runnable() {
			public void run() {
				TextView tv = (TextView) findViewById(R.id.textView7);
				tv.setText(text);
				tv.setTextColor(Color.RED);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeScore * 1);
				tv.setVisibility(View.VISIBLE);
				startAnimation(tv, R.anim.alpha);
			}
		};
		mHandler.post(r);
	}

	public void showMiddleInfoText(final String text) {
		final Runnable r = new Runnable() {
			public void run() {
				TextView tv = (TextView) findViewById(R.id.textView7);
				tv.setText(text);
				tv.setTextColor(Color.GREEN);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tSizeScore);
				tv.setVisibility(View.VISIBLE);
				startAnimation(tv, R.anim.alpha);
			}
		};
		mHandler.post(r);
	}

	@Override
	public void onBackPressed() {
		try {
			if (prepared) {
				doPauseClick(null);
			}
		} catch (Exception e) {
		}
	}

	public void hideMiddleInfoText() {
		final Runnable r = new Runnable() {
			public void run() {
				TextView tv = (TextView) findViewById(R.id.textView7);
				tv.setVisibility(View.INVISIBLE);
			}
		};
		mHandler.post(r);
	}

	private void prepareEquation(int i) {
		if (questionIndex >= num1List.size()) {
			prepareLists();
		}
		int num1 = num1List.get(questionIndex);
		int num2 = num2List.get(questionIndex);
		int result = resultList.get(questionIndex);
		answerCorrect[i] = resultListBoolean.get(questionIndex);
		// Operation op = Operation.ADD;
		Operation op = opList.get(questionIndex);
		String opStr = "";
		if (op == Operation.ADD) {
			opStr = "+";
		} else if (op == Operation.DIVIDE) {
			opStr = "/";
		} else if (op == Operation.EXTRACT) {
			opStr = "-";
		} else if (op == Operation.MULTIPLY) {
			opStr = "x";
		}

		TextView textView1 = null;
		TextView textView2 = null;
		TextView textView3 = null;
		TextView textView5 = null;
		switch (i) {
		case 0:
			textView1 = (TextView) findViewById(R.id.textView1);
			textView2 = (TextView) findViewById(R.id.textView2);
			textView3 = (TextView) findViewById(R.id.textView3);
			textView5 = (TextView) findViewById(R.id.textView5);
			break;
		case 1:
			textView1 = (TextView) findViewById(R.id.TextView02);
			textView2 = (TextView) findViewById(R.id.TextView01);
			textView3 = (TextView) findViewById(R.id.TextView04);
			textView5 = (TextView) findViewById(R.id.TextView05);
			break;
		case 2:
			textView1 = (TextView) findViewById(R.id.TextView07);
			textView2 = (TextView) findViewById(R.id.TextView06);
			textView3 = (TextView) findViewById(R.id.TextView09);
			textView5 = (TextView) findViewById(R.id.TextView10);
			break;
		case 3:
			textView1 = (TextView) findViewById(R.id.TextView12);
			textView2 = (TextView) findViewById(R.id.TextView11);
			textView3 = (TextView) findViewById(R.id.TextView14);
			textView5 = (TextView) findViewById(R.id.TextView15);
			break;
		case 4:
			textView1 = (TextView) findViewById(R.id.TextView17);
			textView2 = (TextView) findViewById(R.id.TextView16);
			textView3 = (TextView) findViewById(R.id.TextView19);
			textView5 = (TextView) findViewById(R.id.TextView20);
			break;

		default:
			break;
		}
		textView1.setText("" + num1);
		textView2.setText(opStr);
		textView3.setText("" + num2);
		textView5.setText("" + result);
		questionIndex++;
	}

	private void prepareButtons() {
		LinearLayout layout01 = (LinearLayout) findViewById(R.id.linearLayout3);
		layout01.setVisibility(View.GONE);
		LinearLayout layout02 = (LinearLayout) findViewById(R.id.LinearLayout02);
		layout02.setVisibility(View.GONE);
		LinearLayout layout03 = (LinearLayout) findViewById(R.id.LinearLayout04);
		layout03.setVisibility(View.GONE);
		LinearLayout layout04 = (LinearLayout) findViewById(R.id.LinearLayout06);
		layout04.setVisibility(View.GONE);
		LinearLayout layout05 = (LinearLayout) findViewById(R.id.LinearLayout08);
		layout05.setVisibility(View.GONE);
		Button bwrong0 = (Button) findViewById(R.id.button1);
		Button bwrong1 = (Button) findViewById(R.id.Button01);
		Button bwrong2 = (Button) findViewById(R.id.Button03);
		Button bwrong3 = (Button) findViewById(R.id.Button05);
		Button bwrong4 = (Button) findViewById(R.id.Button07);
		OnTouchListener touchListenerWrong = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					System.out.println("ACTION_UP:" + v.getId());
					doClickWrong(v);
				}
				if (event.getAction() == MotionEvent.ACTION_POINTER_UP) {
					System.out.println("ACTION_POINTER_UP:" + v.getId());
					doClickWrong(v);
				}
				return false;
			}
		};
		bwrong0.setOnTouchListener(this);
		bwrong1.setOnTouchListener(this);
		bwrong2.setOnTouchListener(this);
		bwrong3.setOnTouchListener(this);
		bwrong4.setOnTouchListener(this);

		Button bright0 = (Button) findViewById(R.id.button2);
		Button bright1 = (Button) findViewById(R.id.Button02);
		Button bright2 = (Button) findViewById(R.id.Button04);
		Button bright3 = (Button) findViewById(R.id.Button06);
		Button bright4 = (Button) findViewById(R.id.Button08);
		OnTouchListener touchListenerRight = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					System.out.println("ACTION_UP:" + v.getId());
					doClickRight(v);
				}
				if (event.getAction() == MotionEvent.ACTION_POINTER_UP) {
					System.out.println("ACTION_POINTER_UP:" + v.getId());
					doClickRight(v);
				}
				return false;
			}
		};
		bright0.setOnTouchListener(this);
		bright1.setOnTouchListener(this);
		bright2.setOnTouchListener(this);
		bright3.setOnTouchListener(this);
		bright4.setOnTouchListener(this);

	}

	public GameView getGameView() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout3);
		return (GameView) relativeLayout.getChildAt(0);
	}

	public void doClickWrong(View view) {
		int index = 0;
		switch (view.getId()) {
		case R.id.button1:
			index = 0;
			break;
		case R.id.Button01:
			index = 1;
			break;
		case R.id.Button03:
			index = 2;
			break;
		case R.id.Button05:
			index = 3;
			break;
		case R.id.Button07:
			index = 4;
			break;

		default:
			break;
		}
		System.out.println(this + ":doClickWrong:" + index);
		answer[index] = false;
		if (calculateButton(index)) {
			getGameView().fire(index);
			prepareEquation(index);
		} else {
			getGameView().shootFailed();
			disableButton(index);
			RIGHTCOUNT = 0;
		}
		refreshShow2x3x();
	}

	public void reload() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public void showMenu() {
		dialog = new Dialog(context, R.style.ThemeDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.stage_menu);
		dialog.setCancelable(false);
		// set up button
		TextView textView1 = (TextView) dialog.findViewById(R.id.textView1);
		textView1.setTypeface(StartActivity.context.face);
		textView1.setText("Level " + MenuActivity.level);
		textView1.setTextColor(Color.RED);

		Button buttonList = (Button) dialog.findViewById(R.id.Button09);
		buttonList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doPlayClick();
				dialog.cancel();
				final Runnable r = new Runnable() {
					public void run() {
						hideMenu();
						Intent intent = new Intent(GameGameActivity.this, MenuActivity.class);
						startActivity(intent);
						finish();
					}
				};
				final Thread t = new Thread() {
					public void run() {
						mHandler.postDelayed(r, 500);
					}
				};
				t.start();
			}
		});
		Button buttonRestart = (Button) dialog.findViewById(R.id.Button10);
		buttonRestart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideMenu();
				doPlayClick();
				createGame();
				// getGameView().reloadGame();
			}
		});
		Button buttonResume = (Button) dialog.findViewById(R.id.Button11);
		buttonResume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideMenu();
				doPlayClick();
			}
		});
		dialog.show();
	}

	public void doPlayClick() {
		paused = false;
		// ((Button) view).setBackgroundResource(R.drawable.pause);
		for (Runnable rx : r) {
			if (rx != null) {
				synchronized (rx) {
					justcametolife = true;
					System.out.println(this + ":r.notify()");
					rx.notify();
				}
			}
		}
		getGameView().resume();
	}

	public void doShareClick(View view) {
		share();
	}

	public void doPauseClick(View view) {
		// if (!getGameView().isZombieWaveStarted())
		// return;

		showMenu();
		timePause = new Date().getTime();
		paused = true;
		// ((Button) view).setBackgroundResource(R.drawable.play);
		getGameView().pause();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		hideMenu();
		doPlayClick();
		super.onPause();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		hideMenu();
		doPlayClick();
		super.onStop();
		// finish();
	}

	private void hideMenu() {
		try {
			if (dialog != null) {
				dialog.dismiss();
				dialog.cancel();
			}
		} catch (Exception e) {
		}
	}

	public void startAnimation(View text, int id) {
		Animation slide = AnimationUtils.loadAnimation(this, id);
		slide.setFillAfter(true);
		text.startAnimation(slide);
	}

	public void doClickRight(View view) {
		int index = 0;
		switch (view.getId()) {
		case R.id.button2:
			index = 0;
			break;
		case R.id.Button02:
			index = 1;
			break;
		case R.id.Button04:
			index = 2;
			break;
		case R.id.Button06:
			index = 3;
			break;
		case R.id.Button08:
			index = 4;
			break;

		default:
			break;
		}
		System.out.println(this + ":doClickRight:" + index);
		answer[index] = true;
		if (calculateButton(index)) {
			getGameView().fire(index);
			prepareEquation(index);
		} else {
			getGameView().shootFailed();
			disableButton(index);
			RIGHTCOUNT = 0;
		}
		refreshShow2x3x();
	}

	public final Handler mHandler = new Handler();
	public long timeFired;
	public long timePause;
	public long JAMMED_TIME_MAX = GameLoopThread.FPS * 3;
	public long[] JAMMED_TIME = new long[5];
	private Runnable[] r = new Runnable[5];
	public static boolean justcametolife = false;

	public void refreshJAMMEDTIMEArray() {
		refreshJAMMEDTIME(0);
		refreshJAMMEDTIME(1);
		refreshJAMMEDTIME(2);
		refreshJAMMEDTIME(3);
		refreshJAMMEDTIME(4);
	}

	public void refreshJAMMEDTIME(final int i) {
		if (--JAMMED_TIME[i] == 0) {
			LinearLayout layoutTemp = null;
			Button buttonWrong = null;
			Button buttonRight = null;
			switch (i) {
			case 0:
				layoutTemp = (LinearLayout) findViewById(R.id.linearLayout3);
				buttonWrong = (Button) findViewById(R.id.button1);
				buttonRight = (Button) findViewById(R.id.button2);
				break;
			case 1:
				layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout02);
				buttonWrong = (Button) findViewById(R.id.Button01);
				buttonRight = (Button) findViewById(R.id.Button02);
				break;
			case 2:
				layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout04);
				buttonWrong = (Button) findViewById(R.id.Button03);
				buttonRight = (Button) findViewById(R.id.Button04);
				break;
			case 3:
				layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout06);
				buttonWrong = (Button) findViewById(R.id.Button05);
				buttonRight = (Button) findViewById(R.id.Button06);
				break;
			case 4:
				layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout08);
				buttonWrong = (Button) findViewById(R.id.Button07);
				buttonRight = (Button) findViewById(R.id.Button08);
				break;

			default:
				break;
			}

			final LinearLayout layout = layoutTemp;
			final Button buttonWrongF = buttonWrong;
			final Button buttonRightF = buttonRight;

			if (layout != null) {

				final Runnable r2 = new Runnable() {
					public void run() {
						layout.setVisibility(View.GONE);
						buttonWrongF.setEnabled(true);
						buttonRightF.setEnabled(true);
						prepareEquation(i);
						((OwlSprite) getGameView().spritesOwl.toArray()[i]).jammedEnd();
						layout.invalidate();
						buttonWrongF.invalidate();
						buttonRightF.invalidate();
					}
				};
				final Thread t = new Thread() {
					public void run() {
						mHandler.post(r2);
					}
				};
				t.start();
			}
		}
	}

	private void disableButton(final int i) {
		Log.d("OWLSPACE", this + ":disableButton:" + i);
		((OwlSprite) getGameView().spritesOwl.toArray()[i]).jammed();

		getGameView().createFreeSpritesJammed(i);

		LinearLayout layoutTemp = null;
		Button buttonWrong = null;
		Button buttonRight = null;
		switch (i) {
		case 0:
			layoutTemp = (LinearLayout) findViewById(R.id.linearLayout3);
			buttonWrong = (Button) findViewById(R.id.button1);
			buttonRight = (Button) findViewById(R.id.button2);
			break;
		case 1:
			layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout02);
			buttonWrong = (Button) findViewById(R.id.Button01);
			buttonRight = (Button) findViewById(R.id.Button02);
			break;
		case 2:
			layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout04);
			buttonWrong = (Button) findViewById(R.id.Button03);
			buttonRight = (Button) findViewById(R.id.Button04);
			break;
		case 3:
			layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout06);
			buttonWrong = (Button) findViewById(R.id.Button05);
			buttonRight = (Button) findViewById(R.id.Button06);
			break;
		case 4:
			layoutTemp = (LinearLayout) findViewById(R.id.LinearLayout08);
			buttonWrong = (Button) findViewById(R.id.Button07);
			buttonRight = (Button) findViewById(R.id.Button08);
			break;

		default:
			break;
		}
		// layoutTemp.setVisibility(View.VISIBLE);
		// buttonWrong.setEnabled(false);
		// buttonRight.setEnabled(false);
		final LinearLayout layout = layoutTemp;
		final Button buttonWrongF = buttonWrong;
		final Button buttonRightF = buttonRight;

		JAMMED_TIME[i] = JAMMED_TIME_MAX;

		if (layout != null) {

			final Runnable r2 = new Runnable() {
				public void run() {
					layout.setVisibility(View.VISIBLE);
					buttonWrongF.setEnabled(false);
					buttonRightF.setEnabled(false);
				}
			};
			final Thread t = new Thread() {
				public void run() {
					mHandler.post(r2);
				}
			};
			t.start();
		}
	}

	private Timer timerAnimation;

	private boolean calculateButton(int i) {
		return answer[i] == answerCorrect[i];
	}

	private ArrayList<Integer> num1List = new ArrayList<Integer>();
	private ArrayList<Integer> num2List = new ArrayList<Integer>();
	private ArrayList<Operation> opList = new ArrayList<Operation>();
	private ArrayList<Integer> resultList = new ArrayList<Integer>();
	private ArrayList<Boolean> resultListBoolean = new ArrayList<Boolean>();
	protected static int COUNT = 20;
	private static final int MAXNUM = 10;
	public static int RIGHTCOUNT;
	public static int RIGHTCOUNT_2X = 10;
	public static int RIGHTCOUNT_3X = 15;
	private int WRONGCOUNT;

	private void prepareLists() {
		questionIndex = 0;
		ArrayList<Operation> ops = new ArrayList<Operation>();
		num1List.clear();
		opList.clear();
		num2List.clear();
		resultList.clear();
		resultListBoolean.clear();
		ops.add(Operation.ADD);
		// ops.add(Operation.DIVIDE);
		ops.add(Operation.MULTIPLY);
		ops.add(Operation.EXTRACT);
		Random randomGenerator = new Random();
		for (int idx = 0; idx < COUNT; idx++) {
			int randomInt = randomGenerator.nextInt(MAXNUM);
			randomInt = randomInt == 0 ? 1 : randomInt;
			num1List.add(randomInt);
		}
		for (int idx = 0; idx < COUNT; idx++) {
			int randomInt = randomGenerator.nextInt(ops.size());
			opList.add(ops.get(randomInt));
		}
		for (int idx = 0; idx < COUNT; idx++) {
			int randomInt = randomGenerator.nextInt(MAXNUM);
			randomInt = randomInt == 0 ? 1 : randomInt;
			num2List.add(randomInt);
		}

		ops.add(Operation.RANDOM);
		for (int idx = 0; idx < COUNT; idx++) {
			int randomInt = randomGenerator.nextInt(ops.size() - 1);
			int res = 0;
			int res2 = 0;
			if (ops.get(randomInt) == Operation.ADD) {
				res = num1List.get(idx) + num2List.get(idx);
				// } else if (ops.get(randomInt) == Operation.DIVIDE) {
				// res = num1List.get(idx) / num2List.get(idx);
			} else if (ops.get(randomInt) == Operation.EXTRACT) {
				res = num1List.get(idx) - num2List.get(idx);
			} else if (ops.get(randomInt) == Operation.MULTIPLY) {
				res = num1List.get(idx) * num2List.get(idx);
			} else {
				res = num1List.get(idx) + num2List.get(idx) + 1;
			}
			if (opList.get(idx) == Operation.ADD) {
				res2 = num1List.get(idx) + num2List.get(idx);
				// } else if (ops.get(randomInt) == Operation.DIVIDE) {
				// res = num1List.get(idx) / num2List.get(idx);
			} else if (opList.get(idx) == Operation.EXTRACT) {
				res2 = num1List.get(idx) - num2List.get(idx);
			} else if (opList.get(idx) == Operation.MULTIPLY) {
				res2 = num1List.get(idx) * num2List.get(idx);
			}

			int randomIntX = randomGenerator.nextInt(3);// add 33% randomness
			if (randomIntX == 0) {
				res = res2;
			}
			if (res == res2) {
				resultListBoolean.add(true);
			} else {
				resultListBoolean.add(false);
			}
			resultList.add(res);
		}
	}

	private Bitmap captureScreen() {
		LinearLayout relativeView;
		relativeView = (LinearLayout) findViewById(R.id.mainRoot);
		Bitmap screenshot;
		relativeView.setDrawingCacheEnabled(true);
		relativeView.buildDrawingCache();
		screenshot = Bitmap.createBitmap(relativeView.getDrawingCache());
		relativeView.setDrawingCacheEnabled(false);
		return screenshot;
	}

	private void share() {
		Bitmap b = captureScreen();
		sendMail(b);
	}

	protected void sendMail(Bitmap bitmap) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Owls vs Monsters");
		String emailto = "";
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { emailto });
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://market.android.com/details?id=com.sever.android.main");
		if (bitmap != null) {
			Date date = new Date();
			String fileName = "temp" + date.getYear() + date.getMonth() + date.getDay() + date.getHours() + date.getMinutes() + date.getSeconds() + ".png";
			File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				// e.printStackTrace();
			}
			emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			emailIntent.setType("image/jpeg");
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
		}
		startActivity(emailIntent);
	}

}