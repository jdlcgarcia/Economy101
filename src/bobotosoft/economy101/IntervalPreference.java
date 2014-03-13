package bobotosoft.economy101;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;


public class IntervalPreference extends DialogPreference {
	private static final String TAG = "IntervalPreference";
	private String dateString;
	private String changedValueCanBeNull;

	private String mKey;
	private String mIntervalStart;
	private String mIntervalEnd; 
	private DatePicker.OnDateChangedListener changeDate = new DatePicker.OnDateChangedListener() {
		
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar selected = new GregorianCalendar(year, monthOfYear,
					dayOfMonth, 0, 0);
			
			changedValueCanBeNull = formatter().format(selected.getTime());

		}
	};
	private SharedPreferences pref;
	private String startDate,endDate;
	
	public IntervalPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public IntervalPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("NewApi")
	@Override
	protected View onCreateDialogView() {
		mKey = this.getKey();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
		mIntervalStart = pref.getString("intervalStart","");
		mIntervalEnd = pref.getString("intervalEnd","");

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into datepicker
		DatePicker datepicker = new DatePicker(this.getContext());
		datepicker.init(year, month, day, changeDate);
		changedValueCanBeNull = formatter().format(c.getTime());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			datepicker.setCalendarViewShown(false);
		return datepicker;
	}

	public Calendar getDate() {
		try {
			Date date = formatter().parse(defaultValue());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		} catch (java.text.ParseException e) {
			return defaultCalendar();
		}
	}

	public void setDate(String timeString) {
		this.dateString = timeString;
	}

	public static DateFormat formatter() {
		return new SimpleDateFormat("dd-MM-yyyy");
	}

	public static DateFormat summaryFormatter(Context context) {
		return android.text.format.DateFormat.getDateFormat(context);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object def) {
		if (restoreValue) {
			this.dateString = getPersistedString(defaultValue());
			setTheDate(this.dateString);
		} else {
			boolean wasNull = this.dateString == null;
			setDate((String) def);
			if (!wasNull)
				persistDate(this.dateString);
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		if (isPersistent())
			return super.onSaveInstanceState();
		else
			return new SavedState(super.onSaveInstanceState());
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			super.onRestoreInstanceState(state);
			setTheDate(((SavedState) state).dateValue);
		} else {
			SavedState s = (SavedState) state;
			super.onRestoreInstanceState(s.getSuperState());
			setTheDate(s.dateValue);
		}
	}

	@Override
	protected void onDialogClosed(boolean shouldSave) {
		if (shouldSave && this.changedValueCanBeNull != null) {
			setTheDate(this.changedValueCanBeNull);
			this.changedValueCanBeNull = null;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Date selectedDate = stringToDate(this.changedValueCanBeNull);
		boolean wrongDate = false;
		pref = PreferenceManager.getDefaultSharedPreferences(getContext());
		startDate = pref.getString("intervalStart", "");
		endDate = pref.getString("intervalEnd", "");
		Log.d(TAG,"inicio: "+startDate);
		if (which == -1)
		{
			if (mKey.equals("intervalStart") && endDate != "")
			{
				Date lastEndDate = stringToDate(mIntervalEnd);
				
				if (lastEndDate.compareTo(selectedDate) < 0) // lastEndDate goes before the selected. WRONG
				{
					Toast.makeText(getContext(), "You have to choose a date before finish date", Toast.LENGTH_LONG).show();
					wrongDate = true;
				}	
			}
			else if (mKey.equals("intervalEnd") && startDate != "")
			{
				Date lastStartDate = stringToDate(mIntervalStart);
				if (lastStartDate.compareTo(selectedDate) > 0) // lastStartDate goes after the selected. WRONG
				{
					Toast.makeText(getContext(), "You have to choose a date after start date", Toast.LENGTH_LONG).show();
					wrongDate = true;
				}			
			}
		}
		if (!wrongDate)
		{
			super.onClick(dialog, which);
			if (getDialog().getCurrentFocus() != null)
				getDialog().getCurrentFocus().clearFocus();
		}
		
	}

	private void setTheDate(String s) {
		setDate(s);
		persistDate(s);
	}

	private void persistDate(String s) {
		persistString(s);
		setSummary(summaryFormatter(getContext()).format(getDate().getTime()));
	}

	public static Calendar defaultCalendar() {
		return new GregorianCalendar(1970, 0, 1, 0, 0);
	}

	public static String defaultCalendarString() {
		return formatter().format(defaultCalendar().getTime());
	}

	private String defaultValue() {
		if (this.dateString == null)
			setDate(defaultCalendarString());
		return this.dateString;
	}

	public static Calendar getTimeFor(SharedPreferences preferences,
			String field) {
		Date date = stringToDate(preferences.getString(field,
				defaultCalendarString()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	private static Date stringToDate(String timeString) {
		try {
			return formatter().parse(timeString);
		} catch (ParseException e) {
			return defaultCalendar().getTime();
		}
	}
	
	

	private static class SavedState extends BaseSavedState {

		String dateValue;

		public SavedState(Parcel p) {
			super(p);
			dateValue = p.readString();
		}

		public SavedState(Parcelable p) {
			super(p);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeString(dateValue);
		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	
}