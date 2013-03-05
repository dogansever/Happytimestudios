package com.example.ttwidgetdemo;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		System.out.println("onGetViewFactory:" + this);
		return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
	}

}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private Context mContext;

	// private int mAppWidgetId;

	public StackRemoteViewsFactory(Context context, Intent intent) {
		System.out.println("StackRemoteViewsFactory:" + this);
		mContext = context;
		// mAppWidgetId =
		// intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
		// AppWidgetManager.INVALID_APPWIDGET_ID);

	}

	public void onCreate() {
		System.out.println("onCreate:" + this);
	}

	public void onDestroy() {
		System.out.println("onDestroy:" + this);
	}

	public int getCount() {
		return 5;
	}

	public RemoteViews getViewAt(int position) {
		RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
		Uri uri = Uri.parse("http://www.medyafaresi.com/f1/aa4_4207f.jpg");
		rv.setImageViewBitmap(R.id.widget_item_imageView, getBitmapFromAsset(mContext, "tt0" + (position + 1) + ".png"));

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
		System.out.println("onDataSetChanged önerilenler");
	}

	public static Bitmap getBitmapFromAsset(Context context, String strName) {
		AssetManager assetManager = context.getAssets();

		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(strName);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			return null;
		}

		return bitmap;
	}
}