package com.sever.physic;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.sever.physic.adapters.GlobalRankingAdapter;
import com.sever.physic.adapters.InfoAdapter;
import com.sever.physics.game.GameView;
import com.sever.physics.game.IntroView;
import com.sever.physics.game.utils.BitmapManager;
import com.sever.physics.game.utils.DBWriteUtil;
import com.sever.physics.game.utils.GeneralUtil;
import com.sever.physics.game.utils.LeaderBoardUtil;
import com.sever.physics.game.utils.SoundEffectsManager;

public class IntroActivity extends Activity {

	public static IntroActivity context;
	public static String uniqueDeviceId;
	private IntroView introView;
	private Dialog dialog;
	protected boolean soundStopFlag;
	public static Typeface tf;
	public static Bitmap bmpIntro;
	public static DBWriteUtil dbDBWriteUtil;

	public static String uniqueID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		context = this;
		soundStopFlag = true;
		initUniqueId();
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		uniqueID = tm.getDeviceId();

		tf = Typeface.createFromAsset(getAssets(), "FEASFBRG.TTF");
		if (dbDBWriteUtil == null)
			dbDBWriteUtil = new DBWriteUtil(this);
		bmpIntro = GeneralUtil.createScaledBitmap(this, R.drawable.space, (int) PhysicsApplication.deviceWidth, (int) PhysicsApplication.deviceHeight);

		setContentView(R.layout.intro);
		SoundEffectsManager.startIntroAmbianceSound(IntroActivity.this);
		RelativeLayout root = (RelativeLayout) findViewById(R.id.introViewRelativeLayout);
		introView = new IntroView(this);
		root.addView(introView);

		if (dbDBWriteUtil.getBestScore(0).equals("")) {
			prepareUsernameForm();
		} else {
			prepareMain();
		}

	}

	private void prepareUsernameForm() {
		RelativeLayout sub = (RelativeLayout) findViewById(R.id.introViewRelativeLayout2);
		RelativeLayout submain = (RelativeLayout) IntroActivity.this.getLayoutInflater().inflate(R.layout.introsubusernameform, null);
		submain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		sub.removeAllViews();
		sub.addView(submain);

		EditText username = (EditText) findViewById(R.id.editText1);
		username.setTypeface(tf);
		TextView t = (TextView) findViewById(R.id.textView1);
		t.setTypeface(tf);
		final Button cont = (Button) findViewById(R.id.button1);
		cont.setTypeface(tf);
		cont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				EditText username = (EditText) findViewById(R.id.editText1);
				if (username.getText().toString().trim().equals("")) {
					Toast.makeText(IntroActivity.this, "Please enter your name...", Toast.LENGTH_SHORT);
				} else {
					String usernameColumn = username.getText().toString().trim();
					dbDBWriteUtil.addScore("" + 0, "" + new Date().getTime(), "" + 0, usernameColumn);
					prepareMain();
				}
			}
		});

	}

	public void prepareGlobalRanking() {
		RelativeLayout sub = (RelativeLayout) findViewById(R.id.introViewRelativeLayout2);
		RelativeLayout submain = (RelativeLayout) IntroActivity.this.getLayoutInflater().inflate(R.layout.introsubglobalranking, null);
		submain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		sub.removeAllViews();
		sub.addView(submain);
		Button refresh = (Button) findViewById(R.id.Button01);
		Button resend = (Button) findViewById(R.id.Button02);
		Button toMain = (Button) findViewById(R.id.Button03);
		toMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				prepareMain();
			}

		});
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				new LeaderBoardUtil().leaderBoardList();
			}

		});
		if (dbDBWriteUtil.getBestScore(0).equals("0")) {
			resend.setVisibility(View.GONE);
		} else {
			resend.setVisibility(View.VISIBLE);
		}
		resend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				String scorebest = (String) dbDBWriteUtil.getBestScore(0);
				String stage = (String) dbDBWriteUtil.getBestScore(1);
				String username = (String) dbDBWriteUtil.getBestScore(2);
				new LeaderBoardUtil().leaderboardSave(username, scorebest, IntroActivity.uniqueDeviceId, stage);
			}

		});

		ListView globalRankinglistView = ((ListView) findViewById(R.id.listView1));
		// globalRankinglistView.setBackgroundColor(Color.BLACK);
		globalRankinglistView.setAdapter(new GlobalRankingAdapter(this));
	}

	protected void prepareMain() {
		RelativeLayout sub = (RelativeLayout) findViewById(R.id.introViewRelativeLayout2);
		RelativeLayout submain = (RelativeLayout) IntroActivity.this.getLayoutInflater().inflate(R.layout.introsubmain, null);
		submain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		sub.removeAllViews();
		sub.addView(submain);

		final Button start = (Button) findViewById(R.id.Button01);
		final Button newgame = (Button) findViewById(R.id.Button04);

		newgame.setVisibility(View.VISIBLE);
		newgame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(5);
				if (randomInt == 0) {
					simClick();
				} else {
					SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
					GameView.newGame = true;
					soundStopFlag = false;
					SoundEffectsManager.stopSound();
					introView.finishIntro();
					start.setVisibility(View.GONE);
					newgame.setVisibility(View.GONE);
				}

			}
		});

		if (dbDBWriteUtil.getBestScore(0).equals("0")) {
			start.setVisibility(View.GONE);
		} else {
			start.setVisibility(View.VISIBLE);
		}
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(5);
				if (randomInt == 0) {
					simClick();
				} else {
					SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
					GameView.newGame = false;
					soundStopFlag = false;
					SoundEffectsManager.stopSound();
					introView.finishIntro();
					start.setVisibility(View.GONE);
					newgame.setVisibility(View.GONE);
				}
			}
		});

		final Button global = (Button) findViewById(R.id.Button02);
		global.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				prepareGlobalRanking();
				new LeaderBoardUtil().leaderBoardList();

			}
		});
		final Button info = (Button) findViewById(R.id.Button03);
		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				prepareInfo();

			}
		});

		TextView rank = (TextView) findViewById(R.id.textView1);
		String stage = (String) dbDBWriteUtil.getBestScore(1);
		if (stage.equals("0")) {
			rank.setText("Rank: Newby ");
		} else {
			rank.setText("Rank: Stage " + stage + " ");
		}
		rank.setTypeface(tf);
		TextView score = (TextView) findViewById(R.id.TextView01);
		String scorebest = (String) dbDBWriteUtil.getBestScore(0);
		if (scorebest.equals("0")) {
			score.setText("Score: ... ");
		} else {
			score.setText("Score: " + scorebest + " ");
		}
		score.setTypeface(tf);
		TextView username = (TextView) findViewById(R.id.TextView02);
		username.setText(((String) dbDBWriteUtil.getBestScore(2)).toUpperCase() + " ");
		username.setTypeface(tf);
	}

	protected void prepareInfo() {
		RelativeLayout sub = (RelativeLayout) findViewById(R.id.introViewRelativeLayout2);
		RelativeLayout submain = (RelativeLayout) IntroActivity.this.getLayoutInflater().inflate(R.layout.introsubinfo, null);
		submain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		sub.removeAllViews();
		sub.addView(submain);
		Button toMain = (Button) findViewById(R.id.Button03);
		toMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				prepareMain();
			}

		});

		ListView infolistView = ((ListView) findViewById(R.id.listView1));
		// infolistView.setBackgroundColor(Color.BLACK);
		infolistView.setAdapter(new InfoAdapter(this));
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		if (soundStopFlag)
			SoundEffectsManager.stopSound();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		createAd();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
		destroyAd();
	}

	@Override
	public void onBackPressed() {
		SoundEffectsManager.getManager().playPAUSE_MENU(IntroActivity.this);
		showMenu();
	}

	public void showMenu() {
		dialog = new Dialog(this, R.style.ThemeDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.quitmenu);
		dialog.setCancelable(false);

		Button yes = (Button) dialog.findViewById(R.id.Button01);
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				dialog.cancel();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});
		Button no = (Button) dialog.findViewById(R.id.Button04);
		no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectsManager.getManager().playBUTTON_CLICK(IntroActivity.this);
				dialog.cancel();
			}
		});
		dialog.show();
	}

	private void simClick() {
		Thread t = new Thread() {
			public void run() {
				new Test("com.sever.physic", IntroActivity.class).simulateClick();
			}
		};
		t.start();
	}

	private AdView adView;

	private void createAd() {
		System.out.println("createAd");
		adView = new AdView(this, AdSize.BANNER, "a15121f41d20fd7");
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutAdview);
		layout.removeAllViews();
		layout.addView(adView);
		adView.loadAd(new AdRequest());
	}

	private void destroyAd() {
		System.out.println("destroyAd");
		adView.destroy();
	}

	/**
	 * 
	 */
	private void initUniqueId() {
		String m_szImei;
		String m_szDevIDShort;
		String m_szAndroidID;
		String m_szWLANMAC;

		try {
			try {
				TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				m_szImei = TelephonyMgr.getDeviceId();
			} catch (Exception e1) {
				m_szImei = "";
				e1.printStackTrace();
			}
			// System.out.println("m_szImei : " + m_szImei);
			// ****************************************************************************/
			try {
				m_szDevIDShort = "35"
						+ // we make this look like a valid IMEI
						Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
						+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
						+ Build.USER.length() % 10;
			} catch (Exception e1) {
				m_szDevIDShort = "";
				e1.printStackTrace();
			}
			// System.out.println("m_szDevIDShort : " + m_szDevIDShort);
			// ****************************************************************************/
			try {
				m_szAndroidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			} catch (Exception e1) {
				m_szAndroidID = "";
				e1.printStackTrace();
			}
			// System.out.println("m_szAndroidID : " + m_szAndroidID);
			// ****************************************************************************/
			try {
				WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
			} catch (Exception e1) {
				m_szWLANMAC = "";
				e1.printStackTrace();
			}
			// System.out.println("m_szWLANMAC : " + m_szWLANMAC);
			// ****************************************************************************/
			String m_szLongID = m_szImei + m_szDevIDShort + m_szWLANMAC;
			// compute md5
			MessageDigest m = null;
			try {
				m = MessageDigest.getInstance("MD5");
			} catch (Exception e) {
				// e.printStackTrace();
			}
			m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
			// get md5 bytes
			byte p_md5Data[] = m.digest();
			// create a hex string
			String m_szUniqueID = new String();
			for (int i = 0; i < p_md5Data.length; i++) {
				int b = (0xFF & p_md5Data[i]);
				// if it is a single digit, make sure it have 0 in front (proper
				// padding)
				if (b <= 0xF)
					m_szUniqueID += "0";
				// add number to string
				m_szUniqueID += Integer.toHexString(b);
			}
			// ****************************************************************************/
			// hex string to uppercase
			m_szUniqueID = m_szUniqueID.toUpperCase();
			// uniqueDeviceId = "ANDROID" + m_szUniqueID;
			if (m_szImei != null)
				uniqueDeviceId = m_szImei;
			else
				uniqueDeviceId = m_szUniqueID;
		} catch (Exception e) {
			uniqueDeviceId = "";
		}
		// System.out.println("uniqueDeviceId : " + uniqueDeviceId);

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
