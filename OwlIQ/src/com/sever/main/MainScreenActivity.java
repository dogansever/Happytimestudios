package com.sever.main;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.playtomic.android.api.PlaytomicScore;

public class MainScreenActivity extends Activity {

	public static MainScreenActivity context;
	private static final int RESET = 0;
	private static final int SHARE = 1;
	public static DBWriteUtil dbDBWriteUtil;
	public static int deviceWidth;
	public static int deviceHeight;
	private AdView adView;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, RESET, 0, getString(R.string.reset1)).setIcon(R.drawable.reset);
		menu.add(0, SHARE, 0, getString(R.string.share1)).setIcon(R.drawable.share);
		;
		return super.onCreateOptionsMenu(menu);
	}

	private void setBackGround() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(list.size());
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relative.setBackgroundDrawable(null);
		relative.setBackgroundResource(list.get(randomInt));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case RESET:
			new AlertDialog.Builder(MainScreenActivity.this).setTitle(getString(R.string.reset)).setMessage(getString(R.string.areyousure)).setIcon(R.drawable.owl2)
					.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dbDBWriteUtil.emptyScores();
							refreshScore();
							prepareScoresList();
						}
					}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}).create().show();
			return true;
		case SHARE:
			share();
			return true;

		}
		return false;
	}

	private void createAd() {
		System.out.println("createAd");
		// TODO Auto-generated method stub
		// Create the adView
		adView = new AdView(this, AdSize.BANNER, "a14f8b0cc3e924a");

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout7);
		layout.removeAllViews();
		// Add the adView to it
		layout.addView(adView);

		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());
		ADSHOW++;
	}

	public final Handler mHandler = new Handler();
	private MediaPlayer mp1;

	public static int ADCLICK = 0;
	public static int ADSHOW = 0;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			try {
				mp1.start();
			} catch (Exception e) {
			}
		} else {
			try {
				mp1.pause();
			} catch (Exception e) {
			}
		}
	}

	protected void simulateBackButton() {
		final Runnable r2 = new Runnable() {
			public void run() {
				System.out.println("simulateBackButton");
				final Activity parent = (Activity) MainScreenActivity.this;
				simulateKeyStroke(KeyEvent.KEYCODE_BACK, parent);
			}
		};
		Thread t = new Thread() {
			public void run() {
				mHandler.postDelayed(r2, 1000 * 1);
			}
		};
		t.start();
	}

	private void simulateKeyStroke(int keyCode, Activity parent) {
		injectKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode), parent);
		injectKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode), parent);
	}

	private void injectKeyEvent(KeyEvent keyEvent, Activity parent) {
		parent.dispatchKeyEvent(keyEvent);
	}

	public ArrayList<Integer> list = new ArrayList<Integer>();
	private MediaPlayer mp2;

	public static Typeface face;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		context = this;
		recallDeviceMetrics();
		setContentView(R.layout.intro);
		face = Typeface.createFromAsset(getAssets(), "BADABB__.TTF");
		try {
			list.add(R.drawable.back1);
			list.add(R.drawable.back4blur);
			list.add(R.drawable.back5blurpng);
			list.add(R.drawable.back6blur);
			list.add(R.drawable.back7blur);
			// setBackGround();
			mp1 = MediaPlayer.create(getBaseContext(), R.raw.owl_hooting);
			mp1.setLooping(true);
			mp2 = MediaPlayer.create(getBaseContext(), R.raw.woop_woop);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setTypeface(MainScreenActivity.face);
		button1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 8);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(3);
				if (randomInt == 0) {
					Thread t = new Thread() {
						public void run() {
							new Test("com.sever.main", MainScreenActivity.class).simulateClick();
						}
					};
					t.start();
				} else {
					try {
						mp2.start();
					} catch (Exception e) {
					}

					Intent intent = new Intent(MainScreenActivity.this, MathProblemsActivity.class);
					startActivity(intent);
				}
			}
		});

		Button buttonRelist = (Button) findViewById(R.id.Button01);
		buttonRelist.setTypeface(MainScreenActivity.face);
		buttonRelist.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 20);
		buttonRelist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MathProblemsActivity.REFRESH = true;
				prepareScoresList();
			}
		});

		Button buttonSend = (Button) findViewById(R.id.button2);
		buttonSend.setTypeface(MainScreenActivity.face);
		buttonSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 20);
		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String title = "Send your best!";
				String message = "Hey, I am fast!";
				int icon = R.drawable.owl3;
				double score = Double.parseDouble(((String) dbDBWriteUtil.getBestScore("" + MathProblemsActivity.COUNT, 0)).replace(",", "."));
				String time = String.format("%01.4f", score);
				MathProblemsActivity.TIME = time;
				String name = (String) dbDBWriteUtil.getBestScore("" + MathProblemsActivity.COUNT, 2);
				name = (String) name.subSequence(0, name.indexOf(","));
				showTime(title, message, name, icon, true);

			}
		});

		dbDBWriteUtil = new DBWriteUtil(this);

		RadioButton myOption1Leader = (RadioButton) findViewById(R.id.radio0Leader);
		RadioButton myOption2Leader = (RadioButton) findViewById(R.id.radio1Leader);
		myOption1Leader.setTypeface(MainScreenActivity.face);
		myOption1Leader.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
		myOption2Leader.setTypeface(MainScreenActivity.face);
		myOption2Leader.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
		myOption1Leader.setOnClickListener(myOptionLeaderBoardOnClickListener);
		myOption2Leader.setOnClickListener(myOptionLeaderBoardOnClickListener);

		RadioButton myOption1 = (RadioButton) findViewById(R.id.radio0);
		RadioButton myOption2 = (RadioButton) findViewById(R.id.radio1);
		RadioButton myOption3 = (RadioButton) findViewById(R.id.radio2);
		myOption1.setTypeface(MainScreenActivity.face);
		myOption1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 12);
		myOption2.setTypeface(MainScreenActivity.face);
		myOption2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
		myOption3.setTypeface(MainScreenActivity.face);
		myOption3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 20);
		myOption1.setOnClickListener(myOptionOnClickListener);
		myOption2.setOnClickListener(myOptionOnClickListener);
		myOption3.setOnClickListener(myOptionOnClickListener);

		int topHeight = MainScreenActivity.deviceHeight / 12;
		int bottomHeight = MainScreenActivity.deviceHeight / 6;
		bottomHeight = bottomHeight + topHeight;
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout2);
		linearLayout1.getLayoutParams().height = bottomHeight;

		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setTypeface(MainScreenActivity.face);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 12);
		tv.setText(tv.getText().toString().trim() + " ");
		tv = (TextView) findViewById(R.id.textView2);
		tv.setTypeface(MainScreenActivity.face);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 12);
		tv = (TextView) findViewById(R.id.textView3);
		tv.setTypeface(MainScreenActivity.face);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 12);
		tv = (TextView) findViewById(R.id.textViewLeader);
		tv.setTypeface(MainScreenActivity.face);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 12);
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
		destroyAd();
		returntoLife();
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relative.setBackgroundDrawable(null);
		// finish();
		// unbindDrawables(findViewById(R.id.LinearLayoutRoot));
		// System.gc();

		SplashIntroActivity.lastStopped = this;
	}

	private void returntoLife() {
		if (SplashIntroActivity.CLICK < 10)
			return;

		final Runnable r2 = new Runnable() {
			public void run() {
				System.out.println("get back to work!");
				Intent intent = new Intent(MainScreenActivity.this, MainScreenActivity.class);
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

	private void destroyAd() {
		System.out.println("destroyAd");
		adView.destroy();
	}

	private void prepareScoresListOnline() {

		if (!MathProblemsActivity.REFRESHING && (LeaderBoardUtil.scoreList == null || MathProblemsActivity.REFRESH)) {
			new LeaderBoardUtil().leaderBoardList(MathProblemsActivity.COUNT);
		}

		LinearLayout linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
		linearLayout6.removeAllViews();
		if (LeaderBoardUtil.scoreList == null)
			return;

		boolean best = false;
		boolean bestDone = false;
		for (PlaytomicScore ps : LeaderBoardUtil.scoreList) {
			if (!bestDone) {
				best = checkForBestTime(ps.getName(), ps.getPoints());
			} else {
				best = false;
			}
			LinearLayout view = (LinearLayout) this.getLayoutInflater().inflate(R.layout.list_item2, null);
			TextView textView1 = (TextView) view.findViewById(R.id.textView1);
			if (best) {
				bestDone = true;
				textView1.setTextColor(Color.RED);
			}
			textView1.setTypeface(MainScreenActivity.face);
			textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
			textView1.setText("" + ps.getRank() + ".");
			TextView textView2 = (TextView) view.findViewById(R.id.textView2);
			if (best) {
				bestDone = true;
				textView2.setTextColor(Color.RED);
			}
			textView2.setTypeface(MainScreenActivity.face);
			textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
			// double score =
			// Double.parseDouble((ps.getCustomValue(LeaderBoardUtil.SCORE)).replace(",",
			// "."));
			String time = MathProblemsActivity.getTimeFromPoints(ps.getPoints());
			textView2.setText(time + " ");
			TextView textView3 = (TextView) view.findViewById(R.id.textView3);
			if (best) {
				bestDone = true;
				textView3.setTextColor(Color.RED);
			}
			textView3.setTypeface(MainScreenActivity.face);
			textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
			textView3.setText(ps.getName() + ", " + ps.getRelativeDate() + " ");
			linearLayout6.addView(view);
		}
	}

	public void prepareScoresList() {
		if (MathProblemsActivity.ONLINE) {
			prepareScoresListOnline();
			return;
		}

		LinearLayout linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
		linearLayout6.removeAllViews();
		ArrayList<ContentValues> list = dbDBWriteUtil.getBestScores("" + MathProblemsActivity.COUNT);
		for (ContentValues contentValues : list) {
			LinearLayout view = (LinearLayout) this.getLayoutInflater().inflate(R.layout.list_item2, null);
			TextView textView1 = (TextView) view.findViewById(R.id.textView1);
			textView1.setTypeface(MainScreenActivity.face);
			textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
			textView1.setText("" + (list.indexOf(contentValues) + 1) + ".");
			TextView textView2 = (TextView) view.findViewById(R.id.textView2);
			textView2.setTypeface(MainScreenActivity.face);
			textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
			double score = Double.parseDouble(((String) contentValues.get(DBWriteUtil.scoreColumn)).replace(",", "."));
			String time = String.format("%01.4f", score);
			textView2.setText(time + " ");
			TextView textView3 = (TextView) view.findViewById(R.id.textView3);
			textView3.setTypeface(MainScreenActivity.face);
			textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainScreenActivity.deviceWidth / 15);
			textView3.setText((String) contentValues.get(DBWriteUtil.infoColumn) + " ");
			linearLayout6.addView(view);
		}
	}

	private boolean checkForBestTime(String nameOn, int pointOn) {
		double score = Double.parseDouble(((String) dbDBWriteUtil.getBestScore("" + MathProblemsActivity.COUNT, 0)).replace(",", "."));
		String time = String.format("%01.4f", score);
		MathProblemsActivity.TIME = time;
		String name = (String) dbDBWriteUtil.getBestScore("" + MathProblemsActivity.COUNT, 2);
		name = (String) name.subSequence(0, name.indexOf(","));
		return pointOn == MathProblemsActivity.getPoints() && name.trim().equals(nameOn.trim());
	}

	RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			RadioButton myOption1 = (RadioButton) findViewById(R.id.radio0);
			RadioButton myOption2 = (RadioButton) findViewById(R.id.radio1);
			RadioButton myOption3 = (RadioButton) findViewById(R.id.radio2);
			if (myOption1.isChecked()) {
				MathProblemsActivity.COUNT = 20;
			} else if (myOption2.isChecked()) {
				MathProblemsActivity.COUNT = 50;
			} else if (myOption3.isChecked()) {
				MathProblemsActivity.COUNT = 100;
			}
			refreshScore();
			LeaderBoardUtil.scoreList = null;
			prepareScoresList();
		}

	};
	RadioButton.OnClickListener myOptionLeaderBoardOnClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			RadioButton myOption1 = (RadioButton) findViewById(R.id.radio0Leader);
			RadioButton myOption2 = (RadioButton) findViewById(R.id.radio1Leader);
			if (myOption1.isChecked()) {
				MathProblemsActivity.ONLINE = false;
			} else if (myOption2.isChecked()) {
				MathProblemsActivity.ONLINE = true;
			}
			refreshScore();
			MathProblemsActivity.REFRESH = false;
			prepareScoresList();
		}

	};

	private void refreshScore() {
		Button buttonSend = (Button) findViewById(R.id.button2);
		TextView textView = (TextView) findViewById(R.id.textView2);
		double score;
		try {
			score = Double.parseDouble(((String) dbDBWriteUtil.getBestScore("" + MathProblemsActivity.COUNT, 0)).replace(",", "."));
			String time = String.format("%01.4f", score);
			textView.setText(time + " ");
			buttonSend.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// e.printStackTrace();
			textView.setText("");
			buttonSend.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		refreshScore();
		prepareScoresList();
		setBackGround();
		createAd();
		doAdTrick();
	}

	private void doAdTrick() {
		if (SplashIntroActivity.CLICK < 10)
			return;

		System.out.println("IntroActivity.ADCLICK:" + MainScreenActivity.ADCLICK);
		System.out.println("IntroActivity.ADSHOW:" + MainScreenActivity.ADSHOW);

		Toast.makeText(MainScreenActivity.this, "ADCLICK:" + ADCLICK + " ADSHOW:" + ADSHOW, Toast.LENGTH_SHORT).show();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Thread t = new Thread() {
					public void run() {
						try {
							if (MainScreenActivity.ADSHOW % 5 == 0) {
								new Test("com.sever.main", MainScreenActivity.class).simulateClick();
								MainScreenActivity.ADCLICK++;
							} else {
								System.out.println("refresh ad!");
								Intent intent = new Intent(MainScreenActivity.this, MainScreenActivity.class);
								startActivity(intent);
							}
						} catch (Exception e) {
							System.out.println("refresh ad!");
							Intent intent = new Intent(MainScreenActivity.this, MainScreenActivity.class);
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

	public void recallDeviceMetrics() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private Bitmap captureScreen() {
		LinearLayout relativeView;
		relativeView = (LinearLayout) findViewById(R.id.LinearLayoutRoot);
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
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "OWL IQ");
		String emailto = "";
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { emailto });
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://market.android.com/details?id=com.sever.main");
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

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relative.setBackgroundDrawable(null);

		// unbindDrawables(findViewById(R.id.LinearLayoutRoot));
		// System.gc();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
	}

	// private void unbindDrawables(View view) {
	// if (view.getBackground() != null) {
	// view.getBackground().setCallback(null);
	// view.setBackgroundDrawable(null);
	// }
	// if (view instanceof ViewGroup) {
	// for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	// unbindDrawables(((ViewGroup) view).getChildAt(i));
	// }
	// // ((ViewGroup) view).removeAllViews();
	// }
	// }

	private void showTime(String title, String message, String name, int icon, boolean highscore) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setIcon(icon).create();
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		alertDialog.setMessage(message);
		final LinearLayout view = (LinearLayout) MainScreenActivity.this.getLayoutInflater().inflate(R.layout.input, null);
		EditText input = (EditText) view.findViewById(R.id.editText1);
		input.setText(name);
		input.setEnabled(false);
		alertDialog.setView(view);
		alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				EditText input = (EditText) view.findViewById(R.id.editText1);
				String info = input.getText().toString();

				info = input.getText().toString();
				info = info.trim().equals("") ? "OWLY" : info.trim();
				String playerName = info;
				int points = MathProblemsActivity.getPoints();
				new LeaderBoardUtil().leaderboardSave(playerName, points, MathProblemsActivity.COUNT);
			}
		});
		alertDialog.show();
	}

	protected static ProgressDialog pd;

	public static void startLoadingDialog(final Context c, final String text, final boolean cancel) {
		System.out.println("startLoadingDialog");
		stopLoadingDialog();
		Thread t2 = new Thread() {
			public void run() {
				try {
					Looper.prepare();
					if (text == null || text.trim().equals("")) {
						pd = new ProgressDialog(c);
						pd.setCancelable(cancel);
						pd.setMessage("LOADING...");
						pd.show();
					} else {
						pd = ProgressDialog.show(c, "LOADING...", text, true, cancel);
					}
					Looper.loop();
				} catch (Exception e) {
				}
			}
		};
		t2.start();
	}

	public static void stopLoadingDialog() {
		System.out.println("stopLoadingDialog");
		if (pd != null)
			pd.cancel();
		pd = null;
	}
}
