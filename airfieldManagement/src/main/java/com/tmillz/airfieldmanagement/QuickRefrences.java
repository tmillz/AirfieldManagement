package com.tmillz.airfieldmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuickRefrences extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view =inflater.inflate(R.layout.quickrefrences, container, false);

		TextView textView1 = (TextView) view.findViewById(R.id.textView1);
		textView1.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView textView2 = (TextView) view.findViewById(R.id.textView2);
		textView2.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView textView3 = (TextView) view.findViewById(R.id.textView3);
		textView3.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView textView4 = (TextView) view.findViewById(R.id.textView4);
		textView4.setMovementMethod(LinkMovementMethod.getInstance());
		
		return view;
	}
	
	//@Override
	//public void onActivityCreated(Bundle savedInstanceState){
	//	super.onActivityCreated(savedInstanceState);
	//
	//}
}
