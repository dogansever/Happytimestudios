package com.example.ttwidgetdemo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

public class StackWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// update each of the widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			if (Build.VERSION.SDK_INT >= 11) {
				Intent intent = new Intent(context, StackWidgetService.class);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
				intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
				RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.stackwidgetlayout);
				rv.setEmptyView(R.id.stack_view, R.id.widget_item_NoItem);
				rv.setRemoteAdapter(R.id.stack_view, intent);

				appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
			}
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}