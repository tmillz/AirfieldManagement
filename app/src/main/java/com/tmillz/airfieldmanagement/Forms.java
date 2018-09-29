package com.tmillz.airfieldmanagement;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class Forms extends Fragment {
	
	Button workorder;
	Button flightPlan;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View view =inflater.inflate(R.layout.forms, container, false);
		workorder = (Button) view.findViewById(R.id.button1);
		flightPlan = (Button) view.findViewById(R.id.button2);

		workorder.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				File pdfFile = new File(getContext().getExternalFilesDir(null),
						"af332.pdf" );
				if(pdfFile.exists()) {
					Intent pdfIntent = new Intent(Intent.ACTION_VIEW,
							FileProvider.getUriForFile(getActivity(),
									getActivity().getApplicationContext().getPackageName()
											+ ".provider", pdfFile));
					pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

					try {
						startActivity(pdfIntent);
					}
					catch(ActivityNotFoundException e) {
						Toast.makeText(getContext(),
								"No Application available to view pdf",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		flightPlan.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				File pdfFile = new File(getContext().getExternalFilesDir(null),
						"dd0175.pdf" );
				if(pdfFile.exists()) {
					Intent pdfIntent = new Intent(Intent.ACTION_VIEW,
							FileProvider.getUriForFile(getActivity(),
									getActivity().getApplicationContext().getPackageName()
											+ ".provider", pdfFile));
					pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

					try {
						startActivity(pdfIntent);
					}
					catch(ActivityNotFoundException e) {
						Toast.makeText(getContext(),
								"No Application available to view pdf",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
}
	
