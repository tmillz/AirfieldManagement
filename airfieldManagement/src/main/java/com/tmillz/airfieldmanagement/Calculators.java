package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculators extends Fragment {
	
	Button button1;
	Button button2;
	EditText editText1;
	EditText editText2;
	EditText editText3;
	EditText editText4;
	String ts;
	BigDecimal scaled;
	BigDecimal value;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.calculators, container, false);
		button1 = (Button) view.findViewById(R.id.button1);
		button2 = (Button) view.findViewById(R.id.button2);
		editText1=(EditText) view.findViewById(R.id.editText1);
		editText2=(EditText) view.findViewById(R.id.editText2);
		editText3=(EditText) view.findViewById(R.id.editText3);
		editText4=(EditText) view.findViewById(R.id.editText4);

		button1.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {

			if (editText1.getText().toString().equals("")) {
				Toast.makeText(getContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
				return;
			}
			ts =  new BigDecimal (editText1.getText().toString()).subtract(new BigDecimal("1000")).toString();
			value = new BigDecimal (ts);
			MathContext mc = new MathContext(4, RoundingMode.FLOOR);
			scaled = value.divide(new BigDecimal ("7"), mc);
			editText2.setText(scaled.toString());
		}
		});

		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context context = getActivity();
				if (editText3.getText().toString().equals("")) {
					Toast.makeText(
	                        context, "Please enter a value", Toast.LENGTH_SHORT).show();
					return;
				}
				ts =  new BigDecimal (editText3.getText().toString()).subtract(new BigDecimal("200")).toString();
				value = new BigDecimal (ts);
				MathContext mc = new MathContext(4, RoundingMode.FLOOR);
				scaled = value.divide(new BigDecimal ("50"), mc);
				editText4.setText(scaled.toString());
			}
			});
		return view;
	}
}
	
