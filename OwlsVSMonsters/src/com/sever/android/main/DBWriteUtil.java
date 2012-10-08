package com.sever.android.main;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBWriteUtil extends SQLiteOpenHelper {
	static final String dbName = "demoDB";
	static final String gameTable = "gameTable";
	static final String stageColumn = "stageColumn";
	static final String levelColumn = "levelColumn";
	static final String scoreColumn = "scoreColumn";
	static final String scoreStarColumn = "scoreStarColumn";
	static final String dateColumn = "dateColumn";

	public DBWriteUtil(Context context) {
		super(context, dbName, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("onCreate:" + this);
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
			db.execSQL("CREATE TABLE if not exists " + gameTable + " (" + scoreColumn + " String , " + scoreStarColumn + " String , " + stageColumn
					+ " String , " + levelColumn + " String , " + dateColumn + " String)");
		} catch (SQLException e) {
		} finally {
			try {
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
					// db.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
	}

	// ***********************************************************************/
	public void addOrUpdateScore(String scoreColumn, String scoreStarColumn, String stageColumn, String levelColumn, String dateColumn) {
		System.out.println("addOrUpdateScore:");
		SQLiteDatabase db = null;
		try {
			ContentValues score = getScore(stageColumn, levelColumn);
			db = this.getWritableDatabase();
			if (score == null) {
				ContentValues cv = new ContentValues();
				cv.put(DBWriteUtil.scoreColumn, scoreColumn);
				cv.put(DBWriteUtil.scoreStarColumn, scoreStarColumn);
				cv.put(DBWriteUtil.stageColumn, stageColumn);
				cv.put(DBWriteUtil.levelColumn, levelColumn);
				cv.put(DBWriteUtil.dateColumn, dateColumn);
				db.insert(gameTable, "", cv);
			} else {
				if (score.getAsInteger(DBWriteUtil.scoreColumn) < Integer.parseInt(scoreColumn)) {
					ContentValues args = new ContentValues();
					args.put(DBWriteUtil.scoreColumn, scoreColumn);
					args.put(DBWriteUtil.scoreStarColumn, scoreStarColumn);
					db.update(gameTable, args, DBWriteUtil.stageColumn + "=" + stageColumn + " and " + DBWriteUtil.levelColumn + "=" + levelColumn,
							null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null && !db.isOpen()) {
					// db.close();
				}
			} catch (Exception e) {
			}

		}
	}

	public ContentValues getScore(String stageColumn, String levelColumn) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			cur = db.rawQuery("SELECT * from " + gameTable + " where " + DBWriteUtil.stageColumn + " = ? and " + DBWriteUtil.levelColumn + " = ? ", new String[] {
					stageColumn, levelColumn });
			if (cur.getCount() > 0) {
				cur.moveToFirst();
				ContentValues cv = new ContentValues();
				cv.put(DBWriteUtil.scoreColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.scoreColumn)));
				cv.put(DBWriteUtil.scoreStarColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.scoreStarColumn)));
				cv.put(DBWriteUtil.stageColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.stageColumn)));
				cv.put(DBWriteUtil.levelColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.levelColumn)));
				cv.put(DBWriteUtil.dateColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.dateColumn)));
				return cv;
			}

			return null;
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {
				if (cur != null && !cur.isClosed()) {
					cur.close();
				}
				if (db != null && !db.isOpen()) {
					// db.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
		return null;
	}

}
