package bobotosoft.economy101;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

public class DBManager {
	public static final String TABLE_NAME = "movement";
	public static final String CN_ID = "_id";
	public static final String CN_DESCRIPTION = "description";
	public static final String CN_AMOUNT = "amount";
	public static final String CN_DATE = "date";
	public static final String CN_CURRENCY = "currency";

	//public static final String RESET_TABLE = "delete from " + TABLE_NAME + ";";
	public static final String CREATE_TABLE = "create table " + TABLE_NAME
			+ "(" + CN_ID + " integer primary key autoincrement,"
			+ CN_DESCRIPTION + " text not null," + CN_AMOUNT + " integer,"
			+ CN_DATE + " integer," + CN_CURRENCY + " text);";
	private static final String TAG = "DBManager";

	private DBHelper helper;
	private SQLiteDatabase db;

	private Context context;
	// preferences
	SharedPreferences pref;
	public String currency;
	private boolean currentMonth;
	private long startDate;
	private long endDate;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
		/*
		 * db.execSQL(DBManager.RESET_TABLE); this.insert("Gameboy",
		 * -70,503107200,"EUR"); this.insert("Food", -50,1392904800,"EUR");
		 * this.insert("Dinner with friends",-30,1392933600,"EUR");
		 * this.insert("Salary", 1120,1393545600,"EUR");
		 * this.insert("Christmas", 150,1387929600,"EUR");
		 */
	}

	public ContentValues createContentValues(String description, double amount,
			long l, String currency) {
		ContentValues cv = new ContentValues();
		cv.put(CN_DESCRIPTION, description);
		cv.put(CN_AMOUNT, amount);
		cv.put(CN_DATE, l);
		cv.put(CN_CURRENCY, currency);
		Log.d(TAG,"description: "+description+"; amount: "+String.valueOf(amount)+"; date"+String.valueOf(l)+";currency:"+currency);
		return cv;
	}

	public void insert(String description, double amount, long l,
			String currency) {
		db.insert(TABLE_NAME, null,
				createContentValues(description, amount, l, currency));
	}

	public void delete(int id) {
		db.delete(TABLE_NAME, CN_ID + "=?",
				new String[] { Integer.toString(id) });
	}

	public void editAmount(String description, double amount, int date,
			String currency) {
		db.update(TABLE_NAME,
				createContentValues(description, amount, date, currency),
				CN_AMOUNT + "=?", new String[] { Double.toString(amount) });
	}

	public ArrayList<Movement> get_expenses(Context context) {
		loadPreferences(context);
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ CN_AMOUNT + " < 0 and " + CN_DATE + ">=" + startDate
				+ " AND " + CN_DATE + "<=" + endDate, null);
		if (mCursor.moveToFirst()) {
			do {
				Movement m = new Movement(mCursor.getString(mCursor
						.getColumnIndex(CN_DESCRIPTION)),
						mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT)),
						mCursor.getInt(mCursor.getColumnIndex(CN_DATE)),
						mCursor.getString(mCursor.getColumnIndex(CN_CURRENCY)));
				m.setId(mCursor.getInt(mCursor.getColumnIndex(CN_ID)));

				resultList.add(m);

			} while (mCursor.moveToNext());
		}
		return resultList;
	}

	private void loadPreferences(Context context) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		currency = pref.getString("currency", "");
		currentMonth = pref.getBoolean("currentMonth", true);
		
		if (currentMonth) {
			Calendar rightNow = Calendar.getInstance();
			startDate = firstDay(rightNow);
			endDate = lastDay(rightNow);
		} else {
			SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");

			Date d1 = null, d2 = null;
			try {
				d1 = f.parse(pref.getString("intervalStart", ""));
				d2 = f.parse(pref.getString("intervalEnd", ""));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			startDate = d1.getTime() / 1000;
			endDate = d2.getTime() / 1000;
		}
		
	}

	private long lastDay(Calendar rightNow) {
		rightNow.set(Calendar.DAY_OF_MONTH, rightNow.getInstance()
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		return rightNow.getTimeInMillis()/1000;
	}

	private long firstDay(Calendar rightNow) {
		rightNow.set(Calendar.DAY_OF_MONTH, 1);
		return rightNow.getTimeInMillis()/1000;
	}

	public ArrayList<Movement> get_income(Context context) {
		loadPreferences(context);
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ CN_AMOUNT + " > 0 and " + CN_DATE + ">=" + startDate
				+ " AND " + CN_DATE + "<=" + endDate, null);
		if (mCursor.moveToFirst()) {
			do {
				Movement m = new Movement(mCursor.getString(mCursor
						.getColumnIndex(CN_DESCRIPTION)),
						mCursor.getDouble(mCursor.getColumnIndex(CN_AMOUNT)),
						mCursor.getInt(mCursor.getColumnIndex(CN_DATE)),
						mCursor.getString(mCursor.getColumnIndex(CN_CURRENCY)));
				m.setId(mCursor.getInt(mCursor.getColumnIndex(CN_ID)));

				resultList.add(m);

			} while (mCursor.moveToNext());
		}
		return resultList;
	}

	public void reset() {
		//db.execSQL(DBManager.RESET_TABLE);

	}

	public void deleteExpenses() {
		db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + CN_AMOUNT + " < 0");
	}

	public void deleteIncome() {
		db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + CN_AMOUNT + " > 0");
	}

	public Double get_total_expenses(Context context) {
		loadPreferences(context);
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		double d = 0;
		Cursor mCursor = db.rawQuery("SELECT sum(amount) as total FROM " + TABLE_NAME + " WHERE "
				+ CN_AMOUNT + " < 0 and " + CN_DATE + ">=" + startDate
				+ " AND " + CN_DATE + "<=" + endDate, null);
		if (mCursor.moveToFirst()) {
			do {
				d = mCursor.getDouble(mCursor.getColumnIndex("total"));
			} while (mCursor.moveToNext());
		}
		return d;
	}

	public Double get_total_income(Context context) {
		loadPreferences(context);
		ArrayList<Movement> resultList = new ArrayList<Movement>();
		double d = 0;
		Cursor mCursor = db.rawQuery("SELECT sum(amount) as total FROM " + TABLE_NAME + " WHERE "
				+ CN_AMOUNT + " > 0 and " + CN_DATE + ">=" + startDate
				+ " AND " + CN_DATE + "<=" + endDate, null);
		if (mCursor.moveToFirst()) {
			do {
				d = mCursor.getDouble(mCursor.getColumnIndex("total"));
			} while (mCursor.moveToNext());
		}
		return d;
	}

}
