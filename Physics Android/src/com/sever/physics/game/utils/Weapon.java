package com.sever.physics.game.utils;

public class Weapon {

	WeaponTypes type;
	int lifeTimeInFPS;
	boolean available;
	boolean explodes;
	boolean implodes;
	int loadingTimeInFPS;

	public Weapon(WeaponTypes type, int lifeTimeInFPS, boolean available, boolean explodes, boolean implodes, int loadingTimeInFPS) {
		super();
		this.type = type;
		this.lifeTimeInFPS = lifeTimeInFPS;
		this.available = available;
		this.explodes = explodes;
		this.implodes = implodes;
		this.loadingTimeInFPS = loadingTimeInFPS;
	}

	public WeaponTypes getType() {
		return type;
	}

	public void setType(WeaponTypes type) {
		this.type = type;
	}

	public int getLifeTimeInMiliseconds() {
		return lifeTimeInFPS;
	}

	public void setLifeTimeInMiliseconds(int lifeTimeInMiliseconds) {
		this.lifeTimeInFPS = lifeTimeInMiliseconds;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isExplodes() {
		return explodes;
	}

	public void setExplodes(boolean explodes) {
		this.explodes = explodes;
	}

	public boolean isImplodes() {
		return implodes;
	}

	public void setImplodes(boolean implodes) {
		this.implodes = implodes;
	}

	public int getLoadingTimeInMiliseconds() {
		return loadingTimeInFPS;
	}

	public void setLoadingTimeInMiliseconds(int loadingTimeInMiliseconds) {
		this.loadingTimeInFPS = loadingTimeInMiliseconds;
	}
}
