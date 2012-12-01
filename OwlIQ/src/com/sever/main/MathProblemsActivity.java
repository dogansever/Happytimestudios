package com.sever.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MathProblemsActivity extends Activity {
	protected static boolean ONLINE = false;
	protected static boolean REFRESH = false;
	protected static boolean REFRESHING = false;
	protected static int COUNT = 20;
	private static final int MAXNUM = 11;
	private static int SCROLLSTEP = 0;
	protected static int CNTDOWN = 3;
	private static int INDEX = 0;
	static String TIME;
	private long STARTDATE;
	private long ENDDATE;
	private boolean finished;
	private ArrayList<Integer> num1List = new ArrayList<Integer>();
	private ArrayList<Integer> num2List = new ArrayList<Integer>();
	private ArrayList<Operation> opList = new ArrayList<Operation>();
	private ArrayList<Integer> resultList = new ArrayList<Integer>();
	private ArrayList<Boolean> resultListBoolean = new ArrayList<Boolean>();
	private Boolean ANSWER;
	private int RIGHTCOUNT;
	private Timer timerAnimation;
	public final Handler mHandler = new Handler();
	private ViewGroup linearLayout4;
	private ScrollView scrollView;
	private boolean DIALOG_VISIBLE;
	private MediaPlayer mp2;
	private MediaPlayer mp1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		mp1 = MediaPlayer.create(getBaseContext(), R.raw.flyby);
		mp1.setVolume(1.0f, 0.0f);
		mp2 = MediaPlayer.create(getBaseContext(), R.raw.metal_clang);
		mp2.setVolume(0.0f, 1.0f);
		CNTDOWN = 3;
		DIALOG_VISIBLE = false;
		INDEX = 0;
		RIGHTCOUNT = 0;
		finished = false;
		setContentView(R.layout.main);
		MainScreenActivity.face = Typeface.createFromAsset(getAssets(), "BADABB__.TTF");
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setTypeface(MainScreenActivity.face);
		button1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 12);
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setTypeface(MainScreenActivity.face);
		button2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 12);
		linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ANSWER = false;
				calculate();
			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ANSWER = true;
				calculate();
			}
		});

		int topHeight = MainScreenActivity.deviceHeight / 12;
		int bottomHeight = MainScreenActivity.deviceHeight / 6;
		int lineHeight = 3 * MainScreenActivity.deviceHeight / 20;
		bottomHeight = bottomHeight + topHeight;
		topHeight = 0;
		SCROLLSTEP = lineHeight;
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		linearLayout1.getLayoutParams().height = topHeight;
		LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
		linearLayout2.getLayoutParams().height = bottomHeight;
		LinearLayout linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
		linearLayout5.getLayoutParams().height = lineHeight;
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
		scrollView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		prepareLists();
		LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
		linearLayout4.addView(getEmptyView());
		linearLayout4.addView(getEmptyView());
		for (int i = 0; i < COUNT; i++) {
			linearLayout4.addView(getView(i));
		}
		linearLayout4.addView(getEmptyView());
		linearLayout4.addView(getEmptyView());
		refreshScroll();

		// startCountDown();
	}

	@Override
	public void onBackPressed() {
		// Intent intent = new Intent(MathProblemsActivity.this,
		// MainScreenActivity.class);
		// startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		setBackGround();
	}

	private void setBackGround() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainRootLinearLayout);
		linearLayout.setBackgroundResource(R.drawable.back2);
		LinearLayout white = (LinearLayout) findViewById(R.id.linearLayout3);
		white.setBackgroundResource(R.drawable.white);
	}

	@Override
	protected void onStart() {
		System.out.println("onStart:" + this);
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		System.out.println("onWindowFocusChanged.hasFocus:" + hasFocus);
		super.onWindowFocusChanged(hasFocus);
		if (CNTDOWN != 0 && hasFocus) {
			startCountDown();
		} else if (CNTDOWN == 0 && !DIALOG_VISIBLE) {
			// Intent intent = new Intent(MathProblemsActivity.this,
			// MainScreenActivity.class);
			// startActivity(intent);
			// finish();
		}

	}

	private void startCountDown() {
		final LinearLayout linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
		final LinearLayout linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
		final TextView textView1 = (TextView) linearLayout6.findViewById(R.id.textView1);
		final Button button1 = (Button) findViewById(R.id.button1);
		final Button button2 = (Button) findViewById(R.id.button2);
		scrollView.setVisibility(View.INVISIBLE);
		linearLayout5.setVisibility(View.INVISIBLE);
		button1.setEnabled(false);
		button2.setEnabled(false);
		textView1.setText("" + CNTDOWN);
		textView1.setTypeface(MainScreenActivity.face);
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 5);
		final Runnable r = new Runnable() {
			public void run() {
				if (CNTDOWN == 0) {
					timerAnimation.cancel();
					scrollView.setVisibility(View.VISIBLE);
					linearLayout5.setVisibility(View.VISIBLE);
					linearLayout6.setVisibility(View.INVISIBLE);
					button1.setEnabled(true);
					button2.setEnabled(true);
					STARTDATE = new Date().getTime();
					return;
				} else {
					textView1.setText("" + (CNTDOWN--));
				}
			}
		};
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mHandler.post(r);
			}
		};
		Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.MILLISECOND, 5000);
		timerAnimation = new Timer();
		timerAnimation.schedule(task, calendar.getTime(), 1000 * 1);

	}

	private void prepareLists() {
		ArrayList<Operation> ops = new ArrayList<Operation>();
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
			if (res == res2) {
				resultListBoolean.add(true);
			} else {
				resultListBoolean.add(false);
			}
			resultList.add(res);
		}

	}

	public View getView(int arg0) {
		LinearLayout view = (LinearLayout) MathProblemsActivity.this.getLayoutInflater().inflate(R.layout.list_item, null);
		// view.setLayoutParams(new
		// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		int lineHeight = 3 * MainScreenActivity.deviceHeight / 20;
		RelativeLayout relativeLayout1 = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
		relativeLayout1.getLayoutParams().height = lineHeight;
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		TextView textView1 = (TextView) view.findViewById(R.id.textView1);
		int num1 = num1List.get(arg0);
		int num2 = num2List.get(arg0);
		int result = resultList.get(arg0);
		// Operation op = Operation.ADD;
		Operation op = opList.get(arg0);
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
		textView1.setText("" + num1);
		textView1.setTypeface(MainScreenActivity.face);
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 8);
		TextView textView2 = (TextView) view.findViewById(R.id.textView2);
		textView2.setTypeface(MainScreenActivity.face);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 8);
		textView2.setText(opStr);
		TextView textView3 = (TextView) view.findViewById(R.id.textView3);
		textView3.setTypeface(MainScreenActivity.face);
		textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 8);
		textView3.setText("" + num2);
		TextView textView4 = (TextView) view.findViewById(R.id.textView4);
		textView4.setTypeface(MainScreenActivity.face);
		textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 8);
		TextView textView5 = (TextView) view.findViewById(R.id.textView5);
		textView5.setTypeface(MainScreenActivity.face);
		textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 8);
		textView5.setText("" + result);
		imageView.setVisibility(View.INVISIBLE);
		return view;
	}

	public View getEmptyView() {
		LinearLayout view = (LinearLayout) MathProblemsActivity.this.getLayoutInflater().inflate(R.layout.list_item, null);
		// view.setLayoutParams(new
		// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		int lineHeight = 3 * MainScreenActivity.deviceHeight / 20;
		RelativeLayout relativeLayout1 = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
		relativeLayout1.getLayoutParams().height = lineHeight;
		view.setVisibility(View.INVISIBLE);
		return view;
	}

	protected void calculate() {
		ENDDATE = new Date().getTime();
		LinearLayout view = (LinearLayout) linearLayout4.getChildAt(INDEX + 2);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		if (resultListBoolean.get(INDEX) == ANSWER) {
			try {
				mp1.start();
			} catch (Exception e) {
			}

			imageView.setImageResource(R.drawable.right);
			RIGHTCOUNT++;
		} else {
			try {
				mp2.start();
			} catch (Exception e) {
			}
			imageView.setImageResource(R.drawable.wrong);
		}
		imageView.setVisibility(View.VISIBLE);

		if (finished)
			return;
		if (INDEX + 1 == MathProblemsActivity.COUNT) {
			float time = new Float(ENDDATE - STARTDATE) / 1000.0f;
			int penalty = (COUNT - RIGHTCOUNT) * 5;
			MathProblemsActivity.TIME = String.format("%10.4f", time + penalty).replace(".", ",");
			String _2TIME = String.format("%01.4f", time).replace(".", ",");
			String message = getString(R.string.result_string).replace("_TIME", TIME).replace("_2TIME", _2TIME).replace("_PENALTY", "" + penalty)
					.replace("_ACC", "" + ((100 * RIGHTCOUNT) / COUNT) + "%");
			Double best;
			try {
				best = Double.parseDouble(((String) MainScreenActivity.dbDBWriteUtil.getBestScore("" + MathProblemsActivity.COUNT, 0)).replace(",", "."));
			} catch (Exception e) {
				best = 1000.0;
			}
			String title = best > time + penalty ? getString(R.string.new_record) : getString(R.string.try_again);
			int icon = best > time + penalty ? R.drawable.owl3 : R.drawable.owl2;
			showTime(title, message, icon, best > time + penalty);
			finished = true;
		} else if (INDEX + 1 < MathProblemsActivity.COUNT) {
			// if (INDEX < MathProblemsActivity.COUNT - 2)
			// linearLayout4.addView(getView(INDEX + 2));
			// else
			// linearLayout4.addView(getEmptyView());
			INDEX++;
			refreshScroll();
		}
	}

	private void refreshScroll() {
		scrollView.scrollTo(0, SCROLLSTEP * (INDEX));
		// mHandler.post(new Runnable() {
		// @Override
		// public void run() {
		// scrollView.scrollTo(0, scrollStep * (INDEX));
		// // scrollView.fullScroll(View.FOCUS_DOWN);
		// }
		// });
	}

	private void showTime(String title, String message, int icon, boolean highscore) {
		DIALOG_VISIBLE = true;
		AlertDialog alertDialog = new AlertDialog.Builder(this).setIcon(icon).create();
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		alertDialog.setMessage(message);
		final LinearLayout view = (LinearLayout) MathProblemsActivity.this.getLayoutInflater().inflate(R.layout.input, null);
		alertDialog.setView(view);
		alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				EditText input = (EditText) view.findViewById(R.id.editText1);
				String info = input.getText().toString();

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				String dateStr = formatter.format(new Date());
				info = info.trim().equals("") ? "OWLY, " + dateStr : info + ", " + dateStr;
				MainScreenActivity.dbDBWriteUtil.addScore(TIME, "" + new Date().getTime(), "" + COUNT, info);

				info = input.getText().toString();
				info = info.trim().equals("") ? "OWLY" : info.trim();
				String playerName = info;
				int points = getPoints();
				new LeaderBoardUtil().leaderboardSave(playerName, points, COUNT);
				finish();
			}
		});
		alertDialog.show();
	}

	public static String getTimeFromPoints(int points) {
		float time = COUNT * 10000000.0f / points;
		return String.format("%01.4f", time).replace(",", ".");
	}

	public static int getPoints() {
		return (int) (COUNT * 10000000.0f / (Double.parseDouble(TIME.replace(",", "."))));
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainRootLinearLayout);
		linearLayout.setBackgroundDrawable(null);
		LinearLayout white = (LinearLayout) findViewById(R.id.linearLayout3);
		white.setBackgroundDrawable(null);

		unbindDrawables(findViewById(R.id.mainRootLinearLayout));
		System.gc();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
		// SplashIntroActivity.lastStopped = this;
		finish();
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
			((ViewGroup) view).removeAllViews();
		}
	}
}