package bobotosoft.economy101;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpensesActivity extends ListActivity implements OnClickListener{

	private static final String TAG = "Expenses";
	ListView expenses;
	DBManager manager;
	private ArrayList<Movement> moves;
    private MovementAdapter mAdapter;
    EditText newDescription;
    EditText newAmount;
    TextView total;
    
    View list;
    SharedPreferences pref;
	public String currency;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		currency = pref.getString("currency", "EUR");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		manager = new DBManager(this);
		
	    newDescription = (EditText)findViewById(R.id.ETdescription);
        newAmount = (EditText)findViewById(R.id.ETamount);
        total = (TextView)findViewById(R.id.tvTotal);
        reloadExpenses();
        list = getListView();
        registerForContextMenu(list);
        
        final Button bNew = (Button) findViewById(R.id.bNewExpense);
        bNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent newmovement = new Intent(getApplicationContext(), NewMoveActivity.class);
            	startActivity(newmovement);
            	/*
                if (newDescription.getText().toString().matches("")){
                	Toast toast = Toast.makeText(getApplicationContext(), R.string.nodescription, Toast.LENGTH_SHORT);
                	toast.show();
                } 
                else if (newAmount.getText().toString().matches("")){
                	Toast toast = Toast.makeText(getApplicationContext(), R.string.noamount, Toast.LENGTH_SHORT);
                	toast.show();
                }  
                else {
                	Movement m = new Movement(newDescription.getText().toString(),-1.0*Double.parseDouble(newAmount.getText().toString()),
                			(long)System.currentTimeMillis()/1000,
                			currency);
                	Log.d(TAG,m.toString());
                	manager.insert(m.getDescription(), m.getAmount(),m.getDate(),m.getCurrency());
                	newDescription.setText("");
                	newAmount.setText("");
                	Toast toast = Toast.makeText(getApplicationContext(), R.string.newexpenseok, Toast.LENGTH_SHORT);
                	toast.show();
                	reloadExpenses();
                	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(newAmount.getWindowToken(), 0);
                	
                }*/
                
            }
        });
        
        final Button bCancel = (Button) findViewById(R.id.bCancelExpense);
        bCancel.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		newDescription.setText("");
            	newAmount.setText("");
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(newAmount.getWindowToken(), 0);
			}
                	
        });
        

	}

	private void reloadExpenses() {
		//FILL THE ARRAYLIST<MOVEMENT>
		moves = new ArrayList<Movement>();
		moves = manager.get_expenses(this.getApplicationContext());
		mAdapter = new MovementAdapter(this, moves);
	    setListAdapter(mAdapter);
	    total.setText(getString(R.string.total)+" = "+manager.get_total_expenses(this.getApplicationContext()).toString() + " " + currency);
	    
	}

	

	@Override
	protected void onStop() {
		super.onStop();
		
		
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
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.contextmovement, menu);
	    String title =menu.findItem(R.id.iDeleteAll).getTitle() + " " + getString(R.string.title_activity_expenses); 
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
	            reloadExpenses();
	            return true;
	        case R.id.iDeleteAll:
	        	manager.deleteExpenses();
	        	reloadExpenses();
	            return true;
	        case R.id.iReboot:
	        	manager.reset();
	            reloadExpenses();
	            return true;
	        
	        default:
	            return super.onContextItemSelected(item);
	    }
	}


}
