package com.sever.physics.game.utils;

public class Weapon {

	public WeaponTypes type;
	public int lifeTimeInFPS;
	public boolean available;
	public boolean explodes;
	public boolean implodes;
	public boolean automatic;
	public boolean fireAtMaxSpeed;
	public int loadingTimeInFPS;
	public int damageLife;
	public int explosionRadius;

	public Weapon(WeaponTypes type, int lifeTimeInFPS, boolean available, boolean explodes, boolean implodes, int loadingTimeInFPS, int damageLife, int explosionRadius, boolean automatic,
			boolean fireAtMaxSpeed) {
		super();
		this.type = type;
		this.lifeTimeInFPS = lifeTimeInFPS;
		this.available = available;
		this.explodes = explodes;
		this.implodes = implodes;
		this.loadingTimeInFPS = loadingTimeInFPS;
		this.damageLife = damageLife;
		this.explosionRadius = explosionRadius;
		this.automatic = automatic;
		this.fireAtMaxSpeed = fireAtMaxSpeed;
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

	public int getLifeTimeInFPS() {
		return lifeTimeInFPS;
	}

	public int getLoadingTimeInFPS() {
		return loadingTimeInFPS;
	}

	public int getDamageLife() {
		return damageLife;
	}

	public int getExplosionRadius() {
		return explosionRadius;
	}
}
