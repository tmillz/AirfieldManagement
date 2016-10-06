package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Bowmonk extends Fragment {
	
	public Bowmonk(){
		super();
	}
	
	Button calculate;
	TextView rcr;
	EditText bm;
	String bms;
	BigDecimal scaled;
	BigDecimal value;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v =inflater.inflate(R.layout.bowmonk, container, false);
		final Context context = getActivity();
		calculate = (Button) v.findViewById(R.id.button1);
		bm=(EditText) v.findViewById(R.id.editText1);
		rcr=(TextView) v.findViewById(R.id.textView3);
		((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Bowmonk Converter");
		final InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.RESULT_SHOWN);
		
		
		calculate.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			Context context = getActivity(); 

			inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, InputMethodManager.RESULT_SHOWN);
			
			if (bm.getText().toString().equals("")) {
				Toast.makeText(
                        context, "Please enter a bowmonk reading", Toast.LENGTH_SHORT).show();
				return;
			}
			bms = new String (new BigDecimal (bm.getText().toString()).multiply(new BigDecimal("0.323")).toString());
			value = new BigDecimal (bms);
			scaled = value.setScale(0, RoundingMode.DOWN);
			if ((scaled.compareTo(new BigDecimal("17"))== 1) || (scaled.compareTo(new BigDecimal("17"))== 0)) {
				rcr.setText("RCR: " + scaled.toString() + " Braking action GOOD");
			} else if ((scaled.compareTo(new BigDecimal("12"))== 1) && (scaled.compareTo(new BigDecimal("17"))== -1) || (scaled.compareTo(new BigDecimal("12"))== 0)) {
				rcr.setText("RCR: " + scaled.toString() + " Braking action FAIR");
			} else if ((scaled.compareTo(new BigDecimal("6"))== 1) && (scaled.compareTo(new BigDecimal("12"))== -1)|| (scaled.compareTo(new BigDecimal("6"))== 0)) {
				rcr.setText("RCR: " + scaled.toString() + " Braking action POOR");
			} else if (scaled.compareTo(new BigDecimal("5"))== -1) {
				rcr.setText("RCR: " + scaled.toString() + " Braking action NILL");
			}
		}
		});
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
}
	
