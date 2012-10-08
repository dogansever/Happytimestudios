package com.sever.android.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sever.android.main.sprite.FreeSprite;
import com.sever.android.main.sprite.HitChanceSprite;
import com.sever.android.main.sprite.HitSprite;
import com.sever.android.main.sprite.LifeProgressSprite;
import com.sever.android.main.sprite.MissSprite;
import com.sever.android.main.sprite.OwlSprite;
import com.sever.android.main.sprite.ProgressSprite;
import com.sever.android.main.sprite.WarningSprite;
import com.sever.android.main.sprite.ZombieSprite;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public GameLoopThread gameLoopThread;
	public List<ArrayList<Object>> zombies = new ArrayList<ArrayList<Object>>();
	public boolean ready2SendNextWave;
	public ConcurrentLinkedQueue<FreeSprite> freeSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<OwlSprite> spritesOwl = new ConcurrentLinkedQueue<OwlSprite>();
	public ConcurrentLinkedQueue<ZombieSprite> sprites = new ConcurrentLinkedQueue<ZombieSprite>();
	public ConcurrentLinkedQueue<ZombieSprite> sprites2 = new ConcurrentLinkedQueue<ZombieSprite>();
	public ConcurrentLinkedQueue<ZombieSprite> sprites3 = new ConcurrentLinkedQueue<ZombieSprite>();
	public ConcurrentLinkedQueue<ZombieSprite> sprites4 = new ConcurrentLinkedQueue<ZombieSprite>();
	public ConcurrentLinkedQueue<ZombieSprite> sprites5 = new ConcurrentLinkedQueue<ZombieSprite>();
	public static int waveCount = 50;
	public static int[] waveCountList = { 20, 30, 35, 40, 45, 50, 55, 60, 65, 80 };
	public static int hitCount = 0;
	public static int LIFE_COUNT = 100;
	public int score = 0;
	public int point = 0;

	public void cancelTimer() {
		timerAnimation.cancel();
	}

	public int incrementScore(int index) {
		if (GameView.hitCount > 0) {
			GameView.hitCount--;
		}
		int bonus = 1;
		if (getHitChance(index) == 0) {// far
			bonus = 5;
		} else if (getHitChance(index) == 1) {// medium
			bonus = 2;
		} else if (getHitChance(index) == 2) {// close
			bonus = 1;
		}
		if (GameGameActivity.RIGHTCOUNT < GameGameActivity.RIGHTCOUNT_2X) {
			point = 1 * bonus;
			score += point;
			return score;
		} else if (GameGameActivity.RIGHTCOUNT < GameGameActivity.RIGHTCOUNT_3X) {
			point = 2 * bonus;
			score += point;
			return score;
		} else {
			point = 3 * bonus;
			score += point;
			return score;
		}
	}

	public int getPoint() {
		return point;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	private int scoreHigh = 0;
	static final int PLAY_AREA_ROWS = 5;
	static final int PLAY_AREA_PADDING_TOP = 10;
	static final int PLAY_AREA_PADDING_BOTTOM = 10;
	public static ArrayList<Integer> rowQueue = new ArrayList<Integer>();
	public float percentage = 0.50f;
	private Context context;

	public void prepareZombieWaves() {
		ready2SendNextWave = true;
		Random randomGenerator = new Random();
		int randomInt;
		for (int i = 0; i < waveCount; i++) {
			int minDelay = 5;
			int delayInSeconds = randomGenerator.nextInt(5) + minDelay;
			System.out.println("prepareZombieWaves:delayInSeconds:" + delayInSeconds);
			ArrayList<Object> wave = new ArrayList<Object>();
			wave.add(delayInSeconds);

			for (int j = 0; j < PLAY_AREA_ROWS; j++) {
				randomInt = randomGenerator.nextInt(Zombies.values().length);
				wave.add(Zombies.values()[randomInt]);
			}
			zombies.add(wave);
		}
	}

	public float getLifeProgressPercentage() {
		return 1.0f - ((float) (LIFE_COUNT - hitCount) / LIFE_COUNT);
	}

	public float getProgressPercentage() {
		return 1.0f - ((float) (waveCount - zombies.size()) / waveCount);
	}

	public boolean threadStarted = false;
	public List<ArrayList<Owls>> owls = new ArrayList<ArrayList<Owls>>();

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surfaceDestroyed");
		boolean retry = true;
		gameLoopThread.setRunning(false);
		timePause = new Date().getTime();
		while (retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		threadStarted = false;
		System.out.println("surfaceDestroyed:end");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated");
		if (gameLoopThread.getState() == Thread.State.TERMINATED) {
			gameLoopThread = new GameLoopThread(this, holder);
			threadStarted = true;
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
			resume();
		} else {
			hitCount = 0;
			threadStarted = true;
			gameLoopThread.setRunning(true);
			prepareRowCoordinates();
			prepareOwls();
			createZombieWaveSprites();
			createSpritesOwl();
			createFreeSprites();
			showStartingText();
			gameLoopThread.start();
		}
		// if (gameLoopThread.getState() == Thread.State.TERMINATED) {
		// gameLoopThread = new GameLoopThread(GameView.this);
		// threadStarted = true;
		// gameLoopThread.setRunning(true);
		// gameLoopThread.start();
		// } else {
		// threadStarted = true;
		// gameLoopThread.setRunning(true);
		// gameLoopThread.start();
		// }
	}

	private void showStartingText() {
		ArrayList<String> texts = new ArrayList<String>();
		texts.add("I like the smell of a zombie in the morning...");
		texts.add("A large zombie activity is detected this way...");
		texts.add("It is going to be a beautiful day, I think...");
		texts.add("I feel lucky today...");
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(texts.size());
		((GameGameActivity) context).showMiddleInfoText(texts.get(index));
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		System.out.println("surfaceChanged");
		if (!threadStarted) {
			threadStarted = true;
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
		}
	}

	public SurfaceHolder holder;

	public GameView(final Context context) {
		super(context);
		this.context = context;
		prepareZombieWaves();
		holder = getHolder();
		holder.addCallback(this);
		gameLoopThread = new GameLoopThread(this, holder);
	}

	protected void prepareOwls() {
		ArrayList<Owls> list = new ArrayList<Owls>();
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlSniper);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlMachineGun);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlSniper);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlSniper);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlSniper);
		list.add(Owls.owlSniper);
		list.add(Owls.owlSniper);
		list.add(Owls.owlSniper);
		list.add(Owls.owlSniper);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlSniper);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlSniper);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlSniper);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlSniper);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlSniper);
		owls.add(list);

		list = new ArrayList<Owls>();
		list.add(Owls.owlSniper);
		list.add(Owls.owlBaretta);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlMachineGun);
		list.add(Owls.owlBaretta);
		owls.add(list);
	}

	public void releaseBitmaps() {
		for (ZombieSprite z : sprites) {
			z.freeBitmaps();
		}
		sprites.clear();
		for (ZombieSprite z : sprites2) {
			z.freeBitmaps();
		}
		sprites2.clear();
		for (ZombieSprite z : sprites3) {
			z.freeBitmaps();
		}
		sprites3.clear();
		for (ZombieSprite z : sprites4) {
			z.freeBitmaps();
		}
		sprites4.clear();
		for (ZombieSprite z : sprites5) {
			z.freeBitmaps();
		}
		sprites5.clear();
		for (FreeSprite z : freeSprites) {
			z.freeBitmaps();
		}
		freeSprites.clear();
	}

	public Timer timerAnimation;
	private Runnable r;
	private boolean justcametolife = false;
	public long timePause;
	public long timeResumed;
	public long delayInSeconds;
	private long timeFired;

	private void createZombieWaveSprites() {
		try {
			if (zombies.size() == 0) {
				return;
			}

			// if (!gameLoopThread.isRunning()) {
			// System.out.println("returning:isRunning:" +
			// gameLoopThread.isRunning());
			// return;
			// }
			//
			// if (gameLoopThread.isSleeping()) {
			// System.out.println("returning:isSleeping:" +
			// gameLoopThread.isSleeping());
			// return;
			// }

			final ArrayList<Object> wave = zombies.get(0);
			System.out.println("zombies.size():" + zombies.size());
			zombies.remove(0);
			delayInSeconds = (Integer) wave.get(0);

			r = new Runnable() {
				public void run() {
					try {
						synchronized (this) {
							while (!gameLoopThread.isRunning()) {
								try {
									System.out.println("wait:notRunning");
									wait();
									System.out.println("continue");
								} catch (Exception e) {
								}
							}
						}
						synchronized (this) {
							while (gameLoopThread.isSleeping()) {
								try {
									System.out.println("wait:sleeping");
									wait();
									System.out.println("continue");
								} catch (Exception e) {
								}
							}
						}

						synchronized (this) {
							if (justcametolife) {
								justcametolife = false;
								try {
									long t = delayInSeconds * 1000 - (timePause - timeFired);
									System.out.println("createSprites: timePause - timeFired:" + t);
									System.out.println("createSprites: wait:" + t);
									wait(t);
								} catch (Exception e) {
								}
							}
						}

						int i = 0;
						int t = 0;
						System.out.println("new sprite wave is being send.");
						for (Object object : wave) {
							if (object instanceof Integer) {
							} else {
								if (((Zombies) object) == Zombies.nozombie) {
									i++;
									continue;
								} else if (((Zombies) object) == Zombies.zombie1) {
									t = 0;
								} else if (((Zombies) object) == Zombies.zombie2) {
									t = 1;
								} else if (((Zombies) object) == Zombies.zombie3) {
									t = 2;
								} else if (((Zombies) object) == Zombies.zombie4) {
									t = 3;
								} else if (((Zombies) object) == Zombies.zombie5) {
									t = 4;
								} else if (((Zombies) object) == Zombies.zombie6) {
									t = 5;
								}
								switch (i) {
								case 0:
									sprites.add(createSprite(new Bitmap[] { StartActivity.bmpZombie.get(t), StartActivity.bmpZombieDie.get(t),
											StartActivity.bmpZombieAttack.get(t) }, i, sprites, ((Zombies) object)));
									break;
								case 1:
									sprites2.add(createSprite(new Bitmap[] { StartActivity.bmpZombie.get(t), StartActivity.bmpZombieDie.get(t),
											StartActivity.bmpZombieAttack.get(t) }, i, sprites2, ((Zombies) object)));
									break;
								case 2:
									sprites3.add(createSprite(new Bitmap[] { StartActivity.bmpZombie.get(t), StartActivity.bmpZombieDie.get(t),
											StartActivity.bmpZombieAttack.get(t) }, i, sprites3, ((Zombies) object)));
									break;
								case 3:
									sprites4.add(createSprite(new Bitmap[] { StartActivity.bmpZombie.get(t), StartActivity.bmpZombieDie.get(t),
											StartActivity.bmpZombieAttack.get(t) }, i, sprites4, ((Zombies) object)));
									break;
								case 4:
									sprites5.add(createSprite(new Bitmap[] { StartActivity.bmpZombie.get(t), StartActivity.bmpZombieDie.get(t),
											StartActivity.bmpZombieAttack.get(t) }, i, sprites5, ((Zombies) object)));
									break;

								default:
									break;
								}
								i++;
							}
						}

						createZombieWaveSprites();
					} catch (Exception e) {
					}
				}
			};
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					r.run();
				}
			};
			timerAnimation = new Timer();
			timerAnimation.schedule(task, 1000 * delayInSeconds);
			timeFired = new Date().getTime();

			// 800-280 = 520 /5 = 104
			// 280+104*n (n=1....5)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareRowCoordinates() {
		if (rowQueue.size() != 0) {
			return;
		}
		int height = 800;
		int sky = 0;
		int rowHeight = (this.getHeight() * (height - sky) / height) / PLAY_AREA_ROWS;
		for (int i = 0; i < PLAY_AREA_ROWS; i++) {
			rowQueue.add((this.getHeight() * sky / height) + rowHeight * (i + 1));
		}
	}

	public void createFreeSpritesWarning(int index) {
		freeSprites.add(createWarning(index));
	}

	public FreeSprite createWarning(int index) {
		System.out.println("createWarning:" + index);
		return new WarningSprite(freeSprites, this, index, StartActivity.warningBmp);
	}

	public void createFreeSpritesJammed(int index) {
		freeSprites.add(createJammed(index));
	}

	public FreeSprite createJammed(int index) {
		System.out.println("createJammed:" + index);
		return new HitSprite(freeSprites, this, index, StartActivity.jammedBmp);
	}

	public void createFreeSpritesHit(int index) {
		freeSprites.add(createHit(index));
	}

	public FreeSprite createHit(int index) {
		System.out.println("createHit:" + index);
		return new HitSprite(freeSprites, this, index, StartActivity.hitBmp);
	}

	public void createFreeSpritesMiss(int index) {
		freeSprites.add(createMiss(index));
	}

	public FreeSprite createMiss(int index) {
		System.out.println("createMiss:" + index);
		return new MissSprite(freeSprites, this, index, StartActivity.missBmp);
	}

	protected void createFreeSprites() {
		freeSprites.add(createHitChance(0));
		freeSprites.add(createHitChance(1));
		freeSprites.add(createHitChance(2));
		freeSprites.add(createHitChance(3));
		freeSprites.add(createHitChance(4));
		freeSprites.add(createStageProcess());
		freeSprites.add(createLifeProgress());
	}

	private ProgressSprite createStageProcess() {
		System.out.println("createStageProcess:");
		int x = (int) (getWidth() * 0.025f);
		// int x = (int) (20);// width of pause
		int y = 1;// padding of pause button
		return new ProgressSprite(freeSprites, this, x, y, StartActivity.bmpProgress, StartActivity.bmpProgressHead);
	}

	private LifeProgressSprite createLifeProgress() {
		System.out.println("createLifeProgress:");
		int xw = (int) (getWidth() * 0.045f);
		// int xw = (int) (40);
		int x = (int) (((FreeSprite) freeSprites.toArray()[5]).getWidth()) + xw + (int) (xw * ProgressSprite.bmpPercentage);//
		int y = 1;
		return new LifeProgressSprite(freeSprites, this, x, y, StartActivity.bmpProgressLife, StartActivity.bmpProgressOwl);
	}

	private HitChanceSprite createHitChance(int index) {
		System.out.println("createHitChance:" + index);
		return new HitChanceSprite(freeSprites, this, index, StartActivity.hitChancesBmp);
	}

	private ZombieSprite createSprite(Bitmap[] bmp, int i, ConcurrentLinkedQueue<ZombieSprite> sprites6, Zombies object) {
		((StartActivity) StartActivity.context).zombieWalksSound();
		System.out.println("createSprite:" + i);
		if (((Zombies) object) == Zombies.zombie5) {
			return new ZombieSprite(this, bmp, i, sprites6).makeZombieRun();
		} else if (((Zombies) object) == Zombies.zombie6) {
			return new ZombieSprite(this, bmp, i, sprites6).makeZombieTough();
		} else {
			return new ZombieSprite(this, bmp, i, sprites6);
		}
	}

	private void createSpritesOwl() {
		createSpriteOwlx(0, owls.get(MenuActivity.level - 1).get(0));
		createSpriteOwlx(1, owls.get(MenuActivity.level - 1).get(1));
		createSpriteOwlx(2, owls.get(MenuActivity.level - 1).get(2));
		createSpriteOwlx(3, owls.get(MenuActivity.level - 1).get(3));
		createSpriteOwlx(4, owls.get(MenuActivity.level - 1).get(4));
	}

	private void createSpriteOwlx(int i, Owls owl) {
		spritesOwl.add(createSpriteOwl(new Bitmap[] { StartActivity.bmpOwl.get(owl), StartActivity.bmpOwlAttacked.get(owl),
				StartActivity.bmpOwl_Jammed.get(owl) }, i, spritesOwl, owl));
	}

	private OwlSprite createSpriteOwl(Bitmap[] bmp, int i, ConcurrentLinkedQueue<OwlSprite> sprites, Owls o) {
		System.out.println("createSpriteOwl:" + i);
		return new OwlSprite(this, bmp, i, sprites, o);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		update();
		// Paint paint = new Paint();
		// paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		// canvas.drawPaint(paint);
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		// canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

		try {
			Rect dst = new Rect(0, 0, getWidth(), getHeight());
			canvas.drawBitmap(StartActivity.bmpBack, null, dst, null);
			synchronized (getHolder()) {
				for (Iterator<OwlSprite> it = spritesOwl.iterator(); it.hasNext();) {
					OwlSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}
			synchronized (getHolder()) {
				for (Iterator<ZombieSprite> it = sprites.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}
			synchronized (getHolder()) {
				for (Iterator<ZombieSprite> it = sprites2.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}
			synchronized (getHolder()) {
				for (Iterator<ZombieSprite> it = sprites3.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}
			synchronized (getHolder()) {
				for (Iterator<ZombieSprite> it = sprites4.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}

			synchronized (getHolder()) {
				for (Iterator<ZombieSprite> it = sprites5.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}
			synchronized (getHolder()) {
				for (Iterator<FreeSprite> it = freeSprites.iterator(); it.hasNext();) {
					FreeSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ZombieSprite victim = null;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (victim != null) {
				float x2 = event.getX();
				victim.moveBack(x2);
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			victim = null;
			for (Iterator<ZombieSprite> it = sprites.iterator(); it.hasNext();) {
				ZombieSprite sprite = it.next();
				float x2 = event.getX();
				float y2 = event.getY();
				if (sprite.isCollision(x2, y2)) {
					victim = sprite;
					return true;
				}
			}
			for (Iterator<ZombieSprite> it = sprites2.iterator(); it.hasNext();) {
				ZombieSprite sprite = it.next();
				float x2 = event.getX();
				float y2 = event.getY();
				if (sprite.isCollision(x2, y2)) {
					victim = sprite;
					return true;
				}
			}
			for (Iterator<ZombieSprite> it = sprites3.iterator(); it.hasNext();) {
				ZombieSprite sprite = it.next();
				float x2 = event.getX();
				float y2 = event.getY();
				if (sprite.isCollision(x2, y2)) {
					victim = sprite;
					return true;
				}
			}
			for (Iterator<ZombieSprite> it = sprites4.iterator(); it.hasNext();) {
				ZombieSprite sprite = it.next();
				float x2 = event.getX();
				float y2 = event.getY();
				if (sprite.isCollision(x2, y2)) {
					victim = sprite;
					return true;
				}
			}
			for (Iterator<ZombieSprite> it = sprites5.iterator(); it.hasNext();) {
				ZombieSprite sprite = it.next();
				float x2 = event.getX();
				float y2 = event.getY();
				if (sprite.isCollision(x2, y2)) {
					victim = sprite;
					return true;
				}
			}
		} else {
			victim = null;
		}
		return true;
	}

	public void shootFailed() {
		((StartActivity) StartActivity.context).fireFailedSound();
	}

	public void shootAt(int rowIndex) {
		try {
			((OwlSprite) spritesOwl.toArray()[rowIndex]).fire();
			int tempx = 0;
			ZombieSprite tempsprite = null;
			synchronized (getHolder()) {
				switch (rowIndex) {
				case 0:
					tempsprite = null;
					for (Iterator<ZombieSprite> it = sprites.iterator(); it.hasNext();) {
						ZombieSprite sprite = it.next();
						if (sprite.isKilled())
							continue;
						if (tempx < sprite.x) {
							tempx = sprite.x;
							tempsprite = sprite;
						}
					}
					if (tempsprite != null) {
						if (killSucceed(tempx, ((OwlSprite) spritesOwl.toArray()[rowIndex]).owl)) {
							tempsprite.killed();
							createFreeSpritesHit(rowIndex);
						} else {
							createFreeSpritesMiss(rowIndex);
						}
					}
					break;
				case 1:
					tempsprite = null;
					for (Iterator<ZombieSprite> it = sprites2.iterator(); it.hasNext();) {
						ZombieSprite sprite = it.next();
						if (sprite.isKilled())
							continue;
						if (tempx < sprite.x) {
							tempx = sprite.x;
							tempsprite = sprite;
						}
					}
					if (tempsprite != null) {
						if (killSucceed(tempx, ((OwlSprite) spritesOwl.toArray()[rowIndex]).owl)) {
							tempsprite.killed();
							createFreeSpritesHit(rowIndex);
						} else {
							createFreeSpritesMiss(rowIndex);
						}
					}
					break;
				case 2:
					tempsprite = null;
					for (Iterator<ZombieSprite> it = sprites3.iterator(); it.hasNext();) {
						ZombieSprite sprite = it.next();
						if (sprite.isKilled())
							continue;
						if (tempx < sprite.x) {
							tempx = sprite.x;
							tempsprite = sprite;
						}
					}
					if (tempsprite != null) {
						if (killSucceed(tempx, ((OwlSprite) spritesOwl.toArray()[rowIndex]).owl)) {
							tempsprite.killed();
							createFreeSpritesHit(rowIndex);
						} else {
							createFreeSpritesMiss(rowIndex);
						}
					}
					break;
				case 3:
					tempsprite = null;
					for (Iterator<ZombieSprite> it = sprites4.iterator(); it.hasNext();) {
						ZombieSprite sprite = it.next();
						if (sprite.isKilled())
							continue;
						if (tempx < sprite.x) {
							tempx = sprite.x;
							tempsprite = sprite;
						}
					}
					if (tempsprite != null) {
						if (killSucceed(tempx, ((OwlSprite) spritesOwl.toArray()[rowIndex]).owl)) {
							tempsprite.killed();
							createFreeSpritesHit(rowIndex);
						} else {
							createFreeSpritesMiss(rowIndex);
						}
					}
					break;
				case 4:
					tempsprite = null;
					for (Iterator<ZombieSprite> it = sprites5.iterator(); it.hasNext();) {
						ZombieSprite sprite = it.next();
						if (sprite.isKilled())
							continue;
						if (tempx < sprite.x) {
							tempx = sprite.x;
							tempsprite = sprite;
						}
					}
					if (tempsprite != null) {
						if (killSucceed(tempx, ((OwlSprite) spritesOwl.toArray()[rowIndex]).owl)) {
							tempsprite.killed();
							createFreeSpritesHit(rowIndex);
						} else {
							createFreeSpritesMiss(rowIndex);
						}
					}
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int perc10 = (int) (220.0f * StartActivity.deviceWidth / 1200.0f);
	int perc50 = (int) (510.0f * StartActivity.deviceWidth / 1200.0f);
	public static boolean success;

	private void setHitChance(int index, int tempx) {
		try {
			if (tempx < perc10) {
				((HitChanceSprite) freeSprites.toArray()[index]).setHitChanceFar();
			} else if (tempx < perc50) {
				((HitChanceSprite) freeSprites.toArray()[index]).setHitChanceMedium();
			} else {
				((HitChanceSprite) freeSprites.toArray()[index]).setHitChanceClose();
			}
		} catch (Exception e) {
		}
	}

	public int getHitChance(int index) {
		try {
			return ((HitChanceSprite) freeSprites.toArray()[index]).getHitChance();
		} catch (Exception e) {
		}
		return 2;
	}

	private boolean killSucceed(int tempx, Owls owl) {
		Random randomGenerator = new Random();
		int t;
		if (tempx < perc10) {
			if (owl == Owls.owlMachineGun) {
				t = randomGenerator.nextInt(2);
			} else if (owl == Owls.owlSniper) {
				t = randomGenerator.nextInt(4);
			} else {
				t = randomGenerator.nextInt(10);
			}
		} else if (tempx < perc50) {
			if (owl == Owls.owlMachineGun) {
				t = 0;
			} else if (owl == Owls.owlSniper) {
				t = 0;
			} else {
				t = randomGenerator.nextInt(2);
			}
			t = randomGenerator.nextInt(2);
		} else {
			t = 0;
		}
		if (t == 0)
			return true;
		// TODO Auto-generated method stub
		return false;
	}

	public void update() {
		try {
			synchronized (getHolder()) {

				// ----------------------------------------------------------------------
				boolean bt = false;
				int tempx = 0;
				for (Iterator<ZombieSprite> it = sprites.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					// get closest zombie coordinate
					if (!sprite.isKilled()) {
						if (tempx < sprite.x) {
							tempx = sprite.x;
						}
					}

					boolean b = sprite.checkReachedLimit() && !sprite.isKilled();
					bt = bt || b;
					if (b) {
						((OwlSprite) spritesOwl.toArray()[0]).attacked();
					} else {
					}
				}

				// set hit chance for index
				setHitChance(0, tempx);

				if (!bt) {
					((OwlSprite) spritesOwl.toArray()[0]).attackedEnd();
				}

				// ----------------------------------------------------------------------
				bt = false;
				tempx = 0;
				for (Iterator<ZombieSprite> it = sprites2.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					// get closest zombie coordinate
					if (!sprite.isKilled()) {
						if (tempx < sprite.x) {
							tempx = sprite.x;
						}
					}

					boolean b = sprite.checkReachedLimit() && !sprite.isKilled();
					bt = bt || b;
					if (b) {
						((OwlSprite) spritesOwl.toArray()[1]).attacked();
					} else {
					}
				}

				// set hit chance for index
				setHitChance(1, tempx);

				if (!bt) {
					((OwlSprite) spritesOwl.toArray()[1]).attackedEnd();
				}

				// ----------------------------------------------------------------------
				bt = false;
				tempx = 0;
				for (Iterator<ZombieSprite> it = sprites3.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					// get closest zombie coordinate
					if (!sprite.isKilled()) {
						if (tempx < sprite.x) {
							tempx = sprite.x;
						}
					}

					boolean b = sprite.checkReachedLimit() && !sprite.isKilled();
					bt = bt || b;
					if (b) {
						((OwlSprite) spritesOwl.toArray()[2]).attacked();
					} else {
					}
				}

				// set hit chance for index
				setHitChance(2, tempx);

				if (!bt) {
					((OwlSprite) spritesOwl.toArray()[2]).attackedEnd();
				}

				// ----------------------------------------------------------------------
				bt = false;
				tempx = 0;
				for (Iterator<ZombieSprite> it = sprites4.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					// get closest zombie coordinate
					if (!sprite.isKilled()) {
						if (tempx < sprite.x) {
							tempx = sprite.x;
						}
					}

					boolean b = sprite.checkReachedLimit() && !sprite.isKilled();
					bt = bt || b;
					if (b) {
						((OwlSprite) spritesOwl.toArray()[3]).attacked();
					} else {
					}
				}

				// set hit chance for index
				setHitChance(3, tempx);

				if (!bt) {
					((OwlSprite) spritesOwl.toArray()[3]).attackedEnd();
				}

				// ----------------------------------------------------------------------
				bt = false;
				tempx = 0;
				for (Iterator<ZombieSprite> it = sprites5.iterator(); it.hasNext();) {
					ZombieSprite sprite = it.next();
					// get closest zombie coordinate
					if (!sprite.isKilled()) {
						if (tempx < sprite.x) {
							tempx = sprite.x;
						}
					}

					boolean b = sprite.checkReachedLimit() && !sprite.isKilled();
					bt = bt || b;
					if (b) {
						((OwlSprite) spritesOwl.toArray()[4]).attacked();
					} else {
					}
				}

				// set hit chance for index
				setHitChance(4, tempx);

				if (!bt) {
					((OwlSprite) spritesOwl.toArray()[4]).attackedEnd();
				}

				// ----------------------------------------------------------------------

				checkGameEnd();
			}

		} catch (Exception e) {
		}
	}

	private void checkGameEnd() {
		if ((zombies.size() + sprites.size() + sprites2.size() + sprites3.size() + sprites4.size() + sprites5.size()) == 0) {
			success = true;
			finishGame();
		} else if (hitCount >= LIFE_COUNT) {
			success = false;
			finishGame();
		}
	}

	public boolean isZombieWaveStarted() {
		return (sprites.size() + sprites2.size() + sprites3.size() + sprites4.size() + sprites5.size()) != 0;
	}

	public void stopGame() {
		// hitCount = LIFE_COUNT;
		gameLoopThread.setRunning(false);
		releaseBitmaps();
	}

	private void finishGame() {
		gameLoopThread.setRunning(false);
		releaseBitmaps();
		Intent intent = new Intent(context, StageEndActivity.class);
		context.startActivity(intent);
		((GameGameActivity) context).finish();
	}

	public void pause() {
		try {
			if (gameLoopThread.isSleeping())
				return;
			synchronized (gameLoopThread) {
				timePause = new Date().getTime();
				gameLoopThread.setSleeping(true);
			}
		} catch (Exception e) {
		}
	}

	// public void pauseOrResume() {
	// synchronized (gameLoopThread) {
	// if (gameLoopThread.isSleeping()) {
	// resume();
	// } else {
	// pause();
	// }
	// }
	// }

	public void resume() {
		try {
			if (!gameLoopThread.isSleeping())
				return;

			synchronized (gameLoopThread) {
				timeResumed = new Date().getTime();
				gameLoopThread.setSleeping(false);
				gameLoopThread.notify();
			}
		} catch (Exception e) {
		}
		if (r != null) {
			synchronized (r) {
				justcametolife = true;
				r.notify();
			}
		}
	}

	public void attacked() {
		hitCount++;
	}

	public void fire(int index) {
		if (((OwlSprite) spritesOwl.toArray()[index]).owl == Owls.owlSniper) {
			shootAt(index);
			shootAt(index);
			shootAt(index);
		} else {
			shootAt(index);
		}
	}
}
