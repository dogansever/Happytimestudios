package com.example.ttwidgetdemo;

import java.lang.reflect.Method;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class SettingsWidgetProvider extends AppWidgetProvider {
	public static final String ACTION_WIFI = "com.example.android.stackwidget.ACTION_WIDGET_1";
	public static final String ACTION_3G = "com.example.android.stackwidget.ACTION_WIDGET_2";
	public static final String ACTION_BRIGHTNESS = "com.example.android.stackwidget.ACTION_WIDGET_3";
	public static final String ACTION_GPS = "com.example.android.stackwidget.ACTION_WIDGET_4";
	public static final String ACTION_BLUETOOTH = "com.example.android.stackwidget.ACTION_WIDGET_5";
	public static final String ACTION_SYNC = "com.example.android.stackwidget.ACTION_WIDGET_6";
	static Intent intents;
	static PendingIntent pendingIntents;

	private static long updateperiodmillis = 2 * 1000;

	// Minimum size in dip = (Number of cells * 74dip) - 2dip
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// System.out.println("onUpdate:" + this);
		intents = new Intent(context, UpdateService.class);
		pendingIntents = PendingIntent.getService(context, 0, intents, 0);
		context.startService(new Intent(context, UpdateService.class));

	}

	public static class UpdateService extends Service {
		@Override
		public void onStart(Intent intent, int startId) {
			// System.out.println("onStart:" + this);
			RemoteViews updateViews = buildUpdate(this);
			ComponentName thisWidget = new ComponentName(this, SettingsWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, updateViews);
		}

		public RemoteViews buildUpdate(Context context) {
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.settingswidgetlayout);
			Intent toastIntent;
			toastIntent = new Intent(context, SettingsWidgetProvider.class);
			toastIntent.setAction(ACTION_WIFI);
			PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.imageView1, toastPendingIntent);
			rv.setImageViewResource(R.id.imageView1, isWirelessOn(context) ? R.drawable.wirelesson : R.drawable.wirelessoff);

			toastIntent = new Intent(context, SettingsWidgetProvider.class);
			toastIntent.setAction(ACTION_3G);
			toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.ImageView01, toastPendingIntent);
			rv.setImageViewResource(R.id.ImageView01, is3GOn(context) ? R.drawable.threegon : R.drawable.threegoff);

			toastIntent = new Intent(context, SettingsWidgetProvider.class);
			toastIntent.setAction(ACTION_BRIGHTNESS);
			toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.ImageView02, toastPendingIntent);
			rv.setImageViewResource(R.id.ImageView02, isBrightnessAuto(context) ? R.drawable.brightnesson : R.drawable.brightnessoff);

			toastIntent = new Intent(context, SettingsWidgetProvider.class);
			toastIntent.setAction(ACTION_GPS);
			toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.ImageView03, toastPendingIntent);
			rv.setImageViewResource(R.id.ImageView03, isGpsOn(context) ? R.drawable.gpson : R.drawable.gpsoff);

			toastIntent = new Intent(context, SettingsWidgetProvider.class);
			toastIntent.setAction(ACTION_BLUETOOTH);
			toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.ImageView04, toastPendingIntent);
			rv.setImageViewResource(R.id.ImageView04, isBluetoothOn(context) ? R.drawable.bluetoothon : R.drawable.bluetoothoff);

			toastIntent = new Intent(context, SettingsWidgetProvider.class);
			toastIntent.setAction(ACTION_SYNC);
			toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.ImageView05, toastPendingIntent);
			rv.setImageViewResource(R.id.ImageView05, isSyncOn(context) ? R.drawable.syncon : R.drawable.syncoff);

			// if (isWirelessRefreshing(context)) {
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC, System.currentTimeMillis() + updateperiodmillis, pendingIntents);
			// }
			return rv;
		}

		private boolean isSyncOn(Context context) {
			// TODO Auto-generated method stub
			return false;
		}

		private boolean isBluetoothOn(Context context) {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			return mBluetoothAdapter.isEnabled();
		}

		private boolean isGpsOn(Context context) {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			boolean enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
			if (enabled)
				return enabled;
			String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return provider.contains("gps");
		}

		private boolean isBrightnessAuto(Context context) {
			ContentResolver cr = context.getContentResolver();
			boolean autoBrightOn = (Settings.System.getInt(cr, SCREEN_BRIGHTNESS_MODE, -1) == SCREEN_MODE_AUTO);
			return autoBrightOn;
		}

		public boolean isWirelessOn(Context context) {
			WifiManager wifi;
			wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			return wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
		}

		private boolean is3GOn(Context context) {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED;
		}

		@Override
		public IBinder onBind(Intent arg0) {
			System.out.println("onBind:" + this);
			return null;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// System.out.println("onReceive:" + this);
		if (intent.getAction().equals(ACTION_WIFI)) {
			wirelessToggle(context);
			Toast.makeText(context, getWirelessStatus(context), Toast.LENGTH_SHORT).show();
			context.startService(new Intent(context, UpdateService.class));
		} else if (intent.getAction().equals(ACTION_3G)) {
			threeGToggle(context);
			Toast.makeText(context, get3GStatus(context), Toast.LENGTH_SHORT).show();
			context.startService(new Intent(context, UpdateService.class));
		} else if (intent.getAction().equals(ACTION_BRIGHTNESS)) {
			brightnessToggle(context);
			context.startService(new Intent(context, UpdateService.class));
		} else if (intent.getAction().equals(ACTION_GPS)) {
			gpsToggle(context);
			context.startService(new Intent(context, UpdateService.class));
		} else if (intent.getAction().equals(ACTION_BLUETOOTH)) {
			bluetoothToggle(context);
			context.startService(new Intent(context, UpdateService.class));
		} else if (intent.getAction().equals(ACTION_SYNC)) {
			syncToggle(context);
			context.startService(new Intent(context, UpdateService.class));
		}
		super.onReceive(context, intent);
	}

	private void syncToggle(Context context) {
		// TODO Auto-generated method stub

	}

	private void bluetoothToggle(Context context) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(context, "Enabling Bluetooth", Toast.LENGTH_SHORT).show();
			mBluetoothAdapter.enable();
		} else {
			Toast.makeText(context, "Disabling Bluetooth", Toast.LENGTH_SHORT).show();
			mBluetoothAdapter.disable();
		}
	}

	private boolean isGpsOn(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
		if (enabled)
			return enabled;
		String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		return provider.contains("gps");
	}

	private void gpsToggle(Context context) {
		if (isGpsOn(context)) {
			Toast.makeText(context, "Disabling GPS", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			intent.putExtra("enabled", false);
			context.sendBroadcast(intent);
		} else {
			Toast.makeText(context, "Enabling GPS", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			intent.putExtra("enabled", true);
			context.sendBroadcast(intent);
		}

		// String provider =
		// Settings.Secure.getString(context.getContentResolver(),
		// Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		// if (provider.contains("gps")) { // if gps is enabled
		// final Intent poke = new Intent();
		// poke.setClassName("com.android.settings",
		// "com.android.settings.widget.SettingsAppWidgetProvider");
		// poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		// poke.setData(Uri.parse("3"));
		// context.sendBroadcast(poke);
		// }
	}

	private static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
	private static final int SCREEN_MODE_MANUAL = 0;
	private static final int SCREEN_MODE_AUTO = 1;

	private void brightnessToggle(Context context) {
		try {
			ContentResolver cr = context.getContentResolver();
			boolean autoBrightOn = (Settings.System.getInt(cr, SCREEN_BRIGHTNESS_MODE, -1) == SCREEN_MODE_AUTO);
			if (autoBrightOn) {
				Settings.System.putInt(cr, SCREEN_BRIGHTNESS_MODE, SCREEN_MODE_MANUAL);
				Toast.makeText(context, "Disabling 'Automatic Brightness'", Toast.LENGTH_SHORT).show();
			} else {
				Settings.System.putInt(cr, SCREEN_BRIGHTNESS_MODE, SCREEN_MODE_AUTO);
				Toast.makeText(context, "Enabling 'Automatic Brightness'", Toast.LENGTH_SHORT).show();
			}

			int brightness = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
			Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS, brightness);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CharSequence get3GStatus(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String status = "....";
		switch (telephonyManager.getDataState()) {
		case TelephonyManager.DATA_CONNECTED:
			status = "Disabling 3G...";
			break;
		case TelephonyManager.DATA_CONNECTING:
			status = "Enabling 3G...";
			break;
		case TelephonyManager.DATA_DISCONNECTED:
			status = "Enabling 3G...";
			break;
		default:
			break;
		}
		return status;
	}

	private void threeGToggle(Context context) {
		try {
			Method dataConnSwitchmethod;
			Class telephonyManagerClass;
			Object ITelephonyStub;
			Class ITelephonyClass;

			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			boolean disable;
			if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
				disable = true;
			} else {
				disable = false;
			}

			telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
			Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
			getITelephonyMethod.setAccessible(true);
			ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
			ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

			if (disable) {
				dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
			} else {
				dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
			}
			dataConnSwitchmethod.setAccessible(true);
			dataConnSwitchmethod.invoke(ITelephonyStub);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CharSequence getWirelessStatus(Context context) {
		String status = "....";
		WifiManager wifi;
		wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		switch (wifi.getWifiState()) {
		case WifiManager.WIFI_STATE_ENABLED:
			status = "Disabling Wifi...";
			break;
		case WifiManager.WIFI_STATE_ENABLING:
			status = "Enabling Wifi...";
			break;
		case WifiManager.WIFI_STATE_DISABLED:
			status = "Enabling Wifi...";
			break;
		case WifiManager.WIFI_STATE_DISABLING:
			status = "Disabling Wifi...";
			break;

		default:
			break;
		}
		return status;
	}

	private void wirelessToggle(Context context) {
		WifiManager wifi;
		wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			wifi.setWifiEnabled(false);// Turn off Wifi
		} else if (wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
			wifi.setWifiEnabled(true);// Turn on Wifi
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		System.out.println("onDeleted:" + this);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		System.out.println("onDisabled:" + this);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		System.out.println("onEnabled:" + this);
	}

}