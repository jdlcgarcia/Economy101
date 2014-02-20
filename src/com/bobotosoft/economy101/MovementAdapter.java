package com.bobotosoft.economy101;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MovementAdapter extends ArrayAdapter<Movement>{

	private Context context;
	private ArrayList<Movement> allMoves;
	private LayoutInflater mInflater;
	private boolean mNotifyOnChange;
	String currency;
	public MovementAdapter(Context context, ArrayList<Movement> moves) {
		super(context, R.layout.movement);
	    this.context = context;
	    this.allMoves = new ArrayList<Movement>(moves);
	    this.mInflater = LayoutInflater.from(context);
	    SharedPreferences pref =
	            PreferenceManager.getDefaultSharedPreferences(this.context);
		currency = pref.getString("currency", "");
	}


@Override
public int getCount() {
    return allMoves.size();
}

@Override
public Movement getItem(int position) {
    return allMoves.get(position);
}

@Override
public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
}

@Override
public int getPosition(Movement item) {
    return allMoves.indexOf(item);
}

@Override
public int getViewTypeCount() {
    return 1; //Number of types + 1 !!!!!!!!
}

@Override
public int getItemViewType(int position) {
    return 1;
}


@Override
public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    int type = getItemViewType(position);
    if (convertView == null) {
        holder = new ViewHolder();
        switch (type) {
        case 1:
            convertView = mInflater.inflate(R.layout.movement,parent, false);
            holder.description = (TextView) convertView.findViewById(R.id.TVdescription);
            holder.amount = (TextView) convertView.findViewById(R.id.TVamount);
            break;
        }
        convertView.setTag(holder);
    } else {
        holder = (ViewHolder) convertView.getTag();
    }
    holder.description.setText(allMoves.get(position).getDescription());
    holder.amount.setText(String.valueOf(allMoves.get(position).getAmount())+" "+currency);
    holder.pos = position;
    return convertView;
}

@Override
public void notifyDataSetChanged() {
    super.notifyDataSetChanged();
    mNotifyOnChange = true;
}

public void setNotifyOnChange(boolean notifyOnChange) {
    mNotifyOnChange = notifyOnChange;
}


//---------------static views for each row-----------//
     static class ViewHolder {

         TextView amount;
         TextView description;
         int pos; //to store the position of the item within the list
     }
}
	
