package com.sever.physic.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sever.physic.R;

public class GlobalRankingAdapter extends BaseAdapter {
	private int MAX_PAGE = 20;
	private Context myContext;

	public GlobalRankingAdapter(Context c) {
		this.myContext = c;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout contentListLayout = (LinearLayout) ((android.app.Activity) myContext).getLayoutInflater().inflate(R.layout.introsubglobalrankinglistitem, null);
		TextView t = (TextView) contentListLayout.findViewById(R.id.textView1);
		t.setText("score");
		t = (TextView) contentListLayout.findViewById(R.id.textView2);
		t.setText("username");
		return contentListLayout;
	}

	@Override
	public int getCount() {
		return MAX_PAGE;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}
}
