package com.tmillz.airfieldmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MenuList extends ListFragment {
	
	static final String[] list_contents = new String [] {
		"Regulations",
		"Aircraft",
		"Calculators",
		"NOTAMs",
		"BowMonk Converter",
		"Links",
		"Map",
		"Forms",
		"Rate App",
		"Email Developer"	
	};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new MenuArrayAdapter(getActivity(), list_contents));
	}
     
	public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id); 
        BaseActivity BaseActivity = (BaseActivity)getActivity();

        Intent a;
        switch(position){
        
        case 0:
        	 BaseActivity.switchContent(new MainView());
			 //titleRes = Regulations;
			break;
        case 1:
        	 BaseActivity.switchContent(new Aircraft());
        	 break;
        case 2:
        	 BaseActivity.switchContent(new Calculators());
        	 break;
        case 3:
	        a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notams.faa.gov"));
	     	startActivity(a);
	        break;
        case 4:
        	BaseActivity.switchContent(new Bowmonk());
            break;
        case 5:
        	BaseActivity.switchContent(new QuickRefrences());
            break;
        case 6:
        	//a= new Intent((BaseActivity).getBaseContext(), Discrepancies.class);
        	//startActivity(a);
			BaseActivity.switchContent(new ViewPagerFragment());
            break;
        case 7:
        	BaseActivity.switchContent(new Forms());
        	break;
        case 8:
        	a = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.tmillz.airfieldmanagement"));
        	startActivity(a);
        	break;
        case 9:
			a = new Intent(Intent.ACTION_SEND);
			a.setType("message/rfc822");
			a.putExtra(Intent.EXTRA_EMAIL, new String[] {"terrymil1981@gmail.com"});
			a.putExtra(Intent.EXTRA_SUBJECT, "Airfield Management App Android");
			a.putExtra(Intent.EXTRA_TEXT, "Android");
			startActivity(Intent.createChooser(a, "Send Email"));
            break;
        }
	}
}

	

