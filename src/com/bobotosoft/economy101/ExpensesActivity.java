package com.bobotosoft.economy101;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ExpensesActivity extends ListActivity implements OnClickListener{

	ListView expenses;
	DBManager manager;
	private ArrayList<Movement> moves;
    private MovementAdapter mAdapter;
    EditText newDescription;
    EditText newAmount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		manager = new DBManager(this);
		
		newDescription = (EditText)findViewById(R.id.ETdescription);
        newAmount = (EditText)findViewById(R.id.ETamount);
        reloadExpenses();
        
        final Button bNew = (Button) findViewById(R.id.bNewExpense);
        bNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
                if (newDescription.getText().toString().matches("")){
                	Toast toast = Toast.makeText(getApplicationContext(), R.string.nodescription, Toast.LENGTH_SHORT);
                	toast.show();
                } 
                else if (newAmount.getText().toString().matches("")){
                	Toast toast = Toast.makeText(getApplicationContext(), R.string.noamount, Toast.LENGTH_SHORT);
                	toast.show();
                }  
                else {
                	Movement m = new Movement(newDescription.getText().toString(),-1.0*Double.parseDouble(newAmount.getText().toString()));
                	manager.insert(m.getDescription(), m.getAmount());
                	newDescription.setText("");
                	newAmount.setText("");
                	Toast toast = Toast.makeText(getApplicationContext(), R.string.newexpenseok, Toast.LENGTH_SHORT);
                	toast.show();
                	reloadExpenses();
                	
                }
                
            }
        });
        
        final Button bCancel = (Button) findViewById(R.id.bCancelExpense);
        bCancel.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		newDescription.setText("");
            	newAmount.setText("");
			}
                	
        });
	}

	private void reloadExpenses() {
		//FILL THE ARRAYLIST<MOVEMENT>
		moves = new ArrayList<Movement>();
		moves = manager.get_expenses();
		mAdapter = new MovementAdapter(this, moves);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.reboot:
	            manager.reset();
	            reloadExpenses();
	        break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void onResume() {
        super.onResume();
		reloadExpenses();
    }
}
