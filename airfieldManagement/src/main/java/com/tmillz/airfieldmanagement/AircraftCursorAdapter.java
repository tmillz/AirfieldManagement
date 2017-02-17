package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class AircraftCursorAdapter extends ResourceCursorAdapter {

	AircraftCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
		super(context, layout, cursor, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor arg1) {
		
		String ft = "ft";
		String k = "K Lbs";

		String picID = arg1.getString(arg1.getColumnIndex("pic"));
		
		int id = context.getResources().getIdentifier("com.tmillz.airfieldmanagement:drawable/" + picID, null, null);
		
		ImageView pic = (ImageView) view.findViewById(R.id.imageView1);
		pic.setImageResource(id);
		
		TextView wingSpan = (TextView) view.findViewById(R.id.wingSpan);
		wingSpan.setText("Wingspan: " + arg1.getString(arg1.getColumnIndex("wing_span")) + ft);
		
		TextView length = (TextView) view.findViewById(R.id.length);
		length.setText("Length: " + arg1.getString(arg1.getColumnIndex("length")) + ft);
		
		TextView height = (TextView) view.findViewById(R.id.height);
		height.setText("Height: " + arg1.getString(arg1.getColumnIndex("height")) + ft);
		
		TextView vert_clr = (TextView) view.findViewById(R.id.vertClearance);
		vert_clr.setText("Vertical Clearance: " + arg1.getString(arg1.getColumnIndex("Vert_clr")) + ft);
		
		TextView max_to_wt = (TextView) view.findViewById(R.id.maxWeight);
		max_to_wt.setText("Max T/O Weight: " + arg1.getString(arg1.getColumnIndex("max_to_wt")) + k);
		
		TextView basic_empty_wt = (TextView) view.findViewById(R.id.basic_empty_wt);
		basic_empty_wt.setText("Basic Empty Wt.: " + arg1.getString(arg1.getColumnIndex("basic_empty_wt")) + k);
		
		TextView turn_radius = (TextView) view.findViewById(R.id.turn_radius);
		turn_radius.setText("Turning Radius: " + arg1.getString(arg1.getColumnIndex("turn_radius")) + ft);
		
		TextView turn_diam = (TextView) view.findViewById(R.id.turn_diam);
		turn_diam.setText("180" + "\u00b0" + " Turn Diam: " + arg1.getString(arg1.getColumnIndex("turn_diameter")) + ft);
		
		TextView acn_max_wt = (TextView) view.findViewById(R.id.acnMaxWeight);
		acn_max_wt.setText(arg1.getString(arg1.getColumnIndex("acn_max_weight")) + k);
		
		TextView max_rigid_a = (TextView) view.findViewById(R.id.rigidMaxA);
		max_rigid_a.setText(arg1.getString(arg1.getColumnIndex("max_rigid_a")));
		
		TextView max_rigid_b = (TextView) view.findViewById(R.id.rigidMaxB);
		max_rigid_b.setText(arg1.getString(arg1.getColumnIndex("max_rigid_b")));
		
		TextView max_rigid_c = (TextView) view.findViewById(R.id.rigidMaxC);
		max_rigid_c.setText(arg1.getString(arg1.getColumnIndex("max_rigid_c")));
		
		TextView max_rigid_d = (TextView) view.findViewById(R.id.rigidMaxD);
		max_rigid_d.setText(arg1.getString(arg1.getColumnIndex("max_rigid_d")));
		
		TextView acn_wt_min = (TextView) view.findViewById(R.id.acnMinWeight);
		acn_wt_min.setText(arg1.getString(arg1.getColumnIndex("acn_weight_min")) + k);
		
		TextView rigid_a = (TextView) view.findViewById(R.id.rigidMinA);
		rigid_a.setText(arg1.getString(arg1.getColumnIndex("rigid_a")));
		
		TextView rigid_b = (TextView) view.findViewById(R.id.rigidMinB);
		rigid_b.setText(arg1.getString(arg1.getColumnIndex("rigid_b")));
		
		TextView rigid_c = (TextView) view.findViewById(R.id.rigidMinC);
		rigid_c.setText(arg1.getString(arg1.getColumnIndex("rigid_c")));
		
		TextView rigid_d = (TextView) view.findViewById(R.id.rigidMinD);
		rigid_d.setText(arg1.getString(arg1.getColumnIndex("rigid_d")));
		
		TextView flexMaxWeight = (TextView) view.findViewById(R.id.flexMaxWeight);
		flexMaxWeight.setText(arg1.getString(arg1.getColumnIndex("acn_max_weight")) + k);
		
		TextView max_flex_a = (TextView) view.findViewById(R.id.flexMaxA);
		max_flex_a.setText(arg1.getString(arg1.getColumnIndex("max_flex_a")));
		
		TextView max_flex_b = (TextView) view.findViewById(R.id.flexMaxB);
		max_flex_b.setText(arg1.getString(arg1.getColumnIndex("max_flex_b")));
		
		TextView max_flex_c = (TextView) view.findViewById(R.id.flexMaxC);
		max_flex_c.setText(arg1.getString(arg1.getColumnIndex("max_flex_c")));
		
		TextView max_flex_d = (TextView) view.findViewById(R.id.flexMaxD);
		max_flex_d.setText(arg1.getString(arg1.getColumnIndex("max_flex_d")));
		
		TextView flexMinWeight = (TextView) view.findViewById(R.id.flexMinWeight);
		flexMinWeight.setText(arg1.getString(arg1.getColumnIndex("acn_weight_min")) + k);
		
		TextView flex_a = (TextView) view.findViewById(R.id.flexMinA);
		flex_a.setText(arg1.getString(arg1.getColumnIndex("flex_a")));
		
		TextView flex_b = (TextView) view.findViewById(R.id.flexMinB);
		flex_b.setText(arg1.getString(arg1.getColumnIndex("flex_b")));
		
		TextView flex_c = (TextView) view.findViewById(R.id.flexMinC);
		flex_c.setText(arg1.getString(arg1.getColumnIndex("flex_c")));
		
		TextView flex_d = (TextView) view.findViewById(R.id.flexMinD);
		flex_d.setText(arg1.getString(arg1.getColumnIndex("flex_d")));
	}
}
