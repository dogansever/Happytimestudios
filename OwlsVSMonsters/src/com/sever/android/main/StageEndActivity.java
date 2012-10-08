package com.sever.android.main;

import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StageEndActivity extends Activity {

	private static MediaPlayer mp1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (GameView.success) {
			startLevelCompletedSound();
			MenuActivity.refreshLevelScore();
			setContentView(R.layout.stage_end);
			int scoreStarColumn = getStar();
			StartActivity.dbDBWriteUtil.addOrUpdateScore("" + MenuActivity.score, "" + scoreStarColumn, "" + MenuActivity.stage, ""
					+ MenuActivity.level, "" + new Date().getTime());

			TextView textView1 = (TextView) findViewById(R.id.textView1);
			textView1.setTypeface(StartActivity.context.face);
			textView1.setText("Score : " + MenuActivity.score);

			textView1 = (TextView) findViewById(R.id.TextView01);
			textView1.setText("Highscore : " + (MenuActivity.score > MenuActivity.highscore ? MenuActivity.score : MenuActivity.highscore));
			textView1.setTypeface(StartActivity.context.face);

			textView1 = (TextView) findViewById(R.id.TextView02);
			textView1.setTypeface(StartActivity.context.face);

			textView1 = (TextView) findViewById(R.id.TextView03);
			textView1.setTypeface(StartActivity.context.face);
			if (MenuActivity.score < MenuActivity.highscore) {
				textView1.setVisibility(View.INVISIBLE);
			} else {
				MenuActivity.star = scoreStarColumn;
			}
			textView1 = (TextView) findViewById(R.id.TextView04);
			textView1.setTypeface(StartActivity.context.face);
			textView1.setText("Level " + MenuActivity.level);
			textView1.setTextColor(Color.RED);

			ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
			if (scoreStarColumn == 0) {
				imageView1.setImageResource(R.drawable.menuback_stars0);
			} else if (scoreStarColumn == 1) {
				imageView1.setImageResource(R.drawable.menuback_stars1);
			} else if (scoreStarColumn == 2) {
				imageView1.setImageResource(R.drawable.menuback_stars2);
			} else if (scoreStarColumn == 3) {
				imageView1.setImageResource(R.drawable.menuback_stars3);
			}
			ImageView ImageView01 = (ImageView) findViewById(R.id.ImageView01);
			if (MenuActivity.star == 0) {
				ImageView01.setImageResource(R.drawable.menuback_stars0);
			} else if (MenuActivity.star == 1) {
				ImageView01.setImageResource(R.drawable.menuback_stars1);
			} else if (MenuActivity.star == 2) {
				ImageView01.setImageResource(R.drawable.menuback_stars2);
			} else if (MenuActivity.star == 3) {
				ImageView01.setImageResource(R.drawable.menuback_stars3);
			}
			StartActivity.dbDBWriteUtil.addOrUpdateScore("0", "0", "" + MenuActivity.stage, "" + (MenuActivity.level + 1), "" + new Date().getTime());
		} else {
			((StartActivity) StartActivity.context).playLevelFailedSound();
			setContentView(R.layout.stage_end2);
			TextView textView1 = (TextView) findViewById(R.id.TextView01);
			textView1.setTypeface(StartActivity.context.face);
			textView1 = (TextView) findViewById(R.id.TextView02);
			textView1.setTypeface(StartActivity.context.face);
			textView1.setText("Level " + MenuActivity.level);
			textView1.setTextColor(Color.RED);
		}
	}

	private int getStar() {
		if (MenuActivity.score > GameView.waveCount * 25) {// 1000
			return 3;
		}
		if (MenuActivity.score > GameView.waveCount * 16) {// 800
			return 2;
		}
		if (MenuActivity.score > GameView.waveCount * 12) {// 700
			return 1;
		}
		return 0;
	}

	public void doPlayClick(View view) {
		if (GameView.success) {
			MenuActivity.level = ++MenuActivity.level % 10;
		}
		MenuActivity.refreshLevelScore();
		startGameActivity();
	}

	private void startGameActivity() {
		stopLevelCompletedSound();
		Intent intent = new Intent(StageEndActivity.this, GameGameActivity.class);
		startActivity(intent);
		finish();
	}

	public void doRestartClick(View view) {
		ContentValues cv = StartActivity.dbDBWriteUtil.getScore("" + MenuActivity.stage, "" + MenuActivity.level);
		if (cv == null) {
			return;
		} else {
			MenuActivity.highscore = cv.getAsInteger(DBWriteUtil.scoreColumn);
		}
		startGameActivity();
	}

	public void doListClick(View view) {
		stopLevelCompletedSound();
		Intent intent = new Intent(StageEndActivity.this, MenuActivity.class);
		startActivity(intent);
		finish();
	}

	public static void startLevelCompletedSound() {
		if (MenuActivity.soundOn) {
			if (mp1 == null) {
				mp1 = MediaPlayer.create(StartActivity.context, R.raw.sunny_day);
				mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			}
			if (!mp1.isPlaying())
				mp1.start();
		}
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
		stopLevelCompletedSound();
	}

	public static void stopLevelCompletedSound() {
		try {
			mp1.pause();
		} catch (Exception e) {
		}
	}
}
