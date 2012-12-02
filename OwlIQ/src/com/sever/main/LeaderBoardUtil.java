package com.sever.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.playtomic.android.api.PlaytomicLeaderboards;
import com.playtomic.android.api.PlaytomicRequestListener;
import com.playtomic.android.api.PlaytomicResponse;
import com.playtomic.android.api.PlaytomicScore;

public class LeaderBoardUtil {
	private static final Logger log = Logger.getLogger(LeaderBoardUtil.class.getName());
	private static final String HIGHSCORES = "HIGHSCORES";
	public static ArrayList<PlaytomicScore> scoreList;
	public static String INFO = "INFO";
	public static String SCORE = "SCORE";

	public void leaderboardSave(Object... args) {
		log.info("\nLeaderboard Save");
		if (!hasConnection(MainScreenActivity.context))
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
		int points = (Integer) args[1];
		String table = HIGHSCORES + args[2];

		PlaytomicScore score = new PlaytomicScore(playerName, points);

		MainScreenActivity.startLoadingDialog(MainScreenActivity.context, "", false);
		leaderboards.save(table, score, true, false);
	}

	private void requestLeaderBoardSaveFinished() {
		log.info("\nSave score success");
		MainScreenActivity.stopLoadingDialog();
		Toast.makeText(MainScreenActivity.context, "Hey! It is done! Relist you may do!", Toast.LENGTH_SHORT).show();
	}

	private void requestLeaderBoardSaveFailed(int errorCode, String message) {
		log.info("\nLeaderboard save failed to save because of errorcode #" + errorCode + " - Message:" + message);
		MainScreenActivity.stopLoadingDialog();
		Toast.makeText(MainScreenActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
	}

	public void leaderBoardSaveAndList(Object... args) {
		log.info("\nLeaderboard List");
		if (!hasConnection(MainScreenActivity.context))
			return;
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

		String playerName = (String) args[0];
		int points = (Integer) args[1];
		String table = HIGHSCORES + args[2];

		PlaytomicScore score = new PlaytomicScore(playerName, points);
		leaderboards.saveAndList(table, score, true, false, "alltime", 20, null, true, null);
		MathProblemsActivity.REFRESHING = true;
	}

	public void leaderBoardList(Object... args) {
		log.info("\nLeaderboard List");
		if (!hasConnection(MainScreenActivity.context))
			return;
		MainScreenActivity.startLoadingDialog(MainScreenActivity.context, "", false);
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

		String table = HIGHSCORES + args[0];
		leaderboards.list(table, true, "alltime", 1, 20, null);
		MathProblemsActivity.REFRESHING = true;
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
		MathProblemsActivity.REFRESHING = false;
		// log.info("Leaderboard {");
		// Iterator<PlaytomicScore> itr = data.iterator();
		// while (itr.hasNext()) {
		// PlaytomicScore score = itr.next();
		// log.info("----------------------------------\nScore:\nName=\"" +
		// score.getName() + "\"");
		// log.info("Points=\"" + score.getPoints() + "\"");
		// log.info("Date=\"" + score.getDate() + "\"");
		// log.info("Relative Date=\"" + score.getRelativeDate() + "\"");
		// log.info("Rank=\"" + score.getRank() + "\"");
		// log.info("Custom Data {");
		// for (Map.Entry<String, String> entry :
		// score.getCustomData().entrySet()) {
		// log.info("Var: Name=\"" + entry.getKey() + "\" Value=\"" +
		// entry.getValue() + "\"");
		// }
		// log.info("}");
		// }
		// log.info("}");
		MainScreenActivity.stopLoadingDialog();
		MathProblemsActivity.REFRESH = false;
		MainScreenActivity.context.prepareScoresList();
	}

	private void requestLeaderBoardListFailed(int errorCode, String message) {
		scoreList = new ArrayList<PlaytomicScore>();
		MathProblemsActivity.REFRESHING = false;
		log.info("Leaderboard list failed to list because of errorcode #" + errorCode + " - Message:" + message);
		MainScreenActivity.stopLoadingDialog();

		Toast.makeText(MainScreenActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
	}

}
