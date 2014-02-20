package com.bobotosoft.economy101;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	private static final String DB_NAME = "economy.sqlite";
	private static final int DB_SCHEME_VERSION = 5;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_SCHEME_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("drop database movement");
		db.execSQL(DBManager.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE movement ADD COLUMN date integer");
		db.execSQL("ALTER TABLE movement ADD COLUMN currency text");
	}

}
