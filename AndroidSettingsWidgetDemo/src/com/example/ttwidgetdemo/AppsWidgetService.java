package com.example.ttwidgetdemo;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class AppsWidgetService extends RemoteViewsService {
	public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
	public static ArrayList<String> appList = new ArrayList<String>();

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		System.out.println("onGetViewFactory:" + this);
		return new StackRemoteViewsFactory2(this.getApplicationContext(), intent);
	}
}

class StackRemoteViewsFactory2 implements RemoteViewsService.RemoteViewsFactory {
	private Context mContext;

	public StackRemoteViewsFactory2(Context context, Intent intent) {
		System.out.println("StackRemoteViewsFactory2:" + this);
		mContext = context;

	}

	public void onCreate() {
		System.out.println("onCreate:" + this);
		AppsWidgetService.appList.add("com.arneca.dergilik.main3x");
		AppsWidgetService.appList.add("com.arneca.backupRestore.main");
		AppsWidgetService.appList.add("com.sever.main");
		AppsWidgetService.appList.add("com.turkcell.kitaplik.main");
		AppsWidgetService.appList.add("com.arneca.dergilik.main.pdf");
		AppsWidgetService.appList.add("com.arneca.dergilik.main");
		AppsWidgetService.appList.add("com.arneca.dergilik.main");
		AppsWidgetService.appList.add("com.arneca.dergilik.main");
	}

	public void onDestroy() {
		System.out.println("onDestroy:" + this);
	}

	public int getCount() {
		return 8;
	}

	public RemoteViews getViewAt(int position) {
		System.out.println("Getting view at position:" + position);
		RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.appswidgetitem);

		try {
			rv.setImageViewBitmap(R.id.imageView1, StackRemoteViewsFactory.getBitmapFromAsset(mContext, "apps/apps0" + (position + 1) + ".png"));
			// Drawable d =
			// Drawable.createFromStream(mContext.getAssets().open("apps0" +
			// (position + 1) + ".png"), null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Bundle extras = new Bundle();
		extras.putString(AppsWidgetService.EXTRA_ITEM, "" + position);
		Intent fillInIntent = new Intent();
		fillInIntent.putExtras(extras);
		rv.setOnClickFillInIntent(R.id.imageView1, fillInIntent);

		return rv;
	}

	public RemoteViews getLoadingView() {
		RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_loading);
		return rv;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public long getItemId(int position) {
		return position;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void onDataSetChanged() {
		System.out.println("onDataSetChanged:" + this);
	}
}