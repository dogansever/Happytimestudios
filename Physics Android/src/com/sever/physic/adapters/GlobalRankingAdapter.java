package com.sever.physic.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playtomic.android.api.PlaytomicScore;
import com.sever.physic.IntroActivity;
import com.sever.physic.R;
import com.sever.physics.game.utils.LeaderBoardUtil;

public class GlobalRankingAdapter extends BaseAdapter {
	private int MAX_PAGE = 50;
	private Context myContext;

	public GlobalRankingAdapter(Context c) {
		this.myContext = c;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout contentListLayout = (LinearLayout) ((android.app.Activity) myContext).getLayoutInflater().inflate(R.layout.introsubglobalrankinglistitem, null);
		TextView t = (TextView) contentListLayout.findViewById(R.id.textView1);
		t.setTypeface(IntroActivity.tf);
		t.setTextSize(20);
		t.setBackgroundColor(Color.TRANSPARENT);
		if (LeaderBoardUtil.scoreList == null || LeaderBoardUtil.scoreList.size() <= position) {
			t.setText((position + 1) + ") 0");
			t = (TextView) contentListLayout.findViewById(R.id.textView2);
			t.setBackgroundColor(Color.TRANSPARENT);
			t.setTextSize(20);
			t.setTypeface(IntroActivity.tf);
			t.setText("owly ");

		} else {
			PlaytomicScore p = LeaderBoardUtil.scoreList.get(position);
			String[] vals = p.getName().split("-");
			t.setText(p.getRank() + ") " + p.getPoints());
			t = (TextView) contentListLayout.findViewById(R.id.textView2);
			t.setBackgroundColor(Color.TRANSPARENT);
			t.setTextSize(20);
			t.setTypeface(IntroActivity.tf);
			// t.setText(p.getCustomData().get("playerName") + " Stage(" +
			// p.getCustomData().get("stage") + ") ");
			t.setText(vals[1] + ", Stage " + p.getCustomData().get("stage") + " ");

		}
		contentListLayout.setBackgroundColor(Color.TRANSPARENT);
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
