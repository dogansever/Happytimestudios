package com.sever.loc.main;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBWriteUtil extends SQLiteOpenHelper {
	static final String dbName = "demoDB";
	static final String locTable = "locTable";
	static final String latColumn = "latColumn";
	static final String longColumn = "longColumn";
	static final String infoColumn = "infoColumn";
	static final String idColumn = "idColumn";

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
			db.execSQL("CREATE TABLE if not exists " + locTable + " (" + idColumn + " String , " + latColumn + " String , " + longColumn
					+ " String , " + infoColumn + " String)");
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
			db.execSQL("DROP TABLE IF EXISTS " + locTable);
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
	public void addLoc(String latColumn, String longColumn, String infoColumn) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(DBWriteUtil.idColumn, getNextID());
			cv.put(DBWriteUtil.latColumn, latColumn);
			cv.put(DBWriteUtil.longColumn, longColumn);
			cv.put(DBWriteUtil.infoColumn, infoColumn);
			db.insert(locTable, "", cv);
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

	public int getNextID() {
		Cursor cur = null;
		SQLiteDatabase db = null;
		ArrayList<ContentValues> list = new ArrayList<ContentValues>();
		int id = 0;
		try {
			db = this.getReadableDatabase();
			cur = db.rawQuery("SELECT * from " + locTable + " order by " + idColumn + " desc", new String[] {});
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					id = Integer.parseInt(cur.getString(cur.getColumnIndex(DBWriteUtil.idColumn)));
					return id + 1;
				}
			}

			return id + 1;
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
		return id + 1;
	}

	public ArrayList<ContentValues> getLocList() {
		Cursor cur = null;
		SQLiteDatabase db = null;
		ArrayList<ContentValues> list = new ArrayList<ContentValues>();
		try {
			db = this.getReadableDatabase();
			cur = db.rawQuery("SELECT * from " + locTable + " order by " + idColumn + " desc", new String[] {});
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					ContentValues cv = new ContentValues();
					cv.put(DBWriteUtil.idColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.idColumn)));
					cv.put(DBWriteUtil.longColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.longColumn)));
					cv.put(DBWriteUtil.latColumn, cur.getString(cur.getColumnIndex(DBWriteUtil.latColumn)));
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

	public void emptyLocTable() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(locTable, "1=1", new String[] {});
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

	public void deleteLoc(String id) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(locTable, "" + idColumn + " = ? ", new String[] { id });
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
