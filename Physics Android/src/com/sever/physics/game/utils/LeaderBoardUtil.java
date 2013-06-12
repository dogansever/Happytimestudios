package com.sever.physics.game.utils;

import java.util.logging.Logger;

import org.json.JSONArray;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.geosophic.service.Geosophic_NicknameResponse;
import com.geosophic.service.Geosophic_ServiceController;
import com.sever.physic.IntroActivity;

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
	public static boolean saveAndShow = false;

	public void leaderboardSave(Object... args) {
		LogUtil.log("\nLeaderboard Save");
		if (!hasConnection())
			return;

		try {
			leaderboardSchemaId = getLSId();
			scoreInMs = Integer.parseInt((String) args[0]);
			saveAndShow = (Boolean) args[1];
			new SaveOperation().execute();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private class GetNameOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			LogUtil.log("GetNameOperation.doInBackground");
			IntroActivity.startLoadingDialog(IntroActivity.context, "", false);
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
			LogUtil.log("GetNameOperation.onPostExecute");
			IntroActivity.stopLoadingDialog();
			if (result.equals("Executed")) {
				// Toast.makeText(IntroActivity.context,
				// "Hey! It is done!", Toast.LENGTH_SHORT).show();
			}
			if (result.equals("Error")) {
				Toast.makeText(IntroActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class UpdateNameOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			LogUtil.log("UpdateNameOperation.doInBackground");
			// IntroActivity.startLoadingDialog(IntroActivity.context,
			// "", false);
			try {
				Geosophic_NicknameResponse nicknameResponse = Geosophic_ServiceController.updateUserNickname(nicknameNew);
				if (nicknameResponse.isNicknameAvailable()) {
					// Toast.makeText(IntroActivity.context,
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
			LogUtil.log("UpdateNameOperation.onPostExecute");
			// IntroActivity.stopLoadingDialog();
			if (result.equals("Executed")) {
				Toast.makeText(IntroActivity.context, "Hey! Rename is done!", Toast.LENGTH_SHORT).show();
			} else if (result.equals("Error")) {
				Toast.makeText(IntroActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(IntroActivity.context, "Hey! This name is not available! Try " + result + "?", Toast.LENGTH_LONG).show();

			}
		}
	}

	private class SaveOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			LogUtil.log("SaveOperation.doInBackground");
			IntroActivity.startLoadingDialog(IntroActivity.context, "", false);
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
			LogUtil.log("SaveOperation.onPostExecute");
			IntroActivity.stopLoadingDialog();
			if (result.equals("Executed")) {
				// Toast.makeText(IntroActivity.context,
				// "Hey! It is done! Relist you may do!",
				// Toast.LENGTH_SHORT).show();
				if (saveAndShow)
					leaderBoardShow();
			}
			if (result.equals("Error")) {
				Toast.makeText(IntroActivity.context, "Oops! It looks like Server is down right now.!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void leaderBoardUpdateNickname(String newname) {
		LogUtil.log("\nLeaderboard Update Nickname");
		if (!hasConnection())
			return;

		nicknameNew = newname;
		new UpdateNameOperation().execute();

	}

	public void leaderBoardRefreshNicknameSync() {
		LogUtil.log("\nLeaderboard Refresh Nickname");
		if (!hasConnection())
			return;

		nickname = Geosophic_ServiceController.getPlayerNickname();
		if (nickname.equalsIgnoreCase("null")) {
			nickname = "Owly";
			Toast.makeText(IntroActivity.context, "Hey, World has to know you! Send Online your best and Rename!", Toast.LENGTH_SHORT).show();
		}
	}

	public String getLeaderBoardNickname() {
		LogUtil.log("\ngetLeaderBoardNickname()");
		if (!hasConnection())
			return "";

		nickname = Geosophic_ServiceController.getPlayerNickname();
		if (nickname.equalsIgnoreCase("null")) {
			nickname = "Owly";
			Toast.makeText(IntroActivity.context, "Hey, World has to know you! Send Online your best and Rename!", Toast.LENGTH_SHORT).show();
		}
		return nickname;
	}

	public void leaderBoardRefreshNickname() {
		LogUtil.log("\nLeaderboard Refresh Nickname");
		if (!hasConnection())
			return;

		new GetNameOperation().execute();

	}

	public void leaderBoardShow() {
		LogUtil.log("\nLeaderboard Show");
		if (!hasConnection())
			return;

		leaderboardSchemaId = getLSId();
		try {
			Geosophic_ServiceController.showLeaderboardView(leaderboardSchemaId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static int getLSId() {
		return 547;
	}

	public boolean hasConnection() {
		return hasConnection(IntroActivity.context);
	}

	public boolean hasConnection(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		boolean res = true;
		if (i == null)
			res = false;
		else if (!i.isConnected())
			res = false;
		else if (!i.isAvailable())
			res = false;
		// LogUtil.log("hasConnection:" + res);
		if (!res)
			Toast.makeText(ctx, "You are not ONLINE!!!", Toast.LENGTH_SHORT).show();
		return res;
	}

}
