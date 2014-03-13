package bobotosoft.economy101;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

public class NewMoveActivity extends Activity {
	SharedPreferences pref;
	public String currency;
	EditText edit;
	private Object positive;
	private Button negative;
	private CheckBox cbPeriodic;
	Spinner spPeriodic, spCurrencies;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_newmove);
		pref = PreferenceManager.getDefaultSharedPreferences(this);

		// Spinner of currencies
		spCurrencies = (Spinner) findViewById(R.id.spCurrencies);
		ArrayAdapter<CharSequence> adapterCurrencies = ArrayAdapter.createFromResource(
				this, R.array.pref_currency_values,
				android.R.layout.simple_spinner_item);
		adapterCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCurrencies.setAdapter(adapterCurrencies);

		// Spinner of currencies
		spPeriodic = (Spinner) findViewById(R.id.spPeriodic);
		ArrayAdapter<CharSequence> adapterPeriodic = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, new CharSequence[]{"daily", "weekly", "15-day", "monthly", "yearly"});
		spPeriodic.setAdapter(adapterPeriodic);
		adapterPeriodic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPeriodic.setAdapter(adapterPeriodic);

		cbPeriodic = (CheckBox) findViewById(R.id.cbPeriodic);
		cbPeriodic.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// if is a periodic movement, a spinner must be available with the
					// options: daily, weekly, 15-day, monthly, yearly
					spPeriodic.setVisibility(View.VISIBLE);
				}
				else{
					//the spinner is not shown.
					spPeriodic.setVisibility(View.GONE);
				}

			}
		});
	}

}
