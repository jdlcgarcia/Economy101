package com.bobotosoft.economy101;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ListView;

public class ExpensesActivity extends ListActivity {

	ListView expenses;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expenses);
		
		DBManager manager = new DBManager(this);
		//manager.insertar("Juan", "645406763");
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expenses, menu);
		return true;
	}
}
