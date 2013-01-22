package com.sever.physics.game.sprites;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class StagePassBarSprite extends FreeSprite {

	private float percentagePrev;

	public StagePassBarSprite(GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
	}

	public void onDraw(Canvas canvas) {
		spriteBmp.currentRow = 0;

		float percentage = ((float) Constants.scoreStage) / ((float) Constants.scoreToPassTheStage);
		if (percentage == 0) {
			percentagePrev = 0;
		}
		if (percentage <= percentagePrev) {
			percentagePrev = percentage;
		} else {
			percentage = percentagePrev + (percentage - percentagePrev) * 0.1f;
			percentagePrev = percentage;
		}

		spriteBmp.currentFrame = 0;
		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) (width), (int) height);

		Matrix m = new Matrix();
		Vec2 translate = getBitmapDrawingXY();
		m.postTranslate(translate.x, translate.y);
		canvas.drawBitmap(spriteBmp.bmpFrame, m, null);

		spriteBmp.currentFrame = 1;
		srcX = (int) (spriteBmp.currentFrame * width);
		srcY = (int) (spriteBmp.currentRow * height);
		src = new Rect(srcX, srcY, (int) (srcX + percentage * width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) (percentage * width), (int) height);

		m = new Matrix();
		translate = getBitmapDrawingXY();
		m.postTranslate(translate.x, translate.y);
		canvas.drawBitmap(spriteBmp.bmpFrame, m, null);

	}

}
