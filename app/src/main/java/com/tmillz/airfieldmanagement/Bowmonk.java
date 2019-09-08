package com.tmillz.airfieldmanagement;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Bowmonk extends Fragment {

	private TextView rcr;
	private EditText bm;
	private String bms;
	private BigDecimal scaled;
	private BigDecimal value;
	
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.bowmonk, container, false);
		Button calculate = view.findViewById(R.id.button1);
		bm = view.findViewById(R.id.editText1);
		rcr = view.findViewById(R.id.textView3);

			calculate.setOnClickListener(v -> {
				if (bm.getText().toString().equals("")) {
					Toast.makeText(getContext(),
							"Please enter a bowmonk reading", Toast.LENGTH_SHORT).show();
					return;
				}
				bms = new BigDecimal (bm.getText().toString()).multiply(
						new BigDecimal("0.323")).toString();
				value = new BigDecimal (bms);
				scaled = value.setScale(0, RoundingMode.DOWN);
				if ((scaled.compareTo(new BigDecimal("17")) > 0) || (
						scaled.compareTo(new BigDecimal("17")) == 0)) {
					rcr.setText(getString(R.string.rcr_good, scaled.toString()));
				} else if ((scaled.compareTo(new BigDecimal("12")) > 0) && (
						scaled.compareTo(new BigDecimal("17")) < 0) || (
								scaled.compareTo(new BigDecimal("12")) == 0)) {
					rcr.setText(getString(R.string.rcr_fair, scaled.toString()));
				} else if ((scaled.compareTo(new BigDecimal("6")) > 0) && (
						scaled.compareTo(new BigDecimal("12")) < 0)|| (
								scaled.compareTo(new BigDecimal("6")) == 0)) {
					rcr.setText(getString(R.string.rcr_poor, scaled.toString()));
				} else if (scaled.compareTo(new BigDecimal("5")) < 0) {
					rcr.setText(getString(R.string.rcr_nil, scaled.toString()));
				}
			});
		return view;
	}
}
	
