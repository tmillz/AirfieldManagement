package com.tmillz.airfieldmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuickRefrences extends Fragment {
	
	public QuickRefrences(){
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v =inflater.inflate(R.layout.quickrefrences, container, false);
		
		((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Links");
		
		TextView textView1 = (TextView) v.findViewById(R.id.textView1);
		textView1.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView textView2 = (TextView) v.findViewById(R.id.textView2);
		textView2.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView textView3 = (TextView) v.findViewById(R.id.textView3);
		textView3.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView textView4 = (TextView) v.findViewById(R.id.textView4);
		textView4.setMovementMethod(LinkMovementMethod.getInstance());
		
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
	}

}
