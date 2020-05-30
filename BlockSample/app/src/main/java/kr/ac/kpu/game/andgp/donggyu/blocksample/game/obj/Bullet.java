package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.world.MainWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.bitmap.FrameAnimationBitmap;

public class Bullet implements GameObject, BoxCollidable, Recyclable {
    public static final int FRAME_PER_SECOND = 6;
    public static final int FRAME_COUNT = 13;
    private static final float BULLET_SPEED = 1500;
    private int halfSize;
    private FrameAnimationBitmap fab;
    private float x;
    private float y;
    private int power;

    private Bullet() {

    }

    public static Bullet get(float x, float y)
    {
        GameWorld gw = GameWorld.get();
        Bullet b = (Bullet)gw.getRecyclePool().get(Bullet.class);
        if(b == null) {
            b = new Bullet();
        }
        b.fab = new FrameAnimationBitmap(R.mipmap.metal_slug_missile, FRAME_PER_SECOND, FRAME_COUNT);
        b.halfSize = b.fab.getHeight() / 2;
        b.x = x;
        b.y = y;
        b.power = 100;

        return b;
    }

    @Override
    public void update()
    {
        MainWorld gw = MainWorld.get();
//        x += dx;
//        if(dx > 0 && x > gw.getRight() - halfSize || dx < 0 && x < gw.getLeft() + halfSize)
//        {
//            dx *= -1;
//        }
        y -= BULLET_SPEED * gw.getTimeDiffInSecond();

        boolean toBeDeleted = false;
        ArrayList<GameObject> enemies = gw.objectsAtLayer(MainWorld.Layer.Enemy);
        for(GameObject e : enemies) {
            if(!(e instanceof Enemy)) {
                //Log.e()
                continue;
            }
            Enemy enemy = (Enemy) e;
            if(CollisionHelper.collides(enemy, this)) {
                enemy.decreaseLift(this.power);
                toBeDeleted = true;
                break;
            }
        }

        if(!toBeDeleted) {
            if (y < gw.getTop() - halfSize) {
                toBeDeleted = true;
            }
        }

        if(toBeDeleted) {
            gw.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        fab.draw(canvas, x, y);
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

    @Override
    public void recycle() {

    }
}
