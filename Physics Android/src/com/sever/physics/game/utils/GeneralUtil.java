package com.sever.physics.game.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GeneralUtil {


	public static Bitmap createScaledBitmap(Context context,int decodeResource, int dstWidth, int dstHeight) {
		if (dstWidth <= 0 || dstHeight <= 0) {
			return BitmapFactory.decodeResource(context.getResources(), decodeResource);
		}
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), decodeResource), dstWidth, dstHeight, false);
	}
}
