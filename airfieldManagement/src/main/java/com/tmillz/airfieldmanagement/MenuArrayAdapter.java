package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class MenuArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public MenuArrayAdapter(Context context, String[] values) {
		super(context, R.layout.menu_list, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.menu_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);
 
		// Change icon based on name
		String s = values[position];
 
		System.out.println(s);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean choose_theme = pref.getBoolean("choose_theme", false);
	    if(choose_theme == true){
			if (s.equals("Regulations")) {
				imageView.setImageResource(R.drawable.ic_action_paste_dark);
			} else if (s.equals("Aircraft")) {
				imageView.setImageResource(R.drawable.ic_action_airplane_mode_on_dark);
			} else if (s.equals("Calculators")) {
				imageView.setImageResource(R.drawable.ic_calculator_dark);
			} else if (s.equals("NOTAMs")) {
				imageView.setImageResource(R.drawable.ic_action_warning_dark);
			} else if (s.equals("BowMonk Converter")) {
				imageView.setImageResource(R.drawable.ic_calculator_dark);
			} else if (s.equals("Links")) {
				imageView.setImageResource(R.drawable.ic_action_web_site_dark);
			} else if (s.equals("Map")) {
				imageView.setImageResource(R.drawable.ic_action_map_dark);
			} else if (s.equals("Forms")) {
				imageView.setImageResource(R.drawable.ic_action_paste_dark);
			} else if (s.equals("Rate App")) {
				imageView.setImageResource(R.drawable.ic_action_important_dark);
			} else if (s.equals("Email Developer")) {
				imageView.setImageResource(R.drawable.ic_action_email_dark);
			} else {
				imageView.setImageResource(R.drawable.ic_action_warning_dark);
			}
	    }
	    
	    if(choose_theme == false){
			if (s.equals("Regulations")) {
				imageView.setImageResource(R.drawable.ic_action_paste);
			} else if (s.equals("Aircraft")) {
				imageView.setImageResource(R.drawable.ic_action_airplane_mode_on);
			} else if (s.equals("Calculators")) {
				imageView.setImageResource(R.drawable.ic_calculator);
			} else if (s.equals("NOTAMs")) {
				imageView.setImageResource(R.drawable.ic_action_warning);
			} else if (s.equals("BowMonk Converter")) {
				imageView.setImageResource(R.drawable.ic_calculator);
			} else if (s.equals("Links")) {
				imageView.setImageResource(R.drawable.ic_action_web_site);
			} else if (s.equals("Map")) {
				imageView.setImageResource(R.drawable.ic_action_map);
			} else if (s.equals("Forms")) {
				imageView.setImageResource(R.drawable.ic_action_paste);
			} else if (s.equals("Rate App")) {
				imageView.setImageResource(R.drawable.ic_action_important);
			} else if (s.equals("Email Developer")) {
				imageView.setImageResource(R.drawable.ic_action_email);
			} else {
				imageView.setImageResource(R.drawable.ic_action_warning);
			}
	    }
	    return rowView;
	}
}