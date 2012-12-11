package com.sever.physics.game.utils;

import java.util.ArrayList;

public class WeaponsManager {

	Weapon currentWeapon;
	boolean showOnlyAvailable;
	static WeaponsManager self = null;
	private static ArrayList<Weapon> weaponsList;

	static {
		weaponsList = new ArrayList<Weapon>();
		weaponsList.add(new Weapon(WeaponTypes.BULLET, 3000, true, false, false, 100));
		weaponsList.add(new Weapon(WeaponTypes.BOMB, 5000, true, true, false, 2000));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_TRIPLE, 5000, true, true, false, 4000));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_BIG, 5000, true, true, false, 4000));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_IMPLODING, 5000, true, true, true, 10000));
		weaponsList.add(new Weapon(WeaponTypes.SHOCK_GUN, 100, true, false, false, 10000));
		weaponsList.add(new Weapon(WeaponTypes.SUPER_SHOCK_GUN, 100, true, false, false, 20000));
	}

	public boolean getWeaponStatus() {
		return currentWeapon.isAvailable();
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
