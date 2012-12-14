package com.sever.physics.game;

import com.sever.physics.game.sprites.FreeSprite;

public interface GameViewI {

	public FreeSprite addFireArrow(float x, float y);

	public FreeSprite addJoystick(float x, float y);

	public FreeSprite addFuelBar(float x, float y);

	public FreeSprite addPowerBar(float x, float y);

	public FreeSprite addBullet(float x, float y);

	public FreeSprite addGrenadeTriple(float x, float y);

	public FreeSprite addGrenadeSmall(float x, float y);

	public FreeSprite addGrenade(float x, float y);

	public FreeSprite addGrenadeImploding(float x, float y);

	public FreeSprite addHookStatic(float x, float y);

	public void addPlayer(float x, float y);

	public void addEnemy(float x, float y);

	public FreeSprite addGroundBoxStatic(float x, float y, float hx, float hy);

	public FreeSprite addBoxStatic(float x, float y, int w, int h);

	public void addPlanetStatic(float x, float y);

	public void addBarrel(float x, float y);

	public FreeSprite addBox(float x, float y);

	public void addBox2(float x, float y);
}
