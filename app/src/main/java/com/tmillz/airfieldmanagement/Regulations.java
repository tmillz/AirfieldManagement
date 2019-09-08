package com.tmillz.airfieldmanagement;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Regulations extends ListFragment {

    private List<String> listDataHeader;
    private HashMap<String, ArrayList<RegsList>> listDataChild;

    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        prepareListData();

        listAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.regulations_list_item, R.id.RegName, listDataHeader);
        setListAdapter(listAdapter);
    }
 
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regulations_list, null);
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        ListView listView = getListView();

        listView.setOnItemClickListener((parent, view1, position, id) -> {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack("Regulations");

            // animation
            ft.setCustomAnimations(R.anim.slide_up, 0);

            RegsSub regsSub = RegsSub.newInstance();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Regs", listDataChild.get(listDataHeader.get(position)));
            regsSub.setArguments(bundle);
            ft.replace(R.id.content_frame, regsSub, "RegsSub");
            ft.commit();
        });
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
        afi.add(new RegsList("AFI 13-204v3 Airfield Operations", "afi13-204v3.pdf"));

        afi.add(new RegsList("AFI 13-204v2 Airfield Operations Standardization and" +
                " Evaluations",
                "afi13-204v2.pdf"));
        afi.add(new RegsList("AFI 13-204v1 Airfield Operations Career Field Development",
                "afi13-204v1.pdf"));
        afi.add(new RegsList("AFI 13-213 Airfield Driving","afi13-213.pdf"));
        afi.add(new RegsList("AFI 11-208IP Department of Defense Notice to Airment System",
                "afi11-208_ip.pdf"));
        afi.add(new RegsList("AFI 11-218 Aircraft Operations and Movement on the Ground",
                "afi11-218.pdf"));
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
                "afi91-212.pdf"));
        afi.add(new RegsList("AFI 32-1041 Pavement Evaluation Program",
                "afi32-1041.pdf"));
        afi.add(new RegsList(
                "AFI 32-1043 Managing, Operating, and Maintaining Aircraft Arresting Systems",
                "afi32-1043.pdf"));

        ArrayList<RegsList> ufc = new ArrayList<>();
        ufc.add(new RegsList("UFC 3-260-01 Airfield and Heliport Planning and Design",
                "ufc_3_260_01_2008.pdf"));
        ufc.add(new RegsList("UFC 3-260-02 Pavement Design For Airfields",
                "ufc_3_260_02_2001.pdf"));
        ufc.add(new RegsList("UFC 3-260-03 Airfield Pavement Evaluation",
                "ufc_3_260_03_2001.pdf"));
        ufc.add(new RegsList("UFC 3-260-04 Airfield and Heliport Marking",
                "ufc_3_260_04_2018.pdf"));
        ufc.add(new RegsList(
                "UFC 3-270-05 Paver Concrete Surfaced Airfields Pavement Condition Index (PCI)"
                , "ufc_3_270_05_2001.pdf"));
        ufc.add(new RegsList(
                "UFC 3-270-06 Paver Asphalt Surfaced Airfields Pavement Condition Index (PCI)",
                "ufc_3_270_06_2001.pdf"));
        ufc.add(new RegsList(
                "UFC 3-535-01 Visual Air Navigation Facilities","ufc_3_535_01_c1_2018.pdf"));
 
        ArrayList<RegsList> etl = new ArrayList<>();
        etl.add(new RegsList(
                "ETL 02-19 Air Field Pavement Evaluation Standards and Procedures",
                "etl_02_19.pdf"));
        etl.add(new RegsList(
                "ETL 07-3 Jet Engine Trust Standoff Requirements for Airfield Asphalt Edge" +
                        " Pavements",
                "etl_07_3.pdf"));
        etl.add(new RegsList(
                "UFGS 32 01 11.51 Rubber and Paint Removal From Airfield Pavements",
                "UFGS 32 01 11.51.pdf"));

        ArrayList<RegsList> faa = new ArrayList<>();
        faa.add(new RegsList("AC 150/5200-18 Airport Safety Self-Inspection",
                "AC_150_5200-18C.pdf"));
        faa.add(new RegsList("AC 150/5300-13 Airport Design",
                "150-5300-13A-chg1-interactive-201804.pdf"));
        faa.add(new RegsList("AC 150/5340-1 Standards for Airport Markings",
                "150_5340_1l.pdf"));
        faa.add(new RegsList("AC 150/5340-18 Standards for Airport Sign Systems",
                "150_5340_18f.pdf"));
        faa.add(new RegsList(
                "AC 150/5345-44 Specification for Runway and Taxiway Signs Document" +
                        " Information",
                "150-5345-44K.pdf"));

        ArrayList<RegsList> majcom = new ArrayList<>();
        majcom.add(new RegsList("AFI 13-204v1 ACCSUP","afi13-204v1_accsup.pdf"));
        majcom.add(new RegsList("AFI 13-204v1 AFMCSUP","afi13-204v1_afmcsup_i.pdf"));
        majcom.add(new RegsList("AFI 13-204v1 AFSPCSUP","afi13-204v1_afspcsup.pdf"));
        majcom.add(new RegsList("AFI 13-204v1 USAFESUP", "afi13-204v1_usafesup.pdf"));
        majcom.add(new RegsList("AFI 13-204v2 ACCSUP", "afi13-204v2_accsup.pdf"));
        majcom.add(new RegsList("AFI 13-204v2 AFMCSUP", "afi13-204v2_afmcsup_i.pdf"));
        majcom.add(new RegsList("AFI 13-204v2 USAFESUP", "afi13-204v2_usafesup.pdf"));
        majcom.add(new RegsList("AFI 13-204v3 ACCSUP", "afi13-204v3_accsup.pdf"));
        majcom.add(new RegsList("AFI 13-204v3 AETCSUP", "afi13-204v3_aetcsup.pdf"));
        majcom.add(new RegsList("AFI 13-204v3 AFMCSUP", "afi13-204v3_afmcsup_i.pdf"));
        majcom.add(new RegsList("AFI 13-204v3 AFSOCSUP", "afi13-204v3_afsocsup.pdf"));
        majcom.add(new RegsList("AFI 13-204v3 AMCSUP", "afi13-204v3_amcsup_i.pdf"));
        majcom.add(new RegsList("AFI 13-204v3 USAFESUP", "afi13-204v3_usafesup.pdf"));

        listDataChild.put(listDataHeader.get(0), afi);
        listDataChild.put(listDataHeader.get(1), ufc);
        listDataChild.put(listDataHeader.get(2), etl);
        listDataChild.put(listDataHeader.get(3), faa);
        listDataChild.put(listDataHeader.get(4), majcom);
    }

    public static class RegsList implements Serializable {
        String reg;
        String file;

        public RegsList(String reg, String file)
        {
            this.reg = reg;
            this.file = file;
        }

        public String getReg() {
            return reg;
        }

        public String getFile() {
            return file;
        }

    }
}
