package com.bobotosoft.economy101;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	
	
	private static final String TAG = "MainActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TabHost tabHost = getTabHost();

		// Tab for Expenses
		String titleExpenses = getString(R.string.title_activity_expenses);
		TabSpec expensesSpec = tabHost.newTabSpec(titleExpenses);
		// setting Title and Icon for the Tab
		expensesSpec.setIndicator(titleExpenses);
		Intent expensesIntent = new Intent(this, ExpensesActivity.class);
		expensesSpec.setContent(expensesIntent);

		// Tab for Income
		String titleIncome = getString(R.string.title_activity_income);
		TabSpec incomeSpec = tabHost.newTabSpec(titleIncome);
		// setting Title and Icon for the Tab
		incomeSpec.setIndicator(titleIncome);
		Intent incomeIntent = new Intent(this, IncomeActivity.class);
		incomeSpec.setContent(incomeIntent);

		// Tab for Stats
		String titleStats = getString(R.string.title_activity_stats);
		TabSpec statsSpec = tabHost.newTabSpec(titleStats);
		// setting Title and Icon for the Tab
		statsSpec.setIndicator(titleStats);
		Intent statsIntent = new Intent(this, StatsActivity.class);
		statsSpec.setContent(statsIntent);
		
		// Adding all TabSpec to TabHost
        tabHost.addTab(expensesSpec); // Adding expenses tab
        tabHost.addTab(incomeSpec); // Adding income tab
        tabHost.addTab(statsSpec); // Adding stats tab
        SharedPreferences pref =
	            PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		Log.d(TAG,"currency = "+pref.getString("currency", ""));
		Log.d(TAG,"currentMonth = "+pref.getBoolean("currentMonth", true));
		Log.d(TAG,"intervalStart = "+pref.getString("intervalStart", ""));
		Log.d(TAG,"intervalEnd = "+pref.getString("intervalEnd", ""));
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	        	startActivity(new Intent(this.getApplicationContext(),SettingsActivity.class));
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
