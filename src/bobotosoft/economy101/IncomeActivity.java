package bobotosoft.economy101;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class IncomeActivity extends ListActivity {

	private static final String TAG = "INCOME";
	ListView income;
	DBManager manager;
	private ArrayList<Movement> moves;
	private MovementAdapter mAdapter;
	EditText newDescription;
	EditText newAmount;
	View list;
	SharedPreferences pref;
	String currency;
	TextView total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		pref = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		currency = pref.getString("currency", "");

		manager = new DBManager(this);

		newDescription = (EditText) findViewById(R.id.ETdescription);
		newAmount = (EditText) findViewById(R.id.ETamount);
		total = (TextView) findViewById(R.id.tvTotal);

		reloadIncome();
		list = getListView();
		registerForContextMenu(list);

		final Button bNew = (Button) findViewById(R.id.bNewExpense);

		bNew.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (newDescription.getText().toString().matches("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							R.string.nodescription, Toast.LENGTH_SHORT);
					toast.show();
				} else if (newAmount.getText().toString().matches("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							R.string.noamount, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					Movement m = new Movement(newDescription.getText()
							.toString(), Double.parseDouble(newAmount.getText()
							.toString()),
							(long) System.currentTimeMillis() / 1000, currency);
					
					manager.insert(m.getDescription(), m.getAmount(),
							m.getDate(), m.getCurrency());
					newDescription.setText("");
					newAmount.setText("");
					Toast toast = Toast.makeText(getApplicationContext(),
							R.string.newincomeok, Toast.LENGTH_SHORT);
					toast.show();
					reloadIncome();

				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(newAmount.getWindowToken(), 0);
			}

		});

	}

	private void reloadIncome() {
		// FILL THE ARRAYLIST<MOVEMENT>
		moves = new ArrayList<Movement>();
		moves = manager.get_income(this.getApplicationContext());
		mAdapter = new MovementAdapter(this, moves);
		setListAdapter(mAdapter);
		total.setText(getString(R.string.total) + " = "
				+ manager.get_total_income(this.getApplicationContext()).toString() + " " + currency);

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
		AdapterView.AdapterContextMenuInfo info;
		try {
            // Casts the incoming data object into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            // If the menu object can't be cast, logs an error.
            Log.e(TAG, "bad menuInfo", e);
            return;
        }
		
		Movement m = (Movement) getListAdapter().getItem(info.position);
		
		Date date = new Date(m.getDate());
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
		menu.setHeaderTitle(m.getDescription() + " ("+dateFormat.format(date)+")");// if your table name is name
        MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextmovement, menu);
		String title = menu.findItem(R.id.iDeleteAll).getTitle() + " "
				+ getString(R.string.title_activity_income);
		menu.findItem(R.id.iDeleteAll).setTitle(title);
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
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
