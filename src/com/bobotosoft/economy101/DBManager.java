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
	public static final String CN_DATE = "date";
	public static final String CN_CURRENCY = "currency";
	
	public static final String RESET_TABLE = "delete from "+TABLE_NAME+";";
	public static final String CREATE_TABLE = "create table "+TABLE_NAME+ "("
			+CN_ID+" integer primary key autoincrement,"
			+CN_DESCRIPTION +" text not null,"
			+CN_AMOUNT+" integer," 
			+CN_DATE+" integer," 
			+CN_CURRENCY+" text);";
	public static final String UPGRADE_TABLE = "ALTER TABLE "+TABLE_NAME+" ADD COLUMN "+ CN_DATE+" integer;" +
			"ALTER TABLE "+TABLE_NAME+" ADD COLUMN "+ CN_CURRENCY+" integer;";
	
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context)
	{
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
		
		db.execSQL(DBManager.RESET_TABLE);
		this.insert("Gameboy", -70,503107200,"EUR");
		this.insert("Food", -50,1392904800,"EUR");
		this.insert("Dinner with friends",-30,1392933600,"EUR");
		this.insert("Salary", 1120,1393545600,"EUR");
		this.insert("Christmas", 150,1387929600,"EUR");
		
	}
	
	public ContentValues createContentValues(String description, double amount,int date, String currency)
	{
		ContentValues cv= new ContentValues();
		cv.put(CN_DESCRIPTION, description);
		cv.put(CN_AMOUNT, amount);
		cv.put(CN_DATE, date);
		cv.put(CN_CURRENCY, currency);
		
		return cv;
	}
	
	public void insert(String description, double amount, int date, String currency){
		db.insert(TABLE_NAME, null, createContentValues(description, amount, date, currency));
	}
	
	public void delete(int id)
	{
		db.delete(TABLE_NAME, CN_ID+"=?", new String[]{Integer.toString(id)});
	}
	
	public void editAmount(String description, double amount,int date, String currency){
		db.update(TABLE_NAME, createContentValues(description, amount, date, currency), CN_AMOUNT+"=?", new String[]{Double.toString(amount)});
	}

	public ArrayList<Movement> get_expenses() {
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE amount < 0", null);
		if (mCursor.moveToFirst()) {
            do {
            	Movement m = new Movement(mCursor.getString(mCursor.getColumnIndex(CN_DESCRIPTION)),
            							  mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT)),
            							  mCursor.getInt(mCursor.getColumnIndex(CN_DATE)),
            							  mCursor.getString(mCursor.getColumnIndex(CN_CURRENCY))
            							  );
            	m.setId(mCursor.getInt(mCursor.getColumnIndex(CN_ID)));								  
            									  
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
						  mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT)),
						  mCursor.getInt(mCursor.getColumnIndex(CN_DATE)),
						  mCursor.getString(mCursor.getColumnIndex(CN_CURRENCY))
						  );
            	m.setId(mCursor.getInt(mCursor.getColumnIndex(CN_ID)));
            									  
               resultList.add(m);

            } while (mCursor.moveToNext());
        }
		return resultList;
	}
	
	

	public void reset() {
		db.execSQL(DBManager.RESET_TABLE);
		
	}

	public void deleteExpenses() {
		db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+CN_AMOUNT+" < 0");
	}

	public void deleteIncome() {
		db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+CN_AMOUNT+" > 0");
	}

	public Double get_total_expenses() {
		// TODO Auto-generated method stub
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		double d=0;
		Cursor mCursor = db.rawQuery("SELECT amount FROM " + TABLE_NAME + " WHERE amount < 0", null);
		if (mCursor.moveToFirst()) {
            do {
            	d += mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT));
            } while (mCursor.moveToNext());
        }
		return d;
	}
	
	public Double get_total_income() {
		// TODO Auto-generated method stub
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		double d=0;
		Cursor mCursor = db.rawQuery("SELECT amount FROM " + TABLE_NAME + " WHERE amount > 0", null);
		if (mCursor.moveToFirst()) {
            do {
            	d += mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT));
            } while (mCursor.moveToNext());
        }
		return d;
	}
}
