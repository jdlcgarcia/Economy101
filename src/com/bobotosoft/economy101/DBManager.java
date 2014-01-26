package com.bobotosoft.economy101;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	public static final String TABLE_NAME = "movement";
	public static final String CN_ID = "_id";
	public static final String CN_DESCRIPTION = "description";
	public static final String CN_AMOUNT = "amount";
	
	public static final String RESET_TABLE = "delete from "+TABLE_NAME+";";
	public static final String CREATE_TABLE = "create table "+TABLE_NAME+ "("
			+CN_ID+" integer primary key autoincrement,"
			+CN_DESCRIPTION +" text not null,"
			+CN_AMOUNT+" integer);";
	
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context)
	{
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
		
		db.execSQL(DBManager.RESET_TABLE);
		this.insert("Gameboy", -70);
		this.insert("Food", -50);
		this.insert("Dinner with friends",-30);
		this.insert("Salary", 1120);
		this.insert("Christmas", 150);
		
	}
	
	public ContentValues createContentValues(String description, double amount)
	{
		ContentValues cv= new ContentValues();
		cv.put(CN_DESCRIPTION, description);
		cv.put(CN_AMOUNT, amount);
		
		return cv;
	}
	
	public void insert(String description, double amount){
		db.insert(TABLE_NAME, null, createContentValues(description, amount));
	}
	
	public void delete(int id)
	{
		db.delete(TABLE_NAME, CN_ID+"=?", new String[]{Integer.toString(id)});
	}
	
	public void editAmount(String description, double amount){
		db.update(TABLE_NAME, createContentValues(description, amount), CN_AMOUNT+"=?", new String[]{Double.toString(amount)});
	}

	public ArrayList<Movement> get_expenses() {
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE amount < 0", null);
		if (mCursor.moveToFirst()) {
            do {
            	Movement m = new Movement(mCursor.getString(mCursor.getColumnIndex(CN_DESCRIPTION)),
            							  mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT))
            							  );
            									  
            									  
               resultList.add(m);

            } while (mCursor.moveToNext());
        }
		return resultList;
	}

	public ArrayList<Movement> get_income() {
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE amount > 0", null);
		if (mCursor.moveToFirst()) {
            do {
            	Movement m = new Movement(mCursor.getString(mCursor.getColumnIndex(CN_DESCRIPTION)),
            							  mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT))
            							  );
            									  
            									  
               resultList.add(m);

            } while (mCursor.moveToNext());
        }
		return resultList;
	}
}
