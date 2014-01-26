package com.bobotosoft.economy101;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class ExpensesActivity extends ListActivity {

	ListView expenses;
	DBManager manager;
	private ArrayList<Movement> moves;
    private MovementAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		manager = new DBManager(this);
		
		
		//FILL THE ARRAYLIST<MOVEMENT>
		moves = new ArrayList<Movement>();
		moves = manager.get_expenses();
		
		this.mAdapter = new MovementAdapter(this, moves);
        setListAdapter(mAdapter);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expenses, menu);
		return true;
	}
	
	
}
