package com.sever.ramsandgoats.util;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class SpriteBmp {

	public int BMP_COLUMNS = 1;
	public int BMP_ROWS = 1;
	public int BMP_FPS = 2;
	public int BMP_FPS_CURRENT = 0;
	public ArrayList<Bitmap> bmpArray;
	public ArrayList<int[]> colsrows;
	public int bmpIndex = 0;
	public Bitmap bmpFrame;

	public int currentFrame = 0;
	public int currentRow = 0;
	public boolean repeat = true;

	public SpriteBmp(ArrayList<Bitmap> bmp, ArrayList<int[]> colsrows) {
		super();
		if (bmp.size() != colsrows.size()) {
			System.out.println("SpriteBmp input error");
			return;
		}

		this.bmpArray = bmp;
		this.colsrows = colsrows;
		BMP_COLUMNS = getBmpColumnS();
		BMP_ROWS = getBmpRowS();
	}

	private int getBmpRowS() {
		return colsrows.get(bmpIndex)[1];
	}

	private int getBmpColumnS() {
		return colsrows.get(bmpIndex)[0];
	}

	public void freeBitmaps() {
//		bmpArray = null;
//		bmpFrame = null;
//		bmpIndex = -1;
	}

	public float getWidth(int index) {
		return bmpArray.get(index).getWidth() / colsrows.get(index)[0];
	}

	public float getWidth() {
		return bmpArray.get(bmpIndex).getWidth() / BMP_COLUMNS;
	}

	public float getHeight() {
		return bmpArray.get(bmpIndex).getHeight() / BMP_ROWS;
	}

	public Bitmap getBitmap() {
		try {
			return bmpArray.get(bmpIndex);
		} catch (Exception e) {
			System.out.println("bmpArray:" + bmpArray);
			System.out.println("bmpIndex:" + bmpIndex);
			e.printStackTrace();
			return null;
		}
	}

	public void setBmpIndex(int bmpIndex) {
		this.bmpIndex = bmpIndex;
		BMP_COLUMNS = getBmpColumnS();
		BMP_ROWS = getBmpRowS();
	}
}
