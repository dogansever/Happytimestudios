package com.sever.physics.game.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsActivity;
import com.sever.physic.R;
import com.sever.physics.game.sprites.PlayerSprite;

public class WeaponsManager {

	private static final int DAMAGE_LOW = 20;
	private static final int DAMAGE_MEDIUM = 30;
	private static final int DAMAGE_HIGH = 50;
	private static final int DAMAGE_VERYHIGH = 75;
	private static final int RADIUS_NARROW = 30;
	private static final int RADIUS_MEDIUM = 30;
	private static final int RADIUS_WIDE = 60;
	private static final int RADIUS_WIDEST = 100;
	Weapon currentWeapon;
	boolean showOnlyAvailable = true;
	static WeaponsManager self = null;
	private static ArrayList<Weapon> weaponsList;

	static {
		weaponsList = new ArrayList<Weapon>();
		weaponsList.add(new Weapon(WeaponTypes.BULLET, Constants.FPS * 2, true, true, false, (int) (Constants.FPS * 0.5), DAMAGE_MEDIUM, RADIUS_NARROW, false, true));
		weaponsList.add(new Weapon(WeaponTypes.MISSILE_LIGHT, Constants.FPS * 2, true, true, false, (int) (Constants.FPS * 0.5), DAMAGE_LOW, RADIUS_NARROW, false, true));
		weaponsList.add(new Weapon(WeaponTypes.MISSILE, Constants.FPS * 2, true, true, false, Constants.FPS * 1, DAMAGE_HIGH, RADIUS_NARROW, false, true));
		weaponsList.add(new Weapon(WeaponTypes.MISSILE_LOCKING, Constants.FPS * 4, false, true, false, Constants.FPS * 2, DAMAGE_MEDIUM, RADIUS_NARROW, false, true));
		weaponsList.add(new Weapon(WeaponTypes.BOMB, Constants.FPS * 2, true, true, false, Constants.FPS * 1, DAMAGE_LOW, RADIUS_MEDIUM, false, false));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_TRIPLE, Constants.FPS * 2, true, true, false, Constants.FPS * 1, DAMAGE_LOW, RADIUS_MEDIUM, false, false));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_CAPSULES, Constants.FPS * 2, true, true, false, Constants.FPS * 1, DAMAGE_LOW, RADIUS_MEDIUM, false, true));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_BIG, Constants.FPS * 4, true, true, false, Constants.FPS * 2, DAMAGE_HIGH, RADIUS_WIDE, false, false));
		weaponsList.add(new Weapon(WeaponTypes.BOMB_IMPLODING, Constants.FPS * 3, true, true, true, Constants.FPS * 4, DAMAGE_VERYHIGH, RADIUS_WIDEST, false, false));
		weaponsList.add(new Weapon(WeaponTypes.BOSS1, Constants.FPS * 3, false, false, false, Constants.FPS * 4, DAMAGE_VERYHIGH, RADIUS_WIDEST, false, true));
		weaponsList.add(new Weapon(WeaponTypes.BOSS2, Constants.FPS * 3, false, false, false, Constants.FPS * 4, DAMAGE_VERYHIGH, RADIUS_WIDEST, false, true));
		weaponsList.add(new Weapon(WeaponTypes.BOSS3, Constants.FPS * 3, false, false, false, Constants.FPS * 4, DAMAGE_VERYHIGH, RADIUS_WIDEST, false, true));
		// weaponsList.add(new Weapon(WeaponTypes.SHOCK_GUN, 0, true, false,
		// false, Constants.FPS * 10, 5, 150, false, false));
		// weaponsList.add(new Weapon(WeaponTypes.SUPER_SHOCK_GUN, 0, true,
		// false, false, Constants.FPS * 10, 5, 1000, false, false));
	}

	// public boolean getWeaponStatus() {
	// currentWeapon = getWeaponByType(WeaponTypes.BOMB);
	// return currentWeapon.isAvailable();
	// }

	public float getBitmapPercentageByType(WeaponTypes wt, Boolean fly) {
		// if (wt == WeaponTypes.MISSILE) {
		// return fly ? 0.7f : 0.9f;
		// }
		// if (wt == WeaponTypes.MISSILE_LIGHT) {
		// return fly ? 0.8f : 1.0f;
		// }
		// if (wt == WeaponTypes.MISSILE_LOCKING) {
		// return fly ? 0.5f : 1.0f;
		// }
		// if (wt == WeaponTypes.BOMB) {
		// return fly ? 1.0f : 1.0f;
		// }
		// if (wt == WeaponTypes.BOMB_BIG) {
		// return fly ? 1.0f : 1.0f;
		// }
		return 1.0f;
	}

	public Weapon getWeaponByType(WeaponTypes wt) {
		for (Weapon w : weaponsList) {
			if (w.getType() == wt) {
				return w;
			}
		}
		return null;
	}

	public Weapon firstAvailableWeapon() {
		currentWeapon = null;
		currentWeapon = nextAvailableWeapon();
		return currentWeapon;
	}

	public Weapon nextAvailableWeapon() {
		if (showOnlyAvailable) {
			int index;
			if (currentWeapon != null) {
				index = weaponsList.indexOf(currentWeapon);
			} else {
				index = 0;
			}
			while (true) {
				Weapon temp = weaponsList.get(++index % weaponsList.size());
				temp.setAvailable(getWTAvailability(temp.getType()));
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

	public WeaponTypes getWTByEnemyLevel(int level) {
		switch (level) {
		case 1:
		case 6:
			return WeaponTypes.MISSILE_LIGHT;
		case 2:
		case 7:
			return WeaponTypes.MISSILE;
		case 3:
		case 8:
			return WeaponTypes.MISSILE_LOCKING;
		case 4:
			return WeaponTypes.BOMB;
		case 5:
			return WeaponTypes.BOMB_BIG;
		}
		return null;
	}

	public boolean getFlyByEnemyLevel(int level) {
		switch (level) {
		case 1:
		case 2:
		case 3:
			return false;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			return true;
		}
		return false;
	}

	public Bitmap getEnemyBitmapByWT(WeaponTypes wt, Boolean fly) {
		int resId = R.drawable.boss01x2x1;
		if (wt == WeaponTypes.MISSILE) {
			resId = R.drawable.enemy05x2x1;
		}
		if (wt == WeaponTypes.MISSILE_LIGHT) {
			resId = R.drawable.enemy02x2x1;
		}
		if (wt == WeaponTypes.MISSILE_LOCKING) {
			resId = R.drawable.enemy03x2x1;
		}
		if (wt == WeaponTypes.BOMB) {
			resId = R.drawable.enemy01x2x1;
		}
		if (wt == WeaponTypes.BOMB_BIG) {
			resId = R.drawable.enemy04x2x1;
		}
		if (wt == WeaponTypes.BOSS1) {
			resId = R.drawable.boss01x2x1;
		}
		if (wt == WeaponTypes.BOSS2) {
			resId = R.drawable.boss02x2x1;
		}
		if (wt == WeaponTypes.BOSS3) {
			resId = R.drawable.boss03x2x1;
		}

		return BitmapManager.getManager().createScaledBitmap(resId, WeaponsManager.getManager().getBitmapPercentageByType(wt, fly));
	}

	public Bitmap getEnemyThrottleBitmapByWT(WeaponTypes wt, Boolean fly) {
		int resId = R.drawable.boss01throttlex2x2;
		if (wt == WeaponTypes.MISSILE) {
			resId = R.drawable.enemy05throttlex2x2;
		}
		if (wt == WeaponTypes.MISSILE_LIGHT) {
			resId = R.drawable.enemy02throttlex2x2;
		}
		if (wt == WeaponTypes.MISSILE_LOCKING) {
			resId = R.drawable.enemy03throttlex2x2;
		}
		if (wt == WeaponTypes.BOMB) {
			resId = R.drawable.enemy01throttlex2x2;
		}
		if (wt == WeaponTypes.BOMB_BIG) {
			resId = R.drawable.enemy04throttlex2x2;
		}
		if (wt == WeaponTypes.BOSS1) {
			resId = R.drawable.boss01throttlex2x2;
		}
		if (wt == WeaponTypes.BOSS2) {
			resId = R.drawable.boss02throttlex2x2;
		}
		if (wt == WeaponTypes.BOSS3) {
			resId = R.drawable.boss03throttlex2x2;
		}

		return BitmapManager.getManager().createScaledBitmap(resId, WeaponsManager.getManager().getBitmapPercentageByType(wt, fly));
	}

	public Bitmap getEnemyBurningBitmapByWT(WeaponTypes wt, Boolean fly) {
		int resId = R.drawable.boss01burningx2x2;
		if (wt == WeaponTypes.MISSILE) {
			resId = R.drawable.enemy05burningx2x2;
		}
		if (wt == WeaponTypes.MISSILE_LIGHT) {
			resId = R.drawable.enemy02burningx2x2;
		}
		if (wt == WeaponTypes.MISSILE_LOCKING) {
			resId = R.drawable.enemy03burningx2x2;
		}
		if (wt == WeaponTypes.BOMB) {
			resId = R.drawable.enemy01burningx2x2;
		}
		if (wt == WeaponTypes.BOMB_BIG) {
			resId = R.drawable.enemy04burningx2x2;
		}
		if (wt == WeaponTypes.BOSS1) {
			resId = R.drawable.boss01burningx2x2;
		}
		if (wt == WeaponTypes.BOSS2) {
			resId = R.drawable.boss02burningx2x2;
		}
		if (wt == WeaponTypes.BOSS3) {
			resId = R.drawable.boss03burningx2x2;
		}

		return BitmapManager.getManager().createScaledBitmap(resId, WeaponsManager.getManager().getBitmapPercentageByType(wt, fly));
	}

	public Bitmap getEnemyExplodingBitmapByWT(WeaponTypes wt, Boolean fly) {
		int resId = R.drawable.explosion02x3x1;
		if (wt == WeaponTypes.MISSILE) {
			resId = R.drawable.explosion02x3x1;
		}
		if (wt == WeaponTypes.MISSILE_LIGHT) {
			resId = R.drawable.explosion02x3x1;
		}
		if (wt == WeaponTypes.MISSILE_LOCKING) {
			resId = R.drawable.explosion02x3x1;
		}
		if (wt == WeaponTypes.BOMB) {
			resId = R.drawable.explosion02x3x1;
		}
		if (wt == WeaponTypes.BOMB_BIG) {
			resId = R.drawable.explosion02x3x1;
		}

		return BitmapManager.getManager().createScaledBitmap(resId, WeaponsManager.getManager().getBitmapPercentageByType(wt, fly));
	}

	public static void refreshSwapWeaponButtonBitmap() {
		Weapon weapon = ((PlayerSprite) PhysicsActivity.context.getGameView().getPlayerSprite()).weapon;
		WeaponTypes type = weapon.getType();
		weapon.setAvailable(getManager().getWTAvailability(type));
		int h = 0;
		int w = 0;
		if (weapon.isAvailable())
			BitmapManager.weaponSwapButton = BitmapManager.getManager().createScaledBitmap(getWTBitmap(type), w, h);
	}

	public boolean getWTAvailability(WeaponTypes wt) {
		int unlockLevel = getWTUnlockLevel(wt);

		return unlockLevel <= Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(1));
	}

	public int getWTUnlockLevel(WeaponTypes wt) {
		int unlockLevel = 100;
		if (wt == WeaponTypes.MISSILE) {
			unlockLevel = 6;
		}
		if (wt == WeaponTypes.MISSILE_LIGHT) {
			unlockLevel = 0;
		}
		if (wt == WeaponTypes.BOMB_TRIPLE) {
			unlockLevel = 3;
		}
		if (wt == WeaponTypes.BOMB_CAPSULES) {
			unlockLevel = 5;
		}
		if (wt == WeaponTypes.BOMB_IMPLODING) {
			unlockLevel = 10;
		}
		if (wt == WeaponTypes.BOMB) {
			unlockLevel = 0;
		}
		if (wt == WeaponTypes.BOMB_BIG) {
			unlockLevel = 7;
		}
		if (wt == WeaponTypes.BULLET) {
			unlockLevel = 0;
		}
		if (wt == WeaponTypes.MISSILE_LOCKING) {
			unlockLevel = 12;
		}
		return unlockLevel;
	}

	public WeaponTypes isNewWeaponUnlocked(boolean b) {
		int level = Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(1));
		ArrayList<WeaponTypes> list = getWTListForPlayer();
		for (WeaponTypes weaponTypes : list) {
			if (getWTUnlockLevel(weaponTypes) == level && b) {
				return weaponTypes;
			}
		}
		return null;
	}

	public ArrayList<WeaponTypes> getWTListForPlayer() {
		ArrayList<WeaponTypes> wtList = new ArrayList<WeaponTypes>();
		wtList.add(WeaponTypes.BULLET);
		wtList.add(WeaponTypes.MISSILE_LIGHT);
		wtList.add(WeaponTypes.BOMB);
		wtList.add(WeaponTypes.BOMB_TRIPLE);
		wtList.add(WeaponTypes.BOMB_CAPSULES);
		wtList.add(WeaponTypes.MISSILE);
		wtList.add(WeaponTypes.BOMB_BIG);
		wtList.add(WeaponTypes.BOMB_IMPLODING);
		wtList.add(WeaponTypes.MISSILE_LOCKING);
		return wtList;
	}

	public static int getWTBitmap(WeaponTypes type) {
		if (type == WeaponTypes.BULLET) {
			return R.drawable.weaponsmine;
		} else if (type == WeaponTypes.BOMB) {
			return R.drawable.weaponsbomb;
		} else if (type == WeaponTypes.BOMB_TRIPLE) {
			return R.drawable.weaponsbombtriple;
		} else if (type == WeaponTypes.BOMB_CAPSULES) {
			return R.drawable.weaponsbombcapsule;
		} else if (type == WeaponTypes.BOMB_BIG) {
			return R.drawable.weaponsbombbig;
		} else if (type == WeaponTypes.BOMB_IMPLODING) {
			return R.drawable.weaponsbombtimer;
		} else if (type == WeaponTypes.MISSILE) {
			return R.drawable.weaponsmissile;
		} else if (type == WeaponTypes.MISSILE_LIGHT) {
			return R.drawable.weaponsmissilelight;
		} else if (type == WeaponTypes.MISSILE_LOCKING) {
			return R.drawable.weaponsmissilelocking;
		}
		return 0;
	}

	public static String getWTInfo(WeaponTypes type) {
		if (type == WeaponTypes.BULLET) {
			return "Mine (Damage:Fair Radius:Medium)";
		} else if (type == WeaponTypes.BOMB) {
			return "Bomb (Damage:Fair Radius:Medium)";
		} else if (type == WeaponTypes.BOMB_TRIPLE) {
			return "Triple Bomb (Damage:Medium Radius:Medium)";
		} else if (type == WeaponTypes.BOMB_CAPSULES) {
			return "Capsules (Damage:Medium Radius:Medium)";
		} else if (type == WeaponTypes.BOMB_BIG) {
			return "Big Bomb (Damage:High Radius:Wide)";
		} else if (type == WeaponTypes.BOMB_IMPLODING) {
			return "Gravity Bomb (Damage:Very High Radius:Widest)";
		} else if (type == WeaponTypes.MISSILE) {
			return "Missile (Damage:High Radius:Narrow)";
		} else if (type == WeaponTypes.MISSILE_LIGHT) {
			return "Light Missile (Damage:Fair Radius:Narrow)";
		} else if (type == WeaponTypes.MISSILE_LOCKING) {
			return "Seeking Missile (Damage:High Radius:Narrow)";
		}
		return "";
	}

	public Integer getBonusByEnemyWT(WeaponTypes wt, boolean fly) {
		Integer bonus = 100;
		if (wt == WeaponTypes.BOMB) {
			bonus = 10;
		} else if (wt == WeaponTypes.BOMB_BIG) {
			bonus = 20;
		} else if (wt == WeaponTypes.MISSILE_LIGHT) {
			bonus = fly ? 30 : 15;
		} else if (wt == WeaponTypes.MISSILE) {
			bonus = fly ? 40 : 20;
		} else if (wt == WeaponTypes.MISSILE_LOCKING) {
			bonus = fly ? 50 : 25;
		}
		return bonus;
	}

}
