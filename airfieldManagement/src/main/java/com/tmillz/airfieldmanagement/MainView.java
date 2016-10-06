package com.tmillz.airfieldmanagement;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
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
	
	public MainView(){}
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.regulations, null);
        Context context = getActivity();
        // get the listview
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Regulations");
        
     // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
        	
        	String child;
        	
        	Context context = getActivity().getApplicationContext();
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	
            	child = (String) listDataHeader.get(groupPosition) + ":" + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString();
            	File pdfFile;
            	Toast.makeText(context, "opening " +child.toString(), Toast.LENGTH_SHORT).show();
            	
                if (child.toString().equalsIgnoreCase("AFIs:AFI 13-204v3")) {
                	 pdfFile = new File(context.getExternalFilesDir(null), "afi13_204v3.pdf" );
                	 if(pdfFile.exists()) {
                         Uri path = Uri.fromFile(pdfFile); 
                         Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                         pdfIntent.setDataAndType(path, "application/pdf");
                         pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                         try {
                             startActivity(pdfIntent);
                         }
                         catch(ActivityNotFoundException e) {
                             Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                         }
                     }
                }
                if (child.toString().equalsIgnoreCase("AFIs:AFI 13-213")) {
               	 pdfFile = new File(context.getExternalFilesDir(null), "afi13_213.pdf" );
               	 if(pdfFile.exists()) {
                        Uri path = Uri.fromFile(pdfFile); 
                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                        pdfIntent.setDataAndType(path, "application/pdf");
                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        try {
                            startActivity(pdfIntent);
                        }
                        catch(ActivityNotFoundException e) {
                            Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                        }
                    }
                }
                if (child.toString().equalsIgnoreCase("AFIs:AFI 13-202")) {
                  	 pdfFile = new File(context.getExternalFilesDir(null), "afi13_202.pdf" );
                  	 if(pdfFile.exists()) {
                           Uri path = Uri.fromFile(pdfFile); 
                           Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                           pdfIntent.setDataAndType(path, "application/pdf");
                           pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                           try {
                               startActivity(pdfIntent);
                           }
                           catch(ActivityNotFoundException e) {
                               Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                           }
                     }
                }
                if (child.toString().equalsIgnoreCase("AFIs:AFJMAN 11-213")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "afjman11_213.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
                     }
                }
                if (child.toString().equalsIgnoreCase("AFIs:AFI 11-218")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "afi11_218.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
                      }
                }
                if (child.toString().equalsIgnoreCase("AFIs:AFI 13-202")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "afi13_202.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
                     }
                }
                if (child.toString().equalsIgnoreCase("AFIs:AFI 10-1001")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "afi10_1001.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
                     }
                }
                if (child.toString().equalsIgnoreCase("AFIs:AFI 10-1002")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "afi10_1002.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
                     }
                }

                if (child.toString().equalsIgnoreCase("AFIs:AFI 32-1042")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "afi32_1042.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
                      }
                }

                if (child.toString().equalsIgnoreCase("AFIs:AFI 36-2201")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "afi36_2201.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
                      }
                 }
                if (child.toString().equalsIgnoreCase("UFCs:UFC 3-260-01")) {
               	 pdfFile = new File(context.getExternalFilesDir(null), "ufc_3_260_01.pdf" );
               	 if(pdfFile.exists()) {
                        Uri path = Uri.fromFile(pdfFile); 
                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                        pdfIntent.setDataAndType(path, "application/pdf");
                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        try {
                            startActivity(pdfIntent);
                        }
                        catch(ActivityNotFoundException e) {
                            Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                        }
                    }
                 }
                if (child.toString().equalsIgnoreCase("UFCs:UFC 3-535-01")) {
                  	 pdfFile = new File(context.getExternalFilesDir(null), "ufc_3_535_01.pdf" );
                  	 if(pdfFile.exists()) {
                           Uri path = Uri.fromFile(pdfFile); 
                           Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                           pdfIntent.setDataAndType(path, "application/pdf");
                           pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                           try {
                               startActivity(pdfIntent);
                           }
                           catch(ActivityNotFoundException e) {
                               Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                           }
                     }
                }
                if (child.toString().equalsIgnoreCase("ETLs:ETL 04-2")) {
                  	 pdfFile = new File(context.getExternalFilesDir(null), "etl_04_2.pdf" );
                  	 if(pdfFile.exists()) {
                           Uri path = Uri.fromFile(pdfFile); 
                           Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                           pdfIntent.setDataAndType(path, "application/pdf");
                           pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                           try {
                               startActivity(pdfIntent);
                           }
                           catch(ActivityNotFoundException e) {
                               Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                           }
                     }
                }
                if (child.toString().equalsIgnoreCase("FAA:JO 7110.10")) {
                 	 pdfFile = new File(context.getExternalFilesDir(null), "fss.pdf" );
                 	 if(pdfFile.exists()) {
                          Uri path = Uri.fromFile(pdfFile); 
                          Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                          pdfIntent.setDataAndType(path, "application/pdf");
                          pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          try {
                              startActivity(pdfIntent);
                          }
                          catch(ActivityNotFoundException e) {
                              Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
                          }
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
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("AFIs");
        listDataHeader.add("UFCs");
        listDataHeader.add("ETLs");
        listDataHeader.add("FAA");
 
        // Adding child data
        List<String> afi = new ArrayList<String>();
        afi.add("AFI 13-204v3");
        afi.add("AFI 13-213");
        afi.add("AFI 13-202");
        afi.add("AFJMAN 11-213");
        afi.add("AFI 11-218");
        afi.add("AFI 10-1001");
        afi.add("AFI 10-1002");
        afi.add("AFI 32-1042");
        afi.add("AFI 36-2201");
 
        List<String> ufc = new ArrayList<String>();
        ufc.add("UFC 3-260-01");
        ufc.add("UFC 3-535-01");
 
        List<String> etl = new ArrayList<String>();
        etl.add("ETL 04-2");
        
        List<String> faa = new ArrayList<String>();
        faa.add("JO 7110.10");

 
        listDataChild.put(listDataHeader.get(0), afi); // Header, Child data
        listDataChild.put(listDataHeader.get(1), ufc);
        listDataChild.put(listDataHeader.get(2), etl);
        listDataChild.put(listDataHeader.get(3), faa);
    }
}
    

