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
    HashMap<String, ArrayList<RegsList>> listDataChild;

    File pdfFile = null;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.regulations, null);
        Context context = getActivity();

        expListView = v.findViewById(R.id.lvExp);
 
        prepareListData();

        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
 
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {
        	
        	String child;
        	
        	Context context = getActivity().getApplicationContext();
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {

                child = listDataChild.get(listDataHeader.get(groupPosition))
                        .get(childPosition).file;

            	pdfFile = new File(context.getExternalFilesDir(null), child);

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
        listDataHeader.add("AFIs: Air Force Instructions");
        listDataHeader.add("UFCs: Unified Facility Criteria");
        listDataHeader.add("ETLs: Engineering Technical Letters");
        listDataHeader.add("FAA: Federal Aviation Administration");
        listDataHeader.add("MAJCOM Supplements");
 
        // Adding child data
        ArrayList<RegsList> afi = new ArrayList<>();
        afi.add(new RegsList("AFI 13-204v3 Airfield Operations", "afi13_204v3.pdf"));
        afi.add(new RegsList("AFI 13-204v2 Airfield Operations Standardization and Evaluations"
                ,"afi13-204v2.pdf"));
        afi.add(new RegsList("AFI 13-204v1 Airfield Operations Career Field Development",
                "afi13-204v1.pdf"));
        afi.add(new RegsList("AFI 13-213 Airfield Driving","afi13-213.pdf"));
        afi.add(new RegsList("AFI 11-208IP Department of Defense Notice to Airment System",
                "afi11-208_ip.pdf"));
        afi.add(new RegsList("AFI 11-218 Aircraft Operations and Movement on the Ground",
                "afi11_218.pdf"));
        afi.add(new RegsList("AFI 13-217 Drop Zone and Landing Zone Operations",
                "afi13-217.pdf"));
        afi.add(new RegsList("AFI 10-1001 Civil Aircraft Landing Permits",
                "afi10-1001.pdf"));
        afi.add(new RegsList(
                "AFI 10-1801 Foreign Governmental Aircraft Landings at USAF Installations",
                "afi10-1801.pdf"));
        afi.add(new RegsList("AFMAN 32-1084 Facility Requirements","afman32-1084.pdf"));
        afi.add(new RegsList(
                "AFPAM 91-212 Bird/Aircraft Strike Hazard (BASH) Reduction Program",
                "afpam91-212.pdf"));
        afi.add(new RegsList("AFI 32-1041 Pavement Evaluation Program", "afi32-1041.pdf"));
        afi.add(new RegsList(
                "AFI 32-1043 Managing, Operating, and Maintaining Aircraft Arresting Systems",
                "afi32-1043.pdf"));

        ArrayList<RegsList> ufc = new ArrayList<>();
        ufc.add(new RegsList("test","ufc_3_260_01.pdf"));
        ufc.add(new RegsList("test","ufc_3_535_01.pdf"));
        ufc.add(new RegsList("test","ufc_3_260_04.pdf"));
 
        ArrayList<RegsList> etl = new ArrayList<>();
        etl.add(new RegsList("test","etl_04_2.pdf"));
        
        ArrayList<RegsList> faa = new ArrayList<>();
        faa.add(new RegsList("test","jo_7110.10.pdf"));

        ArrayList<RegsList> majcom = new ArrayList<>();
        majcom.add(new RegsList("test","AFI 13-204v1 ACCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v1 AFMCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v1 AFSPCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v1 PACAFSUP"));
        majcom.add(new RegsList("test","AFI 13-204v1 USAFESUP"));
        majcom.add(new RegsList("test","AFI 13-204v2 ACCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v2 AFMCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v2 USAFESUP"));
        majcom.add(new RegsList("test","AFI 13-204v3 ACCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v3 AETCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v3 AFMCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v3 AFSOCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v3 AMCSUP"));
        majcom.add(new RegsList("test","AFI 13-204v3 PACAFSUP"));
        majcom.add(new RegsList("test","AFI 13-204v3 USAFESUP"));

        listDataChild.put(listDataHeader.get(0), afi);
        listDataChild.put(listDataHeader.get(1), ufc);
        listDataChild.put(listDataHeader.get(2), etl);
        listDataChild.put(listDataHeader.get(3), faa);
        listDataChild.put(listDataHeader.get(4), majcom);

    }

    class RegsList{
        String reg;
        String file;

        RegsList(String reg, String file)
        {
            this.reg = reg;
            this.file = file;

        }
        //getters and setters here
    }



}
    

