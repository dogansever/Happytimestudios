package com.sever.main;

import java.util.logging.Logger;

import org.json.JSONArray;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.geosophic.error.Geosophic_BadRequestException;
import com.geosophic.error.Geosophic_ResponseFormatingErrorException;
import com.geosophic.service.Geosophic_NicknameResponse;
import com.geosophic.service.Geosophic_ServiceController;

public class LeaderBoardUtil {
	private static final Logger log = Logger.getLogger(LeaderBoardUtil.class.getName());
	public static JSONArray scoreList;
	public static String INFO = "INFO";
	public static String SCORE = "SCORE";
	public static int leaderboardSchemaId = getLSId();
	public static int neighbours = 5;
	public static int scoreInMs = 5;
	public static String nickname = "Tap refresh";
	public static String nicknameNew = "nickname";

	public void leaderboardSave(Object... args) {
		log.info("\nLeaderboard Save");
		if (!hasConnection(MainScreenActivity.context))
			return;

		try {
			leaderboardSchemaId = getLSId();
			scoreInMs = ((Long) args[0]).intValue();
			new SaveOperation().execute();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private class GetNameOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			System.out.println("GetNameOperation.doInBackground");
			MainScreenActivity.startLoadingDialog(MainScreenActivity.context, "", false);
			try {
				nickname = Geosophic_ServiceController.getPlayerNickname();
			} catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("GetNameOperation.onPostExecute");
			MainScreenActivity.stopLoadingDialog();
			if (result.equals("Executed")) {
				MainScreenActivity.context.refreshNickname();
				// Toast.makeText(MainScreenActivity.context,
				// "Hey! It is done!", Toast.LENGTH_SHORT).show();
			}
			if (result.equals("Error")) {
				Toast.makeText(MainScreenActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class UpdateNameOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			System.out.println("UpdateNameOperation.doInBackground");
			// MainScreenActivity.startLoadingDialog(MainScreenActivity.context,
			// "", false);
			try {
				Geosophic_NicknameResponse nicknameResponse = Geosophic_ServiceController.updateUserNickname(nicknameNew);
				if (nicknameResponse.isNicknameAvailable()) {
					// Toast.makeText(MainScreenActivity.context,
					// "Hey! Rename is done!", Toast.LENGTH_SHORT).show();
					nickname = nicknameNew;
				} else {
					return nicknameResponse.getSuggestedNickname();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("UpdateNameOperation.onPostExecute");
			// MainScreenActivity.stopLoadingDialog();
			if (result.equals("Executed")) {
				MainScreenActivity.context.refreshNickname();
				// Toast.makeText(MainScreenActivity.context,
				// "Hey! It is done! Relist you may do!",
				// Toast.LENGTH_SHORT).show();
			} else if (result.equals("Error")) {
				Toast.makeText(MainScreenActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainScreenActivity.context, "Hey! This name is not available! Try " + result + "?", Toast.LENGTH_SHORT).show();

			}
		}
	}

	private class SaveOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			System.out.println("SaveOperation.doInBackground");
			MainScreenActivity.startLoadingDialog(MainScreenActivity.context, "", false);
			try {
				if (Geosophic_ServiceController.isServiceActive())
					Geosophic_ServiceController.postScore(leaderboardSchemaId, scoreInMs);
			} catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("SaveOperation.onPostExecute");
			MainScreenActivity.stopLoadingDialog();
			if (result.equals("Executed")) {
				Toast.makeText(MainScreenActivity.context, "Hey! It is done! Relist you may do!", Toast.LENGTH_SHORT).show();
			}
			if (result.equals("Error")) {
				Toast.makeText(MainScreenActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class ListOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			System.out.println("ListOperation.doInBackground");
			try {
				MathProblemsActivity.REFRESHING = true;
				MainScreenActivity.startLoadingDialog(MainScreenActivity.context, "", false);
				leaderboardSchemaId = getLSId();
				scoreList = Geosophic_ServiceController.getPlayerRank(leaderboardSchemaId, neighbours);
			} catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("ListOperation.onPostExecute");
			MathProblemsActivity.REFRESHING = false;
			MainScreenActivity.stopLoadingDialog();
			MathProblemsActivity.REFRESH = false;
			if (result.equals("Executed")) {
				MainScreenActivity.context.prepareScoresList();
			}
			if (result.equals("Error")) {
				Toast.makeText(MainScreenActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void leaderBoardUpdateNickname(String newname) {
		log.info("\nLeaderboard Update Nickname");
		if (!hasConnection(MainScreenActivity.context))
			return;

		nicknameNew = newname;
		new UpdateNameOperation().execute();

	}

	public void leaderBoardRefreshNicknameSync() {
		log.info("\nLeaderboard Refresh Nickname");
		if (!hasConnection(MainScreenActivity.context))
			return;

		nickname = Geosophic_ServiceController.getPlayerNickname();
		if (nickname.equalsIgnoreCase("null")) {
			nickname = "Owly";
			Toast.makeText(MainScreenActivity.context, "Hey, World has to know you! Send Online your best and Rename!", Toast.LENGTH_SHORT).show();
		}
		MainScreenActivity.context.refreshNickname();
	}

	public void leaderBoardRefreshNickname() {
		log.info("\nLeaderboard Refresh Nickname");
		if (!hasConnection(MainScreenActivity.context))
			return;

		new GetNameOperation().execute();

	}

	public void leaderBoardList() {
		log.info("\nLeaderboard List");
		if (!hasConnection(MainScreenActivity.context))
			return;

		new ListOperation().execute();

	}

	public void leaderBoardShow() {
		log.info("\nLeaderboard Show");
		if (!hasConnection(MainScreenActivity.context))
			return;

		leaderboardSchemaId = getLSId();
		try {
			Geosophic_ServiceController.showLeaderboardView(leaderboardSchemaId);
		} catch (Geosophic_BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Geosophic_ResponseFormatingErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static int getLSId() {
		return MathProblemsActivity.COUNT == 20 ? 526 : (MathProblemsActivity.COUNT == 50 ? 527 : 528);
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

}
