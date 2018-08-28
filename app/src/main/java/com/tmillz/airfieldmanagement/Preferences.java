package com.tmillz.airfieldmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	public static final String KEY_CHOOSE_THEME = "choose_theme";
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {

		 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 boolean choose_theme = pref.getBoolean("choose_theme", false);
		 if(choose_theme){
			 getActivity().setTheme(R.style.AppTheme_Dark);
		 } else getActivity().setTheme(R.style.AppTheme);

		 super.onCreate(savedInstanceState);

		 // Load the preferences from an XML resource
		 addPreferencesFromResource(R.xml.preferences);
	 }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if (key.equals(KEY_CHOOSE_THEME)) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Apply Theme Now?");
			builder.setMessage("You must restart the App to apply the theme now");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(getActivity(), MainActivity.class);
		            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(i);
				}
		    
			 });
		builder.show();
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	    super.onPause();
	}
}