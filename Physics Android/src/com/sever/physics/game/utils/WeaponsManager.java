package com.sever.physics.game.utils;

import java.util.ArrayList;

public class WeaponsManager {

	Weapon currentWeapon;
	boolean showOnlyAvailable;
	static WeaponsManager self = null;
	private static ArrayList<Weapon> weaponsList;

	static {
		weaponsList = new ArrayList<Weapon>();
		weaponsList.add(new Weapon(WeaponTypes.BULLET, Constants.FPS * 2, true, false, false, (int) (Constants.FPS * 0.5)));
		weaponsList.add(new Weapon(WeaponTypes.BOMB, Constants.FPS * 2, true, true, false, Constants.FPS * 2));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_TRIPLE, Constants.FPS * 2, true, true, false, Constants.FPS * 2));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_BIG, Constants.FPS * 4, true, true, false, Constants.FPS * 4));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_IMPLODING, Constants.FPS * 5, true, true, true, Constants.FPS * 10));
		weaponsList.add(new Weapon(WeaponTypes.SHOCK_GUN, 0, true, false, false, Constants.FPS * 10));
		weaponsList.add(new Weapon(WeaponTypes.SUPER_SHOCK_GUN, 0, true, false, false, Constants.FPS * 10));
	}

	public boolean getWeaponStatus() {
		currentWeapon = getWeaponByType(WeaponTypes.BOMB);
		return currentWeapon.isAvailable();
	}

	public Weapon getWeaponByType(WeaponTypes wt) {
		for (Weapon w : weaponsList) {
			if (w.getType() == wt) {
				return w;
			}
		}
		return null;
	}

	public Weapon firstWeapon() {
		return weaponsList.get(0);
	}

	public Weapon nextWeapon() {
		if (showOnlyAvailable) {
			int i = weaponsList.indexOf(currentWeapon);
			while (true) {
				Weapon temp = weaponsList.get(++i % weaponsList.size());
				if (temp.isAvailable() || currentWeapon == temp) {
					currentWeapon = temp;
					break;
				}
			}
		} else {
			int i = weaponsList.indexOf(currentWeapon);
			Weapon temp = weaponsList.get(++i % weaponsList.size());
			currentWeapon = temp;
		}

		return currentWeapon;
	}

	public static WeaponsManager getManager() {
		if (self == null)
			self = new WeaponsManager();
		return self;
	}

}
