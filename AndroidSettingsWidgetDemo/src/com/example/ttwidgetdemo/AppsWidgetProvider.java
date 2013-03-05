package com.example.ttwidgetdemo;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

public class AppsWidgetProvider extends AppWidgetProvider {
	public static final String ACTION_WIDGET_1 = "com.example.android.stackwidget.ACTION_WIDGET_1";
	public static final String ACTION_WIDGET_2 = "com.example.android.stackwidget.ACTION_WIDGET_2";
	public static final String ACTION_WIDGET_3 = "com.example.android.stackwidget.ACTION_WIDGET_3";
	public static final String ACTION_WIDGET_4a = "com.example.android.stackwidget.ACTION_WIDGET_4a";
	public static final String ACTION_WIDGET_4b = "com.example.android.stackwidget.ACTION_WIDGET_4b";
	public static final String ACTION_WIDGET_4c = "com.example.android.stackwidget.ACTION_WIDGET_4c";
	public static final String ACTION_WIDGET_4d = "com.example.android.stackwidget.ACTION_WIDGET_4d";
	public static final String ACTION_WIDGET_1a = "com.example.android.stackwidget.ACTION_WIDGET_1a";
	public static final String ACTION_WIDGET_2a = "com.example.android.stackwidget.ACTION_WIDGET_2a";
	public static final String ACTION_WIDGET_3a = "com.example.android.stackwidget.ACTION_WIDGET_3a";
	public static final String EXTRA_ITEM1 = "com.example.android.stackwidget.EXTRA_ITEM1";
	public static final String EXTRA_ITEM11 = "com.example.android.stackwidget.EXTRA_ITEM11";
	public static final String EXTRA_ITEM12 = "com.example.android.stackwidget.EXTRA_ITEM12";
	public static final String EXTRA_ITEM2 = "com.example.android.stackwidget.EXTRA_ITEM2";
	public static final String EXTRA_ITEM21 = "com.example.android.stackwidget.EXTRA_ITEM21";
	public static final String EXTRA_ITEM22 = "com.example.android.stackwidget.EXTRA_ITEM22";
	public static final String EXTRA_ITEM3 = "com.example.android.stackwidget.EXTRA_ITEM3";
	public static final String EXTRA_ITEM31 = "com.example.android.stackwidget.EXTRA_ITEM31";
	public static final String EXTRA_ITEM32 = "com.example.android.stackwidget.EXTRA_ITEM32";
	public static final String EXTRA_ITEM1a = "com.example.android.stackwidget.EXTRA_ITEM1a";
	public static final String EXTRA_ITEM11a = "com.example.android.stackwidget.EXTRA_ITEM11a";
	public static final String EXTRA_ITEM12a = "com.example.android.stackwidget.EXTRA_ITEM12a";
	public static final String EXTRA_ITEM2a = "com.example.android.stackwidget.EXTRA_ITEM2a";
	public static final String EXTRA_ITEM21a = "com.example.android.stackwidget.EXTRA_ITEM21a";
	public static final String EXTRA_ITEM22a = "com.example.android.stackwidget.EXTRA_ITEM22a";
	public static final String EXTRA_ITEM3a = "com.example.android.stackwidget.EXTRA_ITEM3a";
	public static final String EXTRA_ITEM31a = "com.example.android.stackwidget.EXTRA_ITEM31a";
	public static final String EXTRA_ITEM32a = "com.example.android.stackwidget.EXTRA_ITEM32a";
	public static AppsWidgetProvider Widget = null;
	public static Context context;
	public static AppWidgetManager appWidgetManager;
	public static int appWidgetIds[];
	private static long updateperiodmillis = 10 * 1000;

	// Minimum size in dip = (Number of cells * 74dip) - 2dip
	@Override
	public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		System.out.println("onUpdate:appWidgetIds.length:" + appWidgetIds.length);
		this.appWidgetIds = appWidgetIds;
		intents = new Intent(ctxt, UpdateService.class);
		pendingIntents = PendingIntent.getService(ctxt, 0, intents, 0);
		ctxt.startService(new Intent(ctxt, UpdateService.class));

	}

	static Intent intents;
	static PendingIntent pendingIntents;

	public static class UpdateService extends Service {
		@Override
		public void onStart(Intent intent, int startId) {
			System.out.println("onStart:" + this);
			RemoteViews updateViews = buildUpdate(this);
			ComponentName thisWidget = new ComponentName(this, AppsWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);

			for (int i = 0; i < appWidgetIds.length; i++) {
				manager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.gridView2);
			}
			manager.updateAppWidget(thisWidget, updateViews);

		}

		@TargetApi(14)
		public RemoteViews buildUpdate(Context ctxt) {
			System.out.println("buildUpdate:");
			// AlarmManager am = (AlarmManager)
			// ctxt.getSystemService(Context.ALARM_SERVICE);
			// am.set(AlarmManager.RTC, System.currentTimeMillis() +
			// updateperiodmillis, pendingIntents);

			RemoteViews widget = new RemoteViews(ctxt.getPackageName(), R.layout.appswidgetlayout);
			Intent svcIntent = new Intent(ctxt, AppsWidgetService.class);
			svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
			widget.setRemoteAdapter(R.id.gridView2, svcIntent);
			Intent clickIntent = new Intent(ctxt, AppsTriggerWidgetActivity.class);
			PendingIntent clickPI = PendingIntent.getActivity(ctxt, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			widget.setPendingIntentTemplate(R.id.gridView2, clickPI);

			return widget;

		}

		public void onReceive(Context context, Intent intent) {
			System.out.println("onReceive:" + this);
		}

		@Override
		public IBinder onBind(Intent arg0) {
			System.out.println("onBind:" + this);
			return null;
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