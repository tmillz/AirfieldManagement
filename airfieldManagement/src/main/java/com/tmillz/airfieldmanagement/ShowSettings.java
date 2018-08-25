package com.tmillz.airfieldmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ShowSettings extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean choose_theme = pref.getBoolean("choose_theme", false);
		if(choose_theme){
			setTheme(R.style.AppTheme_Dark);
		} else setTheme(R.style.AppTheme);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.pref_with_actionbar);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Settings");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		//actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
		getFragmentManager().beginTransaction().replace(R.id.content_frame, new Preferences()).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}