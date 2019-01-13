package com.tmillz.airfieldmanagement;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

public class Forms extends Fragment {


	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
		View view =inflater.inflate(R.layout.forms, container, false);
		Button workorder = view.findViewById(R.id.button1);
		Button flightPlan = view.findViewById(R.id.button2);

		workorder.setOnClickListener(view1 -> {
            File pdfFile = new File(Objects.requireNonNull(getContext()).getExternalFilesDir(
                    null),"af332.pdf" );
            if(pdfFile.exists()) {
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW,
                        FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                                Objects.requireNonNull(getActivity())
                                        .getApplicationContext().getPackageName()
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
        });

		flightPlan.setOnClickListener(view12 -> {
            File pdfFile = new File(Objects.requireNonNull(getContext())
                    .getExternalFilesDir(null),"dd0175.pdf" );
            if(pdfFile.exists()) {
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW,
                        FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                                Objects.requireNonNull(getActivity())
                                        .getApplicationContext().getPackageName()
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
        });

		return view;
	}

}
	
