package com.sever.loc.main;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;

public class SavedLocListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ContentValues> locList;

	public SavedLocListAdapter(Context c) {
		// TODO Auto-generated constructor stub
		context = c;
		locList = LocationMeterActivity.dbWriteUtil.getLocList();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return locList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LinearLayout savedLoc = (LinearLayout) ((android.app.Activity) context).getLayoutInflater().inflate(R.layout.saved_loc, null);
		ContentValues cv = locList.get(arg0);
		final String idColumn = cv.getAsString(DBWriteUtil.idColumn);
		String info = cv.getAsString(DBWriteUtil.infoColumn);
		final String lat = cv.getAsString(DBWriteUtil.latColumn);
		final String lng = cv.getAsString(DBWriteUtil.longColumn);
		TextView textView = (TextView) savedLoc.findViewById(R.id.textLoc);
		textView.setText(info);
		Button button1 = (Button) savedLoc.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).setIcon(R.drawable.saved_loc_1).create();
				alertDialog.setTitle("Set Destination");
				// alertDialog.setCancelable(false);
				alertDialog.setMessage("Do you want to set your destination point?");
				alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						LocationMeterActivity.pointNext = new GeoPoint(Integer.parseInt(lat), Integer.parseInt(lng));
						((LocationMeterActivity) context).ds = null;
						((LocationMeterActivity) context).refreshPins();
						((LocationMeterActivity) context).onBackPressed();
					}
				});
				alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				alertDialog.show();

			}
		});
		Button button2 = (Button) savedLoc.findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).setIcon(R.drawable.bin_closed).create();
				alertDialog.setTitle("Delete Location");
				// alertDialog.setCancelable(false);
				alertDialog.setMessage("Are you sure to delete the point?");
				alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						LocationMeterActivity.dbWriteUtil.deleteLoc(idColumn);
						((LocationMeterActivity) context).showSavedLocList();
					}
				});
				alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				alertDialog.show();

			}
		});

		return savedLoc;
	}

}
