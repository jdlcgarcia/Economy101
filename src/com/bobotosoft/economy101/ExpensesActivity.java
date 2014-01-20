package com.bobotosoft.economy101;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExpensesActivity extends ListActivity {

	ListView expenses;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expenses);
		 initView();  
				/*
		expenses = (ListView) findViewById(R.id.LVexpenses);
		
		ArrayList<String> example_data = new ArrayList<String>();
		example_data.add("Food");
		example_data.add("Water bill");
		example_data.add("Power bill");
		example_data.add("Phone bill");
		example_data.add("Gameboy");
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, 
                android.R.layout.simple_list_item_1,
                example_data );

        expenses.setAdapter(arrayAdapter);
*/
	}

	private void initView() {
        List<Movement> movements = populate();
        MovementAdapter adapter = new MovementAdapter(this, movements);
        setListAdapter(adapter);       
    }

	
	private List<Movement> populate(){       
        String[] apps = new String[] {               
            "Food,121.20,EUR",
            "Water bill,-23.3,EUR",
            "Power bill,-85.20,EUR",
            "Phone bill,-12.66,EUR",
            "Gameboy,-60,GBP"
        };
         
        List<Movement> list = new ArrayList<Movement>();
         
        for(String app:apps) {
            String[] rApp = app.split(",");
            Movement m = new Movement();
            m.setDescription(rApp[0]);
            m.setAmount(Double.parseDouble(rApp[1]));
            m.setCurrency(rApp[2]);
            list.add(m);
        }
        
         
        return list;
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expenses, menu);
		return true;
	}
}
