package com.tmillz.airfieldmanagement;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ListFragment;
//import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.vending.expansion.zipfile.APKExpansionSupport;
import com.google.android.vending.expansion.zipfile.ZipResourceFile;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.ListFragment;


import org.apache.commons.io.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegsSub extends ListFragment {

	private List<String> fileList;
	ZipResourceFile expansionFile;

	public static RegsSub newInstance() {
		return new RegsSub();
	}

	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		// final Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

		ArrayList<Regulations.RegsList> RegulationsSubList = (ArrayList<Regulations.RegsList>)
				Objects.requireNonNull(getArguments()).getSerializable("Regs");

		List<String> list = RegulationsSubList.stream().map(item -> item.getReg())
				.collect(Collectors.toList());

		fileList = RegulationsSubList.stream().map(item -> item.getFile())
				.collect(Collectors.toList());

		ListAdapter listAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
				R.layout.regulations_list_item, R.id.RegName, list);

		setListAdapter(listAdapter);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		// Setting title in the wrong place
		// ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
		// Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
		// actionBar.setTitle("Test");

		return inflater.inflate(R.layout.regs_sub_list, null);
	}

	@Override
	public void onViewCreated (@NonNull View view, Bundle savedInstanceState) {
		ListView listView = getListView();
		final Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

		//get expansion apk
		try {
			expansionFile = APKExpansionSupport.getAPKExpansionZipFile(context, 24, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		listView.setOnItemClickListener((parent, view1, position, id) -> {


			String filename = fileList.get(position);

			// get input stream of file and open it
			try {
				InputStream inputStream = expansionFile.getInputStream(filename);
				File file = new File(context.getCacheDir(), filename);
				OutputStream outputStream = new FileOutputStream(file);
				//IOUtils.copy(inputStream, outputStream);
				IOUtil.copy(inputStream, outputStream);
				outputStream.close();
				Intent pdfIntent = new Intent(Intent.ACTION_VIEW,
						FileProvider.getUriForFile(context,
								context.getPackageName() +
										".provider", file));
				pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				try {
					startActivity(pdfIntent);
				}
				catch(ActivityNotFoundException e) {
					Toast.makeText(getContext(), "No Application available to view pdf",
							Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		});
	}
}