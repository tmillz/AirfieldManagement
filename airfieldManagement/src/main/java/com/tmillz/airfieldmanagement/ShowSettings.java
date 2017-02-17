package com.tmillz.airfieldmanagement;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ShowSettings extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
	}
}
