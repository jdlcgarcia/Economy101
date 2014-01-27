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
	TextView whynot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		percentage = (TextView)findViewById(R.id.TVperc);
		whynot = (TextView)findViewById(R.id.TVperc_message);
		manager = new DBManager(this);
		
		loadpercentage();
		
			
	}
	@Override
	public void onResume() {
        super.onResume();
		loadpercentage();
    }
	
	private void loadpercentage() {
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
		if (totalinc == 0){			
			whynot.setText(R.string.perc_message_error);
			percentage.setText("");
		}
		else
		{
			whynot.setText(R.string.perc_message);
			percentage.setText(new DecimalFormat("##.##").format((100*totalexp)/totalinc)+"%");
		}
		
	}


}
