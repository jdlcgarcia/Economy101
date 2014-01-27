package com.bobotosoft.economy101;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class IncomeActivity extends ListActivity {

	ListView income;
	DBManager manager;
	private ArrayList<Movement> moves;
    private MovementAdapter mAdapter;
    EditText newDescription;
    EditText newAmount;
    View list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		manager = new DBManager(this);
		
		newDescription = (EditText)findViewById(R.id.ETdescription);
        newAmount = (EditText)findViewById(R.id.ETamount);
        reloadIncome();
        list = getListView();
        registerForContextMenu(list);
        
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
                	Movement m = new Movement(newDescription.getText().toString(),Double.parseDouble(newAmount.getText().toString()));
                	manager.insert(m.getDescription(), m.getAmount());
                	newDescription.setText("");
                	newAmount.setText("");
                	Toast toast = Toast.makeText(getApplicationContext(), R.string.newincomeok, Toast.LENGTH_SHORT);
                	toast.show();
                	reloadIncome();
                	
                }
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(newAmount.getWindowToken(), 0);
                 
            }
            
        });
         
        
        final Button bCancel = (Button) findViewById(R.id.bCancelExpense);
        bCancel.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		newDescription.setText("");
            	newAmount.setText("");
            	newDescription.clearFocus();
            	newAmount.clearFocus();
            	bCancel.requestFocus();
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(newAmount.getWindowToken(), 0);
			}
                	
        });
        
        
	}

	private void reloadIncome() {
		//FILL THE ARRAYLIST<MOVEMENT>
		moves = new ArrayList<Movement>();
		moves = manager.get_income();
		mAdapter = new MovementAdapter(this, moves);
	    setListAdapter(mAdapter);
		
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		
		
	}
	
	
	@Override
	public void onResume() {
        super.onResume();
		reloadIncome();
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.contextmovement, menu);
	    String title =menu.findItem(R.id.iDeleteAll).getTitle() + " " + getString(R.string.title_activity_income); 
	    menu.findItem(R.id.iDeleteAll).setTitle(title);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    Movement m = (Movement) getListAdapter().getItem(info.position);
	    int key = m.getId();
	    switch (item.getItemId()) {
	        case R.id.iDelete:
	        	manager.delete(key);
	            reloadIncome();
	            return true;
	        case R.id.iDeleteAll:
	        	manager.deleteIncome();
	        	reloadIncome();
	            return true;
	        case R.id.iReboot:
	        	manager.reset();
	        	reloadIncome();
	            return true;
	        
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	
	
	
}
