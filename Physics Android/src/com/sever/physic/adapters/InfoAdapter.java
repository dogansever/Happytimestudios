package com.sever.physic.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sever.physic.IntroActivity;
import com.sever.physic.R;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class InfoAdapter extends BaseAdapter {
	private int MAX_PAGE = WeaponsManager.getManager().getWTListForPlayer().size();;
	private Context myContext;

	public InfoAdapter(Context c) {
		this.myContext = c;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		WeaponTypes wt = WeaponsManager.getManager().getWTListForPlayer().get(position);
		LinearLayout contentListLayout = (LinearLayout) ((android.app.Activity) myContext).getLayoutInflater().inflate(R.layout.introsubinfolistitem, null);
		TextView t = (TextView) contentListLayout.findViewById(R.id.textView1);
		t.setTypeface(IntroActivity.tf);
		t.setTextSize(20);
		t.setText(WeaponsManager.getManager().getWTInfo(wt) + (WeaponsManager.getManager().getWTUnlockLevel(wt) > 0 ? ". Unlock at Stage#" + WeaponsManager.getManager().getWTUnlockLevel(wt) : ""));
		ImageView iw = (ImageView) contentListLayout.findViewById(R.id.imageView1);
		iw.setImageResource(WeaponsManager.getWTBitmap(wt));
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
