package com.tmillz.airfieldmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Forms extends Fragment {
	
	Button workorder;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view =inflater.inflate(R.layout.forms, container, false);
		workorder = (Button) view.findViewById(R.id.button1);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Forms");

		workorder.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
}
	
