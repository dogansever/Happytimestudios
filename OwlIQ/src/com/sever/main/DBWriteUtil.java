package com.sever.main;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sever.main.R;

public class DBWriteUtil extends SQLiteOpenHelper {
	static final String dbName = "demoDB";
	static final String gameTable = "gameTable";
	static final String scoreColumn = "scoreColumn";
	static final String dateColumn = "dateColumn";
	static final String countColumn = "countColumn";
	static final String infoColumn = "infoColumn";
	private static final int BESTSCORECOUNT = 10;

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
			db.execSQL("CREATE TABLE if not exists " + gameTable + " (" + scoreColumn + " String , " + countColumn + " String , " + infoColumn + " String , " + dateColumn + " String)");
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
	public void addScore(String scoreColumn, String dateColumn, String countColumn, String infoColumn) {
		deleteOldScores(countColumn);
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(DBWriteUtil.scoreColumn, scoreColumn);
			cv.put(DBWriteUtil.countColumn, countColumn);
			cv.put(DBWriteUtil.infoColumn, infoColumn);
			cv.put(DBWriteUtil.dateColumn, dateColumn);
			db.insert(gameTable, "", cv);
		} catch (Exception e) {
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

	public Object getBestScore(String countColumnArg, int index) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			cur = db.rawQuery("SELECT * from " + gameTable + " where " + countColumn + " = ? " + " order by " + scoreColumn + " asc", new String[] { countColumnArg });
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
					// db.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
		return false;
	}

	public ArrayList<ContentValues> getBestScores(String countColumnArg) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		ArrayList<ContentValues> list = new ArrayList<ContentValues>();
		try {
			db = this.getReadableDatabase();
			cur = db.rawQuery("SELECT * from " + gameTable + " where " + countColumn + " = ? " + " order by " + scoreColumn + " asc limit " + BESTSCORECOUNT, new String[] { countColumnArg });
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					ContentValues cv = new ContentValues();
					cv.put(DBWriteUtil.scoreColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.scoreColumn)));
					cv.put(DBWriteUtil.infoColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.infoColumn)));
					list.add(cv);
				}
				return list;
			}

			return list;
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
		return list;
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

	public void deleteOldScores(String countColumnArg) {
		ArrayList<ContentValues> scores = getBestScores(countColumnArg);
		String score = "";
		if (scores.size() == BESTSCORECOUNT) {
			score = (String) scores.get(BESTSCORECOUNT - 1).get(DBWriteUtil.scoreColumn);
		} else {
			return;
		}
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(gameTable, "" + countColumn + " = ? and " + scoreColumn + " > ? ", new String[] { countColumnArg, score });
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
