package com.sever.physics.game.utils;

import java.util.ArrayList;

public class WeaponsManager {

	Weapon currentWeapon;
	boolean showOnlyAvailable = true;
	static WeaponsManager self = null;
	private static ArrayList<Weapon> weaponsList;

	static {
		weaponsList = new ArrayList<Weapon>();
		weaponsList.add(new Weapon(WeaponTypes.BULLET, Constants.FPS * 2, true, false, false, (int) (Constants.FPS * 0.1), 5, 10, false, true));
		weaponsList.add(new Weapon(WeaponTypes.MISSILE, Constants.FPS * 2, true, true, false, Constants.FPS * 1, 30, 30, false, true));
		weaponsList.add(new Weapon(WeaponTypes.MISSILE_LIGHT, Constants.FPS * 2, true, true, false, (int) (Constants.FPS * 0.5), 10, 30, false, true));
		weaponsList.add(new Weapon(WeaponTypes.MISSILE_LOCKING, Constants.FPS * 2, false, true, false, Constants.FPS * 2, 10, 30, false, true));
		weaponsList.add(new Weapon(WeaponTypes.BOMB, Constants.FPS * 2, true, true, false, Constants.FPS * 1, 10, 40, false, false));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_TRIPLE, Constants.FPS * 2, true, true, false, Constants.FPS * 1, 10, 40, false, false));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_BIG, Constants.FPS * 4, true, true, false, Constants.FPS * 2, 20, 80, false, false));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_IMPLODING, Constants.FPS * 5, true, true, true, Constants.FPS * 4, 30, 100, false, false));
//		weaponsList.add(new Weapon(WeaponTypes.SHOCK_GUN, 0, true, false, false, Constants.FPS * 10, 5, 150, false, false));
//		weaponsList.add(new Weapon(WeaponTypes.SUPER_SHOCK_GUN, 0, true, false, false, Constants.FPS * 10, 5, 1000, false, false));
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
