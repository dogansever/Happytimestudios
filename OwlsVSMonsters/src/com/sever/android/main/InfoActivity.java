package com.sever.android.main;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class InfoActivity extends Activity {
	private ArrayList<Integer> backgroundResources = new ArrayList<Integer>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_portrait);

		Button kapat = (Button) findViewById(R.id.button1);
		kapat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
		StickyHorizontalScrollView scrollView = (StickyHorizontalScrollView) layout.getChildAt(0);
		LinearLayout layout2 = (LinearLayout) scrollView.getChildAt(0);
		int cnt = layout2.getChildCount();
		while (cnt > 0) {
			cnt--;
			((ImageView) layout2.getChildAt(cnt)).setImageBitmap(null);
		}
		layout2.removeAllViews();
		scrollView.removeAllViews();
		layout.removeAllViews();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();

		backgroundResources.clear();
		backgroundResources.add(R.drawable.info_splash2);
		backgroundResources.add(R.drawable.info_splash1);
		backgroundResources.add(R.drawable.info_splash3);
		backgroundResources.add(R.drawable.info_splash4);
		backgroundResources.add(R.drawable.info_splash5);

		StickyHorizontalScrollView scrollView = new StickyHorizontalScrollView(this);

		LinearLayout layout2 = new LinearLayout(this);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setBackgroundColor(Color.TRANSPARENT);

		scrollView.addView(layout2);
		scrollView.setBackgroundColor(Color.TRANSPARENT);
		scrollView.setHorizontalFadingEdgeEnabled(false);

		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.removeAllViews();
		layout.addView(scrollView, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setBackgroundColor(Color.TRANSPARENT);

		ArrayList<ImageView> dayViews = new ArrayList<ImageView>();
		for (Integer backG : backgroundResources) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(backG);
			imageView.setLayoutParams(new RelativeLayout.LayoutParams(StartActivity.deviceWidth, StartActivity.deviceHeight));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			dayViews.add(imageView);
		}
		// add views to scroll view and set sticky positions
		int i = 0;
		int dayViewWidth = StartActivity.deviceWidth;
		for (View v : dayViews) {
			layout2.addView(v); // viewContainer is a LinearLayout within
								// scrollView
			scrollView.addStickyPosition(i++ * dayViewWidth); // set sticky
																// position
		}
	}
}