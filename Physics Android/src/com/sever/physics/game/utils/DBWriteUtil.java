package com.sever.physics.game.utils;

import com.sever.physic.IntroActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBWriteUtil extends SQLiteOpenHelper {
	static final String dbName = "demoDB";
	static final String gameTable = "gameTable";
	static final String scoreColumn = "scoreColumn";
	static final String stageColumn = "countColumn";
	static final String usernameColumn = "infoColumn";
	static final String dateColumn = "dateColumn";

	public DBWriteUtil(Context context) {
		super(context, dbName, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// System.out.println("onCreate:" + this);
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// System.out.println("onUpgrade:" + this);
		// dropTables(db);
		// onCreate(db);
	}

	public void createTables(SQLiteDatabase db) {
		try {
			if (db == null)
				db = this.getWritableDatabase();
			db.execSQL("CREATE TABLE if not exists " + gameTable + " (" + scoreColumn + " String , " + stageColumn + " String , " + usernameColumn + " String , " + dateColumn + " String)");
		} catch (SQLException e) {
		} finally {
			try {
				if (db != null && !db.isOpen()) {
					db.close();
				}
			} catch (Exception e) {
			}

		}
	}

	public void dropTables(SQLiteDatabase db) {
		try {
			if (db == null)
				db = this.getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + gameTable);
		} catch (SQLException e) {
		} finally {
			try {
				if (db != null && !db.isOpen()) {
					db.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
	}

	// ***********************************************************************/
	public void addScore(String scoreColumn, String dateColumn, String stageColumn, String usernameColumn) {
		String username = (String) getBestScore(2);
		String score = (String) getBestScore(0);
		if (username.equals("")) {
			// insert first time
			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();
				ContentValues cv = new ContentValues();
				cv.put(DBWriteUtil.scoreColumn, scoreColumn);
				cv.put(DBWriteUtil.stageColumn, stageColumn);
				cv.put(DBWriteUtil.usernameColumn, usernameColumn);
				cv.put(DBWriteUtil.dateColumn, dateColumn);
				db.insert(gameTable, "", cv);
			} catch (Exception e) {
			} finally {
				try {
					if (db != null && !db.isOpen()) {
						db.close();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

			}
		} else if (Integer.parseInt(score) < Integer.parseInt(scoreColumn)) {
			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();
				ContentValues cv = new ContentValues();
				cv.put(DBWriteUtil.scoreColumn, scoreColumn);
				cv.put(DBWriteUtil.stageColumn, stageColumn);
				cv.put(DBWriteUtil.usernameColumn, usernameColumn);
				cv.put(DBWriteUtil.dateColumn, dateColumn);
				db.update(gameTable, cv, DBWriteUtil.usernameColumn + " = ? ", new String[] { usernameColumn });
			} catch (Exception e) {
			} finally {
				try {
					if (db != null && !db.isOpen()) {
						db.close();
					}
				} catch (Exception e) {
				}

			}
		} else {
			System.out.println("Something wrong with addScore method!!!");
		}

	}

	public void updateScoreIfNewBestAchieved(String scoreColumn, String dateColumn, String stageColumn) {
		String username = (String) getBestScore(2);
		String score = (String) getBestScore(0);
		if (Integer.parseInt(score) < Integer.parseInt(scoreColumn)) {
			new LeaderBoardUtil().leaderboardSave(username, scoreColumn, IntroActivity.uniqueDeviceId, stageColumn);
			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();
				ContentValues cv = new ContentValues();
				cv.put(DBWriteUtil.scoreColumn, scoreColumn);
				cv.put(DBWriteUtil.stageColumn, stageColumn);
				cv.put(DBWriteUtil.dateColumn, dateColumn);
				db.update(gameTable, cv, DBWriteUtil.usernameColumn + " = ? ", new String[] { username });
			} catch (Exception e) {
			} finally {
				try {
					if (db != null && !db.isOpen()) {
						// db.close();
					}
				} catch (Exception e) {
				}

			}
		} else {
			System.out.println("Something wrong with addScore method!!!");
		}

	}

	public Object getBestScore(int index) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			cur = db.rawQuery("SELECT * from " + gameTable + " order by " + scoreColumn + " desc", new String[] {});
			if (cur.getCount() > 0) {
				cur.moveToFirst();
				return cur.getString(index);
			}

			return "";
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {
				if (cur != null && !cur.isClosed()) {
					cur.close();
				}
				if (db != null && !db.isOpen()) {
					db.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
		return false;
	}

	public void emptyScores() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(gameTable, "1=1", new String[] {});
			// db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			try {
				if (db != null && !db.isOpen()) {
					// db.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
	}

}
