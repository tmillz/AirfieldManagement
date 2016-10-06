package com.tmillz.airfieldmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.odk.collect.android.activities.SplashScreenActivity;

public class RandomList extends ListFragment {

    public RandomList(){
		super();
	}
	
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

    private String [] titleRes;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		setListAdapter(new MenuArrayAdapter(getActivity().getApplication(), list_contents));
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
        	 BaseActivity.switchContent(new Acft());
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
        	a= new Intent((BaseActivity).getBaseContext(), Discrepancies.class);
        	startActivity(a);
            break;
        case 7:
        	a= new Intent((BaseActivity).getBaseContext(), SplashScreenActivity.class);
        	startActivity(a);
        	break;
        case 8:
        	a = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.tmillz.airfieldmanagement"));
        	startActivity(a);
        	break;
        case 9:
			a = new Intent(Intent.ACTION_SEND);
			a.setType("message/rfc822");
			a.putExtra(Intent.EXTRA_EMAIL, new String[] {"terrymil1981@gmail.com"});
			a.putExtra(Intent.EXTRA_SUBJECT, "Airfield Management App");
			a.putExtra(Intent.EXTRA_TEXT, "Feedback:");
			startActivity(Intent.createChooser(a, "Send Email"));
            break;
        }
	}
}

	

