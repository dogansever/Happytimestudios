package com.sever.physic;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sever.physic.adapters.GlobalRankingAdapter;
import com.sever.physic.adapters.InfoAdapter;
import com.sever.physics.game.IntroView;
import com.sever.physics.game.utils.DBWriteUtil;
import com.sever.physics.game.utils.GeneralUtil;

public class IntroActivity extends Activity {

	private IntroView introView;
	public static Typeface tf;
	public static Bitmap bmpIntro;
	public static Bitmap bmpIntro2;
	public static DBWriteUtil dbDBWriteUtil;

	public static String uniqueID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		uniqueID = tm.getDeviceId();

		tf = Typeface.createFromAsset(getAssets(), "FEASFBRG.TTF");
		dbDBWriteUtil = new DBWriteUtil(this);
		bmpIntro = GeneralUtil.createScaledBitmap(this, R.drawable.space, (int) PhysicsApplication.deviceWidth, (int) PhysicsApplication.deviceHeight);
		bmpIntro2 = GeneralUtil.createScaledBitmap(this, R.drawable.introsub1, (int) PhysicsApplication.deviceWidth, (int) (PhysicsApplication.deviceHeight * 0.125f));

		setContentView(R.layout.intro);
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

		final Button cont = (Button) findViewById(R.id.button1);
		cont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText username = (EditText) findViewById(R.id.editText1);
				if (username.getText().toString().trim().equals("")) {
					Toast.makeText(IntroActivity.this, "Please enter your name...", Toast.LENGTH_SHORT);
				} else {
					String usernameColumn = username.getText().toString().trim();
					dbDBWriteUtil.addScore("" + 0, "" + new Date().getTime(), "" + 1, usernameColumn);
					prepareMain();
				}
			}
		});

	}

	protected void prepareGlobalRanking() {
		RelativeLayout sub = (RelativeLayout) findViewById(R.id.introViewRelativeLayout2);
		RelativeLayout submain = (RelativeLayout) IntroActivity.this.getLayoutInflater().inflate(R.layout.introsubglobalranking, null);
		submain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		sub.removeAllViews();
		sub.addView(submain);
		Button toMain = (Button) findViewById(R.id.Button03);
		toMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prepareMain();
			}

		});

		ListView globalRankinglistView = ((ListView) findViewById(R.id.listView1));
		globalRankinglistView.setBackgroundColor(Color.BLACK);
		globalRankinglistView.setAdapter(new GlobalRankingAdapter(this));
	}

	protected void prepareMain() {
		RelativeLayout sub = (RelativeLayout) findViewById(R.id.introViewRelativeLayout2);
		RelativeLayout submain = (RelativeLayout) IntroActivity.this.getLayoutInflater().inflate(R.layout.introsubmain, null);
		submain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		sub.removeAllViews();
		sub.addView(submain);

		final Button start = (Button) findViewById(R.id.Button01);
		start.setVisibility(View.VISIBLE);
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				introView.finishIntro();
				start.setVisibility(View.GONE);
			}
		});

		final Button global = (Button) findViewById(R.id.Button02);
		global.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prepareGlobalRanking();

			}
		});
		final Button info = (Button) findViewById(R.id.Button03);
		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prepareInfo();

			}
		});

		TextView rank = (TextView) findViewById(R.id.textView1);
		rank.setText("Rank: Stage " + dbDBWriteUtil.getBestScore(1));
		rank.setTypeface(tf);
		TextView score = (TextView) findViewById(R.id.TextView01);
		score.setText("Score: " + dbDBWriteUtil.getBestScore(0));
		score.setTypeface(tf);
		TextView username = (TextView) findViewById(R.id.TextView02);
		username.setText(((String) dbDBWriteUtil.getBestScore(2)).toUpperCase());
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
				prepareMain();
			}

		});

		ListView infolistView = ((ListView) findViewById(R.id.listView1));
		infolistView.setBackgroundColor(Color.BLACK);
		infolistView.setAdapter(new InfoAdapter(this));
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
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
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
	}

}
