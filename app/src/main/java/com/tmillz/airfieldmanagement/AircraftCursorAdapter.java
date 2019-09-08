package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class AircraftCursorAdapter extends ResourceCursorAdapter {

	AircraftCursorAdapter(Context context, Cursor cursor) {
		super(context, R.layout.aircraft_specs, cursor, 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor arg1) {

		String picID = arg1.getString(arg1.getColumnIndex("pic"));

        int id = context.getResources().getIdentifier(
				"com.tmillz.airfieldmanagement:drawable/" + picID, null,
				null);
		
		ImageView pic = view.findViewById(R.id.imageView1);
		pic.setImageResource(id);
		
		TextView wingSpan = view.findViewById(R.id.wingSpan);

		wingSpan.setText(context.getString(R.string.wingspan,
				arg1.getString(arg1.getColumnIndex("wing_span"))));
		
		TextView length = view.findViewById(R.id.length);
		length.setText(context.getString(R.string.length,
				arg1.getString(arg1.getColumnIndex("length"))));
		
		TextView height = view.findViewById(R.id.height);
		height.setText(context.getString(R.string.height,
				arg1.getString(arg1.getColumnIndex("height"))));
		
		TextView vert_clr = view.findViewById(R.id.vertClearance);
		vert_clr.setText(context.getString(R.string.vertical_clearance,
				arg1.getString(arg1.getColumnIndex("Vert_clr"))));
		
		TextView max_to_wt = view.findViewById(R.id.maxWeight);
		max_to_wt.setText(context.getString(R.string.max_to_weight,
				arg1.getString(arg1.getColumnIndex("max_to_wt"))));
		
		TextView basic_empty_wt = view.findViewById(R.id.basic_empty_wt);
		basic_empty_wt.setText(context.getString(R.string.basic_empty_wt,
				arg1.getString(arg1.getColumnIndex("basic_empty_wt"))));
		
		TextView turn_radius = view.findViewById(R.id.turn_radius);
		turn_radius.setText(context.getString(R.string.turning_radius,
				arg1.getString(arg1.getColumnIndex("turn_radius"))));
		
		TextView turn_diam = view.findViewById(R.id.turn_diam);
		turn_diam.setText(context.getString(R.string.one_eighty_degree_turn,
				arg1.getString(arg1.getColumnIndex(
						"turn_diameter"))));
		
		TextView acn_max_wt = view.findViewById(R.id.acnMaxWeight);
		acn_max_wt.setText(context.getString(R.string.k,
				arg1.getString(arg1.getColumnIndex("acn_max_weight"))));
		
		TextView max_rigid_a = view.findViewById(R.id.rigidMaxA);
		max_rigid_a.setText(arg1.getString(arg1.getColumnIndex("max_rigid_a")));
		
		TextView max_rigid_b = view.findViewById(R.id.rigidMaxB);
		max_rigid_b.setText(arg1.getString(arg1.getColumnIndex("max_rigid_b")));
		
		TextView max_rigid_c = view.findViewById(R.id.rigidMaxC);
		max_rigid_c.setText(arg1.getString(arg1.getColumnIndex("max_rigid_c")));
		
		TextView max_rigid_d = view.findViewById(R.id.rigidMaxD);
		max_rigid_d.setText(arg1.getString(arg1.getColumnIndex("max_rigid_d")));
		
		TextView acn_wt_min = view.findViewById(R.id.acnMinWeight);
		acn_wt_min.setText(context.getString(R.string.k,
				arg1.getString(arg1.getColumnIndex("acn_weight_min"))));
		
		TextView rigid_a = view.findViewById(R.id.rigidMinA);
		rigid_a.setText(arg1.getString(arg1.getColumnIndex("rigid_a")));
		
		TextView rigid_b = view.findViewById(R.id.rigidMinB);
		rigid_b.setText(arg1.getString(arg1.getColumnIndex("rigid_b")));
		
		TextView rigid_c = view.findViewById(R.id.rigidMinC);
		rigid_c.setText(arg1.getString(arg1.getColumnIndex("rigid_c")));
		
		TextView rigid_d = view.findViewById(R.id.rigidMinD);
		rigid_d.setText(arg1.getString(arg1.getColumnIndex("rigid_d")));

		// Need to double check the Column indext
		TextView flexMaxWeight = view.findViewById(R.id.flexMaxWeight);
		flexMaxWeight.setText(context.getString(R.string.k,
				arg1.getString(arg1.getColumnIndex("acn_max_weight"))));

		TextView max_flex_a = view.findViewById(R.id.flexMaxA);
		max_flex_a.setText(arg1.getString(arg1.getColumnIndex("max_flex_a")));
		
		TextView max_flex_b = view.findViewById(R.id.flexMaxB);
		max_flex_b.setText(arg1.getString(arg1.getColumnIndex("max_flex_b")));
		
		TextView max_flex_c = view.findViewById(R.id.flexMaxC);
		max_flex_c.setText(arg1.getString(arg1.getColumnIndex("max_flex_c")));
		
		TextView max_flex_d = view.findViewById(R.id.flexMaxD);
		max_flex_d.setText(arg1.getString(arg1.getColumnIndex("max_flex_d")));
		
		TextView flexMinWeight = view.findViewById(R.id.flexMinWeight);
		flexMinWeight.setText(context.getString(R.string.k,
				arg1.getString(arg1.getColumnIndex("acn_weight_min"))));
		
		TextView flex_a = view.findViewById(R.id.flexMinA);
		flex_a.setText(arg1.getString(arg1.getColumnIndex("flex_a")));
		
		TextView flex_b = view.findViewById(R.id.flexMinB);
		flex_b.setText(arg1.getString(arg1.getColumnIndex("flex_b")));
		
		TextView flex_c = view.findViewById(R.id.flexMinC);
		flex_c.setText(arg1.getString(arg1.getColumnIndex("flex_c")));
		
		TextView flex_d = view.findViewById(R.id.flexMinD);
		flex_d.setText(arg1.getString(arg1.getColumnIndex("flex_d")));
	}
}
