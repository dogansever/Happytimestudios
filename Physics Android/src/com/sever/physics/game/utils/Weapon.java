package com.sever.physics.game.utils;

public class Weapon {

	WeaponTypes type;
	int lifeTimeInMiliseconds;
	boolean available;
	boolean explodes;
	boolean implodes;
	int loadingTimeInMiliseconds;
	
	public Weapon(WeaponTypes type, int lifeTimeInMiliseconds, boolean available, boolean explodes, boolean implodes, int loadingTimeInMiliseconds) {
		super();
		this.type = type;
		this.lifeTimeInMiliseconds = lifeTimeInMiliseconds;
		this.available = available;
		this.explodes = explodes;
		this.implodes = implodes;
		this.loadingTimeInMiliseconds = loadingTimeInMiliseconds;
	}

	public WeaponTypes getType() {
		return type;
	}

	public void setType(WeaponTypes type) {
		this.type = type;
	}

	public int getLifeTimeInMiliseconds() {
		return lifeTimeInMiliseconds;
	}

	public void setLifeTimeInMiliseconds(int lifeTimeInMiliseconds) {
		this.lifeTimeInMiliseconds = lifeTimeInMiliseconds;
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
		return loadingTimeInMiliseconds;
	}

	public void setLoadingTimeInMiliseconds(int loadingTimeInMiliseconds) {
		this.loadingTimeInMiliseconds = loadingTimeInMiliseconds;
	}
}
