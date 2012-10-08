package com.sever.loc.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private static final float DIST = 5;
	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private Context context;

	public CustomItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public CustomItemizedOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}

	@Override
	public boolean onTap(final GeoPoint p, MapView arg1) {
		// return super.onTap(arg0, arg1);
		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, 1);

			String add = "";
			if (addresses.size() > 0) {
				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
					add += addresses.get(0).getAddressLine(i) + "\n";
			}

			// Toast.makeText(context, add, Toast.LENGTH_SHORT).show();

			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Want to pin the selected point?");
			dialog.setMessage(add);
			dialog.setPositiveButton("Ok", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					LocationMeterActivity.pointNext = p;
					((LocationMeterActivity) context).ds = null;
					mapOverlays.clear();
					((LocationMeterActivity) context).refreshPins();
				}
			});
			dialog.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			dialog.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		((LocationMeterActivity) context).redrawFlyPath();
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// LocationMeterActivity.x = event.getX();
		// LocationMeterActivity.y = event.getY();
		// } else if (event.getAction() == MotionEvent.ACTION_UP) {
		// float xDist = Math.abs(LocationMeterActivity.x - event.getX());
		// float yDist = Math.abs(LocationMeterActivity.y - event.getY());
		// System.out.print(" xDist:" + xDist);
		// System.out.println(" yDist:" + yDist);
		// if (xDist > DIST || yDist > DIST) {
		// return true;
		// }
		//
		// final GeoPoint p = mapView.getProjection().fromPixels((int)
		// event.getX(), (int) event.getY());
		// Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		// try {
		// List<Address> addresses = geoCoder.getFromLocation(p.getLatitudeE6()
		// / 1E6, p.getLongitudeE6() / 1E6, 1);
		//
		// String add = "";
		// if (addresses.size() > 0) {
		// for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
		// add += addresses.get(0).getAddressLine(i) + "\n";
		// }
		//
		// // Toast.makeText(context, add, Toast.LENGTH_SHORT).show();
		//
		// AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		// dialog.setTitle("Want to pin the selected point?");
		// dialog.setMessage(add);
		// dialog.setPositiveButton("Ok", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// LocationMeterActivity.pointNext = p;
		// ((LocationMeterActivity) context).ds = null;
		// mapOverlays.clear();
		// ((LocationMeterActivity) context).refreshPins();
		// }
		// });
		// dialog.setNegativeButton("Cancel", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// }
		// });
		// dialog.show();
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return true;
		// } else
		// return false;
		return false;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mapOverlays.get(index);
		// AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		// dialog.setTitle(item.getTitle());
		// dialog.setMessage(item.getSnippet());
		// dialog.show();
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}

}
