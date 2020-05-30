package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.world.MainWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.bitmap.FrameAnimationBitmap;

public class Enemy implements GameObject, BoxCollidable, Recyclable {
    public static final int FRAMES_PER_SECOND = 12;
    private static final String TAG = Enemy.class.getSimpleName();
    public static int[] RES_IDS = {
            R.mipmap.enemy_01, R.mipmap.enemy_02, R.mipmap.enemy_03, R.mipmap.enemy_04,
            R.mipmap.enemy_05, R.mipmap.enemy_06, R.mipmap.enemy_07, R.mipmap.enemy_08,
            R.mipmap.enemy_09, R.mipmap.enemy_10, R.mipmap.enemy_11, R.mipmap.enemy_12,
            R.mipmap.enemy_13, R.mipmap.enemy_14, R.mipmap.enemy_15, R.mipmap.enemy_16,
            R.mipmap.enemy_17, R.mipmap.enemy_18, R.mipmap.enemy_19, R.mipmap.enemy_20,
    };
    private FrameAnimationBitmap fab;
    private int height;
    private float x, y;
    private int speed;
    private int life;
    private Paint paint = new Paint();
    private int score;

    private Enemy() {

    }

    public static Enemy get (int x, int level, int speed) {
        level--;
        if (level >= RES_IDS.length) {
            level = RES_IDS.length - 1;
        }
        int resId = RES_IDS[level];

        MainWorld gw = MainWorld.get();

        Enemy e = (Enemy)gw.getRecyclePool().get(Enemy.class);
        if(e == null) {
            e = new Enemy();
        }

        e.fab = new FrameAnimationBitmap(resId, FRAMES_PER_SECOND, 0);
        e.height = e.fab.getHeight();
        e.x = x;
        e.y = -e.height;
        e.speed = speed;
        e.life = (level + 1) * 100;
        e.score = (level + 1) * 100;

        e.paint.setColor(Color.BLACK);
        e.paint.setTextSize(50);

        return e;
    }
    @Override
    public void update() {
//        Log.d(TAG, "update() - " + this);
//        Log.d(TAG, "update() x=" + x + " y=" + y + " - " + this);
        GameWorld gw = GameWorld.get();
        y += speed * gw.getTimeDiffInSecond();
        if (y > gw.getBottom() + height) {
            gw.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
//        Log.d(TAG, "x=" + x + " y=" + y + " - " + this);
        fab.draw(canvas, x, y);
        canvas.drawText(String.valueOf(life), x - height / 4, y + height / 3, paint);
    }

    @Override
    public void getBox(RectF rect) {
        int hw = fab.getWidth() / 2;
        int hh = fab.getHeight() / 2;
        rect.left = x - hw;
        rect.right = x + hw;
        rect.top = y - hh;
        rect.bottom = y + hh;
    }

    public void decreaseLift(int power) {
        this.life -= power;
        if(life <= 0) {
            MainWorld gw = MainWorld.get();
            gw.remove(this);
            gw.addScore(this.score);
        }
    }

    @Override
    public void recycle() {

    }
}
