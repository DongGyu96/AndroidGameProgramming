package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.RectF;
import android.util.Log;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;

public class Bullet extends AnimObject implements Recyclable, BoxCollidable {
    protected float dx, dy;
    protected Bullet(float x, float y, int width, int height, int resId, float dx, float dy) {
        super(x, y, width, height, resId, 10, 0);
        this.dx = dx;
        this.dy = dy;
    }

    public static Bullet get(float x, float y, int width, int height, int typeIndex) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        Bullet bullet = (Bullet) rpool.get(Bullet.class);
        if (bullet == null) {
            bullet = new Bullet(x, y, width, height, typeIndex);
        } else {
            bullet.x = x;
            bullet.y = y;
            bullet.width = width;
            bullet.height = height;
            bullet.fab = FrameAnimationBitmap.load(RES_IDS[typeIndex]);
        }
        return bullet;
    }

    @Override
    public void recycle() {

    }

    @Override
    public void update() {

    }
}
