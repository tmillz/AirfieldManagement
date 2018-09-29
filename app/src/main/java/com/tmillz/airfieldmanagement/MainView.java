package com.tmillz.airfieldmanagement;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainView extends Fragment {
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    File pdfFile = null;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.regulations, null);
        Context context = getActivity();

        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);
 
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
 
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {
        	
        	String child;
        	
        	Context context = getActivity().getApplicationContext();
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	
            	child = listDataHeader.get(groupPosition) + ":" +
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

            	// Needs Refactor to Clean Code
                    if (child.equalsIgnoreCase("AFIs:AFI 13-204v3")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi13_204v3.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 13-213")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi13_213.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 13-202")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi13_202.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFJMAN 11-213")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afjman11_213.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 11-218")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi11_218.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 13-202")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi13_202.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 10-1001")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi10_1001.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 10-1002")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi10_1002.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 32-1042")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi32_1042.pdf");
                    }
                    if (child.equalsIgnoreCase("AFIs:AFI 36-2201")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "afi36_2201.pdf");
                    }
                    if (child.equalsIgnoreCase("UFCs:UFC 3-260-01")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "ufc_3_260_01.pdf");
                    }
                    if (child.equalsIgnoreCase("UFCs:UFC 3-535-01")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "ufc_3_535_01.pdf");
                    }
                    if (child.equalsIgnoreCase("UFCs:UFC 3-260-04")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "ufc_3_260_04.pdf");
                    }
                    if (child.equalsIgnoreCase("ETLs:ETL 04-2")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "etl_04_2.pdf");
                    }
                    if (child.equalsIgnoreCase("FAA:JO 7110.10")) {
                        pdfFile = new File(context.getExternalFilesDir(null),
                                "JO_7110_10.pdf");
                    }
                    if(pdfFile != null) {
                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW,
                                FileProvider.getUriForFile(context,
                                        context.getApplicationContext().getPackageName() +
                                                ".provider", pdfFile));
                        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            startActivity(pdfIntent);
                        }
                        catch(ActivityNotFoundException e) {
                            Toast.makeText(context, "No Application available to view pdf",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                return false;
            }
        });
        return v;
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
    
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
 
        // Adding child data
        listDataHeader.add("AFIs");
        listDataHeader.add("UFCs");
        listDataHeader.add("ETLs");
        listDataHeader.add("FAA");
 
        // Adding child data
        List<String> afi = new ArrayList<>();
        afi.add("AFI 13-204v3");
        afi.add("AFI 13-213");
        afi.add("AFI 13-202");
        afi.add("AFJMAN 11-213");
        afi.add("AFI 11-218");
        afi.add("AFI 10-1001");
        afi.add("AFI 10-1002");
        afi.add("AFI 32-1042");
        afi.add("AFI 36-2201");
 
        List<String> ufc = new ArrayList<>();
        ufc.add("UFC 3-260-01");
        ufc.add("UFC 3-535-01");
        ufc.add("UFC 3-260-04");
 
        List<String> etl = new ArrayList<>();
        etl.add("ETL 04-2");
        
        List<String> faa = new ArrayList<>();
        faa.add("JO 7110.10");

        listDataChild.put(listDataHeader.get(0), afi); // Header, Child data
        listDataChild.put(listDataHeader.get(1), ufc);
        listDataChild.put(listDataHeader.get(2), etl);
        listDataChild.put(listDataHeader.get(3), faa);
    }
}
    

