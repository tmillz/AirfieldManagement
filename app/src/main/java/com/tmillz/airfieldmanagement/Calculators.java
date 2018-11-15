package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculators extends Fragment {

	private EditText editText1;
	private EditText editText2;
	private EditText editText3;
	private EditText editText4;
	private String ts;
	private BigDecimal scaled;
	private BigDecimal value;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.calculators, container, false);
		Button button1 = view.findViewById(R.id.button1);
		Button button2 = view.findViewById(R.id.button2);
		editText1 = view.findViewById(R.id.editText1);
		editText2 = view.findViewById(R.id.editText2);
		editText3 = view.findViewById(R.id.editText3);
		editText4 = view.findViewById(R.id.editText4);

		button1.setOnClickListener(v -> {

			if (editText1.getText().toString().equals("")) {
				Toast.makeText(getContext(), "Please enter a value",
						Toast.LENGTH_SHORT).show();
				return;
			}
			ts =  new BigDecimal (editText1.getText().toString()).subtract(new BigDecimal(
					"1000")).toString();
			value = new BigDecimal (ts);
			MathContext mc = new MathContext(4, RoundingMode.FLOOR);
			scaled = value.divide(new BigDecimal ("7"), mc);
			editText2.setText(String.format("%f", scaled));
		});

		button2.setOnClickListener(v -> {
            Context context = getActivity();
            if (editText3.getText().toString().equals("")) {
                Toast.makeText(
                        context, "Please enter a value", Toast.LENGTH_SHORT).show();
                return;
            }
            ts =  new BigDecimal (editText3.getText().toString()).subtract(new BigDecimal(
                    "200")).toString();
            value = new BigDecimal (ts);
            MathContext mc = new MathContext(4, RoundingMode.FLOOR);
            scaled = value.divide(new BigDecimal ("50"), mc);
            editText4.setText(String.format("%f", scaled));
        });
		return view;
	}
}
	
