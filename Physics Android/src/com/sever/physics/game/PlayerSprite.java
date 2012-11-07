package com.sever.physics.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sever.physic.PhysicsActivity;
import com.sever.physic.PhysicsWorld;

public class PlayerSprite extends FreeSprite {

	private static final int BMP_COLUMNS = 1;
	private GameView gameView;
	private Bitmap bmp;
	public int x = 0;
	private int y = 0;
	private int currentFrame = 0;
	private int width;
	private int height;
	private int index;

	public PlayerSprite(GameView gameView, int index, Bitmap bmp) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.index = index;
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = (int) PhysicsActivity.mWorld.bodies.get(index).getPosition().x * 10;
		this.y = (int) PhysicsActivity.mWorld.bodies.get(index).getPosition().y * 10;
	}

	public void onDraw(Canvas canvas) {
		update();
		System.out.println("index:" + index + "-" + x + "," + (gameView.getHeight() - y) + "," + (x + width) + "," + (gameView.getHeight() - y + height));
		Rect dst = new Rect(x, gameView.getHeight() - y, x + width, gameView.getHeight() - y + height);
		canvas.drawBitmap(bmp, null, dst, null);
	}

	private void update() {
		this.x = (int) PhysicsActivity.mWorld.bodies.get(index).getPosition().x * 10;
		this.y = (int) PhysicsActivity.mWorld.bodies.get(index).getPosition().y * 10;
	}
}
