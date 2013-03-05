package com.sever.physics.game.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.playtomic.android.api.PlaytomicLeaderboards;
import com.playtomic.android.api.PlaytomicRequestListener;
import com.playtomic.android.api.PlaytomicResponse;
import com.playtomic.android.api.PlaytomicScore;
import com.sever.physic.IntroActivity;

public class LeaderBoardUtil {
	private static final Logger log = Logger.getLogger(LeaderBoardUtil.class.getName());
	private static final String HIGHSCORES = "HIGHSCORES";
	public static ArrayList<PlaytomicScore> scoreList;
	public static String INFO = "INFO";
	public static String SCORE = "SCORE";

	public void leaderboardSave(Object... args) {
		try {
			log.info("\nLeaderboard Save");
			if (!hasConnection(IntroActivity.context))
				return;
			PlaytomicLeaderboards leaderboards = new PlaytomicLeaderboards();

			// we need to set a listener
			leaderboards.setRequestListener(new PlaytomicRequestListener<PlaytomicScore>() {
				@Override
				public void onRequestFinished(PlaytomicResponse playtomicResponse) {
					if (playtomicResponse.getSuccess()) {
						// we call a function for successed cases
						requestLeaderBoardSaveFinished();
					} else {
						// we call a function for failed cases
						requestLeaderBoardSaveFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
					}
				}

			});

			String playerName = (String) args[0];
			int points = Integer.parseInt((String) args[1]);
			String unique = (String) args[2];
			String stage = (String) args[3];

			PlaytomicScore score = new PlaytomicScore(unique + "-" + playerName, points);
			LinkedHashMap<String, String> customData = score.getCustomData();
			// customData.put("playerName", playerName);
			customData.put("stage", stage);

			if (exists(score))
				return;
			IntroActivity.startLoadingDialog(IntroActivity.context, "", false);
			leaderboards.save(HIGHSCORES, score, true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean exists(PlaytomicScore score) {
		try {
			for (PlaytomicScore sc : scoreList) {
				if (sc.getName().equals(score.getName()) && score.getPoints() == sc.getPoints()) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void requestLeaderBoardSaveFinished() {
		log.info("\nSave score success");
		IntroActivity.stopLoadingDialog();
		Toast.makeText(IntroActivity.context, "Hey You did it! A New High Score!!!", Toast.LENGTH_SHORT).show();
	}

	private void requestLeaderBoardSaveFailed(int errorCode, String message) {
		log.info("\nLeaderboard save failed to save because of errorcode #" + errorCode + " - Message:" + message);
		IntroActivity.stopLoadingDialog();
		Toast.makeText(IntroActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
	}

	public void leaderBoardList() {
		log.info("\nLeaderboard List");
		if (!hasConnection(IntroActivity.context))
			return;
		IntroActivity.startLoadingDialog(IntroActivity.context, "", false);
		PlaytomicLeaderboards leaderboards = new PlaytomicLeaderboards();

		// we need to set a listener
		leaderboards.setRequestListener(new PlaytomicRequestListener<PlaytomicScore>() {

			@Override
			public void onRequestFinished(PlaytomicResponse playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					// we call a function for successed cases
					requestLeaderBoardListFinished(playtomicResponse.getData());
				} else {
					// we call a function for failed cases
					requestLeaderBoardListFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});

		leaderboards.list(HIGHSCORES, true, "alltime", 1, 20, null);
	}

	private boolean hasConnection(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		boolean res = true;
		if (i == null)
			res = false;
		else if (!i.isConnected())
			res = false;
		else if (!i.isAvailable())
			res = false;
		System.out.println("hasConnection:" + res);
		if (!res)
			Toast.makeText(ctx, "You are not ONLINE!!!", Toast.LENGTH_SHORT).show();
		return res;
	}

	private void requestLeaderBoardListFinished(ArrayList<PlaytomicScore> data) {
		scoreList = data;
		IntroActivity.stopLoadingDialog();
		IntroActivity.context.prepareGlobalRanking();
	}

	private void requestLeaderBoardListFailed(int errorCode, String message) {
		scoreList = new ArrayList<PlaytomicScore>();
		log.info("Leaderboard list failed to list because of errorcode #" + errorCode + " - Message:" + message);
		IntroActivity.stopLoadingDialog();
		Toast.makeText(IntroActivity.context, "Oops! It looks like Server is down right now!!!", Toast.LENGTH_SHORT).show();
	}

}
