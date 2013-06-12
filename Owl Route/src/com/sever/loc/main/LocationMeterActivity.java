package com.sever.loc.main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class LocationMeterActivity extends MapActivity {
	public static MapController mapController;
	public static List<Overlay> mapOverlays;
	public static MapView mapView;

	// private static final int latitudeE6 = 37985339;
	// private static final int longitudeE6 = 23716735;
	public static final int DEFAULT_ZOOM_LEVEL = 14;
	public static int DEFAULT_ZOOM_LEVEL_CLOSE = 17;
	public static final int DEFAULT_ZOOM_LEVEL_15 = 15;
	public static final int DEFAULT_ZOOM_LEVEL_16 = 16;
	public static final int DEFAULT_ZOOM_LEVEL_17 = 17;
	public static final int SPEED_20 = 20;
	public static final int SPEED_50 = 50;
	public static final int SPEED_70 = 70;
	public static final int SPEED_100 = 100;
	public static GeoPoint pointCurrent = null;
	public static GeoPoint pointNext = null;
	public static float y;
	public static float x;
	protected boolean ZOOM_LOCK;
	protected static boolean MYLOCATION_LOCK;
	private LocationManager locationManager;
	private LocationListener listener;
	private Location lastKnownLocation;
	public final Handler mHandler = new Handler();
	public NavigationDataSet ds = null;
	private CustomItemizedOverlay pointCurrentOverlay;
	private CustomItemizedOverlay pointNextOverlay;
	private float routeDistance;
	// private MyLocationOverlay myLoc;
	protected boolean TAXI_LOCK;
	private float TAXI_DISTANCE;
	public GeoPoint pointPrevious;
	private TextView flyText;
	private TextView routeText;
	private TextView taxiText;
	public long timeCurrent;
	public long timePrevious;
	private double speed;
	private TextView speedText;
	public static DBWriteUtil dbWriteUtil;
	public static int deviceWidth;
	public static int deviceHeight;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {
		recallDeviceMetrics();
		setContentView(R.layout.flipper);
		dbWriteUtil = new DBWriteUtil(this);
		ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
		toggleButton1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ZOOM_LOCK = isChecked;
				if (ZOOM_LOCK) {
					makeText(R.drawable.zoom_on, "Auto lock on close zoom is on");
				} else {
					makeText(R.drawable.zoom_off, "Auto lock on close zoom is off");
				}
			}
		});
		ToggleButton toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
		toggleButton2.setChecked(true);
		MYLOCATION_LOCK = true;
		toggleButton2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MYLOCATION_LOCK = isChecked;
				if (MYLOCATION_LOCK) {
					makeText(R.drawable.myloc_on, "Auto lock on my location is on");
				} else {
					makeText(R.drawable.myloc_off, "Auto lock on my location is off");
				}
			}
		});

		ToggleButton toggleButton01 = (ToggleButton) findViewById(R.id.ToggleButton01);
		toggleButton01.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				TAXI_LOCK = isChecked;
				if (TAXI_LOCK) {
					TAXI_DISTANCE = 0;
					pointPrevious = null;
					refreshTaxiDistance();
					makeText(R.drawable.taxi_on, "Taxi mode is on");
				} else {
					makeText(R.drawable.taxi_off, "Taxi mode is off");
				}
			}
		});

		speedText = (TextView) findViewById(R.id.textView4);
		speedText.setText(" - km/h");
		flyText = (TextView) findViewById(R.id.textView3);
		flyText.setText(" - km");
		routeText = (TextView) findViewById(R.id.TextView02);
		routeText.setText(" - km");
		taxiText = (TextView) findViewById(R.id.TextView03);
		taxiText.setText(" - km");
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(false);
		mapView.setStreetView(true);
		mapOverlays = mapView.getOverlays();
		mapController = mapView.getController();
		mapController.setZoom(DEFAULT_ZOOM_LEVEL);
		// mapView.getZoomButtonsController().setAutoDismissed(false);
		mapView.getZoomButtonsController().setVisible(false);
		// myLoc = new CustomizedMyLocationOverlay(this, mapView);
		// mapOverlays.add(myLoc);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(false);

		listener = new GeoUpdateHandler();
		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 0, 0, listener);
		String provider = locationManager.getBestProvider(criteria, true);
		if (provider != null) {
			lastKnownLocation = locationManager.getLastKnownLocation(provider);
			if (lastKnownLocation != null) {
				int lat = (int) (lastKnownLocation.getLatitude() * 1E6);
				int lng = (int) (lastKnownLocation.getLongitude() * 1E6);
				pointCurrent = new GeoPoint(lat, lng);
				animateTo(pointCurrent);
				refreshPins();
				refreshTaxiDistance();
				refreshGroundSpeed();
				stopLoadingDialog();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// This verification should be done during onStart() because the system
		// calls
		// this method when the user returns to the activity, which ensures the
		// desired
		// location provider is enabled each time the activity resumes from the
		// stopped state.
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			enableLocationSettings();
			// Build an alert dialog here that requests that the user enable
			// the location services, then when the user clicks the "OK" button,
			// call enableLocationSettings()
		}
	}

	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	private void createSavedLocList() {
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setVisibility(View.VISIBLE);
		listView.setCacheColorHint(Color.TRANSPARENT);
		// searchListView.setItemsCanFocus(true);
		listView.setHorizontalScrollBarEnabled(false);
		listView.setVerticalScrollBarEnabled(false);
		listView.setAdapter(new SavedLocListAdapter(this));

	}

	protected void makeText(int imageResId, CharSequence textArg) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast, null);
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(imageResId);
		TextView text = (TextView) layout.findViewById(R.id.textToast);
		text.setText(textArg);
		Toast toast = new Toast(LocationMeterActivity.this);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	private void refreshTaxiDistance() {
		if (pointPrevious != null && pointCurrent != null) {
			Location locationA = new Location("point A");
			locationA.setLatitude(pointPrevious.getLatitudeE6() / 1E6);
			locationA.setLongitude(pointPrevious.getLongitudeE6() / 1E6);
			Location locationB = new Location("point B");
			locationB.setLatitude(pointCurrent.getLatitudeE6() / 1E6);
			locationB.setLongitude(pointCurrent.getLongitudeE6() / 1E6);

			TAXI_DISTANCE += locationA.distanceTo(locationB) / 1000;
		}
		if (TAXI_LOCK) {
			String distStrKm = String.format("%01.3f", TAXI_DISTANCE);
			taxiText.setText(distStrKm + " km");
		}
	}

	public void recallDeviceMetrics() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			LocationMeterActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// myLoc.disableCompass();
		// myLoc.disableMyLocation();
		locationManager.removeUpdates(listener);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// myLoc.enableCompass();
		// myLoc.enableMyLocation();
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
//		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, listener);
		createAd();
		animateTo(pointCurrent);
		// startLoadingDialog(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		destroyAd();
	}

	private AdView adView;

	private void createAd() {
		System.out.println("createAd");
		adView = new AdView(this, AdSize.BANNER, "a151a46bb65c264");
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout8ad);
		layout.removeAllViews();
		layout.addView(adView);
		adView.loadAd(new AdRequest());
	}

	private void destroyAd() {
		System.out.println("destroyAd");
		adView.destroy();
	}

	private void animateTo(GeoPoint point) {
		if (point != null) {
			Runnable r = new Runnable() {

				@Override
				public void run() {
					redrawFlyPath();
				}
			};
			mapController.animateTo(point, r);
		} else {
		}
	}

	@Override
	public void onNewIntent(Intent newIntent) {
		// TODO Auto-generated method stub
		super.onNewIntent(newIntent);
	}

	@Override
	protected int onGetMapDataSource() {
		// TODO Auto-generated method stub
		return super.onGetMapDataSource();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public class GeoUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			// System.out.print(" lat:" + lat);
			// System.out.println(" lng:" + lng);
			timePrevious = timeCurrent;
			timeCurrent = location.getTime();
			pointPrevious = pointCurrent;
			pointCurrent = new GeoPoint(lat, lng);
			refreshPins();
			refreshTaxiDistance();
			refreshGroundSpeed();
			stopLoadingDialog();
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public void refreshPins() {

		// mapOverlays.clear();
		// mapOverlays.add(myLoc);
		mapOverlays.remove(pointCurrentOverlay);
		mapOverlays.remove(pointNextOverlay);
		if (pointCurrent != null) {
			OverlayItem overlayitem = new OverlayItem(pointCurrent, "", "");
			pointCurrentOverlay = new CustomItemizedOverlay(this.getResources().getDrawable(R.drawable.pin), this);
			pointCurrentOverlay.addOverlay(overlayitem);
			mapOverlays.add(pointCurrentOverlay);
		}
		if (pointNext != null) {
			OverlayItem overlayitem = new OverlayItem(pointNext, "", "");
			pointNextOverlay = new CustomItemizedOverlay(this.getResources().getDrawable(R.drawable.pin2), this);
			pointNextOverlay.addOverlay(overlayitem);
			mapOverlays.add(pointNextOverlay);
		}
		calculateFlyDistance();
		redrawRoute();

		// mapView.invalidate();
	}

	public void refreshGroundSpeed() {
		if (pointPrevious != null && pointCurrent != null) {
			Location locationA = new Location("point A");
			locationA.setLatitude(pointPrevious.getLatitudeE6() / 1E6);
			locationA.setLongitude(pointPrevious.getLongitudeE6() / 1E6);

			Location locationB = new Location("point B");
			locationB.setLatitude(pointCurrent.getLatitudeE6() / 1E6);
			locationB.setLongitude(pointCurrent.getLongitudeE6() / 1E6);

			double distance = locationA.distanceTo(locationB);
			long timeDiff = (timeCurrent - timePrevious) / (60 * 60);
			speed = distance / timeDiff;
			String distStrKm;
			if (speed > 0 && speed < 500) {
				distStrKm = String.format("%01.0f", speed);
			} else {
				distStrKm = "-";
			}
			speedText.setText(distStrKm + " km/h");
		}
		if (speed < SPEED_20) {
			DEFAULT_ZOOM_LEVEL_CLOSE = DEFAULT_ZOOM_LEVEL_17;
		} else if (speed < SPEED_50) {
			DEFAULT_ZOOM_LEVEL_CLOSE = DEFAULT_ZOOM_LEVEL_16;
		} else if (speed < SPEED_70) {
			DEFAULT_ZOOM_LEVEL_CLOSE = DEFAULT_ZOOM_LEVEL_15;
		} else {
			DEFAULT_ZOOM_LEVEL_CLOSE = DEFAULT_ZOOM_LEVEL;
		}

		if (ZOOM_LOCK) {
			mapController.setZoom(DEFAULT_ZOOM_LEVEL_CLOSE);
		}

		if (MYLOCATION_LOCK) {
			animateTo(pointCurrent);
		}
	}

	public void calculateFlyDistance() {
		if (pointNext != null && pointCurrent != null) {
			Location locationA = new Location("point A");
			locationA.setLatitude(pointCurrent.getLatitudeE6() / 1E6);
			locationA.setLongitude(pointCurrent.getLongitudeE6() / 1E6);
			Location locationB = new Location("point B");
			locationB.setLatitude(pointNext.getLatitudeE6() / 1E6);
			locationB.setLongitude(pointNext.getLongitudeE6() / 1E6);
			double distance = locationA.distanceTo(locationB) / 1000;
			String distStrKm = String.format("%01.3f", distance);
			// Toast.makeText(this, "Fly Distance: " + distStrKm + " km",
			// Toast.LENGTH_LONG).show();
			flyText.setText(distStrKm + " km");
		}
		redrawFlyPath();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		redrawFlyPath();
		return super.onTouchEvent(event);
	}

	void redrawFlyPath() {
		LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		linearLayout3.removeAllViews();
		if (mapView.getZoomLevel() < DEFAULT_ZOOM_LEVEL) {
			LineView.STROKEWIDTH = 4;
		} else if (mapView.getZoomLevel() < DEFAULT_ZOOM_LEVEL_CLOSE) {
			LineView.STROKEWIDTH = 8;
		} else {
			LineView.STROKEWIDTH = 10;
		}
		if (pointNext != null && pointCurrent != null) {
			Projection projection = mapView.getProjection();
			Point p1 = new Point();
			projection.toPixels(pointCurrent, p1);
			Point p2 = new Point();
			projection.toPixels(pointNext, p2);
			LineView lineView = new LineView(this, p1, p2);
			linearLayout3.addView(lineView, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

	void redrawRoute() {
		if (pointNext != null && pointCurrent != null) {
			Thread t = new Thread() {
				public void run() {
					StringBuilder urlString = new StringBuilder();
					urlString.append("http://maps.google.com/maps?f=d&hl=en");
					urlString.append("&saddr=");
					urlString.append(Double.toString((double) pointCurrent.getLatitudeE6() / 1.0E6));
					urlString.append(",");
					urlString.append(Double.toString((double) pointCurrent.getLongitudeE6() / 1.0E6));
					urlString.append("&daddr=");// to
					urlString.append(Double.toString((double) pointNext.getLatitudeE6() / 1.0E6));
					urlString.append(",");
					urlString.append(Double.toString((double) pointNext.getLongitudeE6() / 1.0E6));
					urlString.append("&ie=UTF8&0&om=0&output=kml");

					try {
						if (ds == null) {
							URL url = new URL(urlString.toString());
							SAXParserFactory factory = SAXParserFactory.newInstance();
							SAXParser parser = factory.newSAXParser();
							XMLReader xmlreader = parser.getXMLReader();
							NavigationSaxHandler navSaxHandler = new NavigationSaxHandler();
							xmlreader.setContentHandler(navSaxHandler);
							InputSource is = new InputSource(url.openStream());
							xmlreader.parse(is);
							ds = navSaxHandler.getParsedData();

							final Runnable r = new Runnable() {
								public void run() {
									drawPath(ds, Color.parseColor("#add331"), mapView);

									String distStrKm = String.format("%01.3f", routeDistance / 1000);
									routeText.setText(distStrKm + " km");
								}
							};
							Thread t = new Thread() {
								public void run() {
									mHandler.post(r);
								}
							};
							t.start();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
		}
	}

	protected float getDistanceBetween(GeoPoint pointCurrent2, GeoPoint pointNext2) {
		float[] results = { 1.0f };
		double startLongitude = (double) pointCurrent2.getLongitudeE6() / 1.0E6;
		double startLatitude = (double) pointCurrent2.getLatitudeE6() / 1.0E6;
		double endLongitude = (double) pointNext2.getLongitudeE6() / 1.0E6;
		double endLatitude = (double) pointNext2.getLatitudeE6() / 1.0E6;
		Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
		return results[0];
	}

	public void drawPath(NavigationDataSet navSet, int color, MapView mMapView01) {
		routeDistance = 0;
		if (color == Color.parseColor("#add331"))
			color = Color.parseColor("#6C8715");
		Collection overlaysToAddAgain = new ArrayList();
		for (Iterator iter = mMapView01.getOverlays().iterator(); iter.hasNext();) {
			Object o = iter.next();
			if (!RouteOverlay.class.getName().equals(o.getClass().getName())) {
				overlaysToAddAgain.add(o);
			}
		}
		mMapView01.getOverlays().clear();
		mMapView01.getOverlays().addAll(overlaysToAddAgain);

		String path = navSet.getRoutePlacemark().getCoordinates();
		if (path != null && path.trim().length() > 0) {
			String[] pairs = path.trim().split(" ");
			String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude
													// lngLat[1]=latitude
													// lngLat[2]=height

			if (lngLat.length < 3)
				lngLat = pairs[1].split(","); // if first pair is not
												// transferred completely, take
												// seconds pair //TODO

			try {
				GeoPoint startGP = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));
				mMapView01.getOverlays().add(new RouteOverlay(startGP, startGP, 1));
				GeoPoint gp1;
				GeoPoint gp2 = startGP;

				for (int i = 1; i < pairs.length; i++) // the last one would be
														// crash
				{
					lngLat = pairs[i].split(",");

					gp1 = gp2;

					if (lngLat.length >= 2 && gp1.getLatitudeE6() != 0 && gp1.getLongitudeE6() != 0 && gp2.getLatitudeE6() != 0
							&& gp2.getLongitudeE6() != 0) {
						gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));

						if (gp2.getLatitudeE6() != 22200000) {
							mMapView01.getOverlays().add(new RouteOverlay(gp1, gp2, 2, color));
							routeDistance += getDistanceBetween(gp1, gp2);
						}
					}
				}
				mMapView01.getOverlays().add(new RouteOverlay(gp2, gp2, 3));
			} catch (NumberFormatException e) {
			}
		}
		mMapView01.setEnabled(true);
		mMapView01.invalidate();
	}

	@Override
	public void onBackPressed() {
		ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
		if (flipper.getDisplayedChild() == 0) {

		} else {
			flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.view_transition_in_right));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.view_transition_out_right));
			flipper.showPrevious();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "My Location").setIcon(R.drawable.myloc);
		menu.add(0, 2, 0, "Reset Zoom").setIcon(R.drawable.zoom);
		menu.add(0, 3, 0, "Remove Destination").setIcon(R.drawable.remove);
		menu.add(0, 4, 0, "Refresh Route").setIcon(R.drawable.refresh);
		menu.add(0, 7, 0, "Save My Location").setIcon(R.drawable.plus);
		menu.add(0, 8, 0, "List Saved Locations").setIcon(R.drawable.list);
		menu.add(0, 5, 0, "Map View").setIcon(R.drawable.map);
		menu.add(0, 6, 0, "Street View").setIcon(R.drawable.street);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			animateTo(pointCurrent);
			return true;
		case 2:
			mapController.setZoom(DEFAULT_ZOOM_LEVEL);
			redrawFlyPath();
			return true;
		case 3:
			pointNext = null;
			ds = null;
			mapOverlays.clear();
			refreshPins();
			return true;
		case 4:
			ds = null;
			mapOverlays.clear();
			refreshPins();
			return true;
		case 5:
			mapView.setSatellite(true);
			mapView.setStreetView(false);
			return true;
		case 6:
			mapView.setSatellite(false);
			mapView.setStreetView(true);
			return true;
		case 7:
			saveLoc();
			return true;
		case 8:
			showSavedLocList();
			return true;
		}
		return false;
	}

	public void showSavedLocList() {
		createSavedLocList();
		ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
		if (flipper.getDisplayedChild() == 0) {
			flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.view_transition_in_left));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.view_transition_out_left));
			flipper.showNext();
		}
	}

	private void saveLoc() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setIcon(R.drawable.plus).create();
		alertDialog.setTitle("Save Location");
		// alertDialog.setCancelable(false);
		alertDialog.setMessage("Do you want to save your current location?");
		final LinearLayout view = (LinearLayout) this.getLayoutInflater().inflate(R.layout.input, null);
		alertDialog.setView(view);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				EditText input = (EditText) view.findViewById(R.id.editText1);
				String infoColumn = input.getText().toString();
				String longColumn = "" + pointCurrent.getLongitudeE6();
				String latColumn = "" + pointCurrent.getLatitudeE6();
				dbWriteUtil.addLoc(latColumn, longColumn, infoColumn);
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.show();
	}

	protected static ProgressDialog pd;

	public void startLoadingDialog(final Context c) {
		stopLoadingDialog();
		Thread t2 = new Thread() {
			public void run() {
				try {
					Looper.prepare();
					pd = new ProgressDialog(c);
					pd.setCancelable(false);
					pd.setMessage("Searching for location...");
					pd.show();
					Looper.loop();
				} catch (Exception e) {
				}
			}
		};
		t2.start();
	}

	public void stopLoadingDialog() {
		if (pd != null) {
			pd.dismiss();
			animateTo(pointCurrent);
		}
		pd = null;
	}
}