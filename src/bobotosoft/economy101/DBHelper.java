package bobotosoft.economy101;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	private static final String DB_NAME = "economy.sqlite";
	private static final int DB_SCHEME_VERSION = 2;
	private static final String TAG = "DBHelper";
	SharedPreferences pref;
	String currency;
	Context c;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_SCHEME_VERSION);
		c = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBManager.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG,"version: "+String.valueOf(newVersion));
		pref = PreferenceManager.getDefaultSharedPreferences(c);
		currency = pref.getString("currency", "EUR");
		
		db.execSQL("ALTER TABLE movement ADD COLUMN date integer DEFAULT "+String.valueOf(System.currentTimeMillis()/1000));
		db.execSQL("ALTER TABLE movement ADD COLUMN currency text DEFAULT "+currency);
		
		//db.execSQL("DROP TABLE movement");
		//db.execSQL("create table movement (_id integer primary key autoincrement,description text not null,amount integer");
	}

}
