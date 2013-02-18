package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.physic.PhysicsApplication;
import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class ButtonFireSprite extends FreeSprite {

	public float xstick = 0;
	public float ystick = 0;
	public boolean active;

	public ButtonFireSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.xstick = x;
		this.ystick = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.spriteList = spriteList;
	}

	public void deactivate() {
		active = false;
	}

	public void activate() {
		active = true;
	}

	public boolean checkButtonTouch(float xn, float yn) {
		if (active)
			return true;

		float range = spacing(xn - x, yn - y);
		if (range < width / 2) {
			// System.out.println("xn:" + xn + " x:" + x + " width:" + width +
			// " yn:" + yn + " y:" + y + " range:" + range);
			return true;
		}

		// if (xn > x - width / 2 && xn < x + width / 2 && yn > y - height / 2
		// && yn < y + height / 2) {
		// System.out.println("xn:" + xn + " x:" + x + " width:" + width +
		// " yn:" + yn + " y:" + y + " height:" + height);
		// return true;
		// }
		return false;
	}

	public void onDown(float xn, float yn) {
		if (!checkButtonTouch(xn, yn))
			return;

		activate();

		// this.x = xn;
		// this.y = yn;
		this.xstick = xn;
		this.ystick = yn;
		// makeVisible();

		((PlayerSprite) gameView.getPlayerSprite()).fireHold();
		if (WeaponsManager.getManager().getWeaponByType(((PlayerSprite) gameView.getPlayerSprite()).weapon.getType()).automatic) {
			((PlayerSprite) gameView.getPlayerSprite()).fireStart();
		}
		((FireArrowSprite) gameView.getFireArrowSprite()).onDown(xn, PhysicsApplication.deviceHeight - yn);
	}

	public void onMove(float xn, float yn) {
		try {
			if (gameView.idle) {
				return;
			}

			this.xstick = xn;

			if (((PlayerSprite) gameView.getPlayerSprite()).weapon.getType() != WeaponTypes.BULLET) {
				if (Math.abs(xstick - x) > 20)
					gameView.getPlayerSprite().facingRigth = xstick > x;
			}
			this.ystick = yn;
			((PlayerSprite) gameView.getPlayerSprite()).fireHold();
			((FireArrowSprite) gameView.getFireArrowSprite()).onMove(xn, PhysicsApplication.deviceHeight - yn);
		} catch (Exception e) {
		}
	}

	public void onUp(float xn, float yn) {
		deactivate();
		if (WeaponsManager.getManager().getWeaponByType(((PlayerSprite) gameView.getPlayerSprite()).weapon.getType()).automatic) {
			((PlayerSprite) gameView.getPlayerSprite()).fireCease();
		} else {
			((PlayerSprite) gameView.getPlayerSprite()).fireStart();
		}
		((FireArrowSprite) gameView.getFireArrowSprite()).onUp(xn, PhysicsApplication.deviceHeight - yn);
		// makeInvisible();
	}

	public void onDraw(Canvas canvas) {

		if (gameView.idle) {
			return;
		}

		// if (gameView.endOfGame) {
		// return;
		// }
		spriteBmp.setBmpIndex(0);
		spriteBmp.currentRow = 0;
		spriteBmp.currentFrame = 0;
		draw(canvas);

		float xtemp = x;
		float ytemp = y;
		x = Math.abs(xstick - x) < width * 0.25f ? xstick : x + (xstick - x < 0 ? -1 : 1) * width * 0.25f;
		y = Math.abs(ystick - y) < height * 0.25f ? ystick : y + (ystick - y < 0 ? -1 : 1) * height * 0.25f;

		spriteBmp.currentFrame = 1;

		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);

		Matrix m = new Matrix();
		Vec2 translate = getBitmapDrawingXY();
		m.postTranslate(translate.x, translate.y);
		canvas.drawBitmap(spriteBmp.bmpFrame, m, null);

		x = xtemp;
		y = ytemp;
	}

	private void draw(Canvas canvas) {
		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);

		Matrix m = new Matrix();
		Vec2 translate = getBitmapDrawingXY();
		m.postTranslate(translate.x, translate.y);
		canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
	}

}
