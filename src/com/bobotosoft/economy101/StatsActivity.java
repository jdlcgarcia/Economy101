package com.bobotosoft.economy101;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class StatsActivity extends Activity {
	DBManager manager;
	private ArrayList<Movement> expenses,income;
	TextView percentage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		percentage = (TextView)findViewById(R.id.TVperc);
		manager = new DBManager(this);
		
		
		//FILL THE ARRAYLIST<MOVEMENT>
		expenses = new ArrayList<Movement>();
		income = new ArrayList<Movement>();
		expenses = manager.get_expenses();
		income = manager.get_income();
		
		double totalexp= 0;
		double totalinc= 0;
		for (int i = 0; i < expenses.size(); i++) {
			totalexp += Math.abs(expenses.get(i).getAmount());
		}
		
		for (int i = 0; i < income.size(); i++) {
			totalinc += Math.abs(income.get(i).getAmount());
		}
		
		percentage.setText(new DecimalFormat("##.##").format((100*totalexp)/totalinc)+"%");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats, menu);
		return true;
	}

}
