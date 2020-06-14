package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Bullet extends AnimObject implements Recyclable, BoxCollidable {
    protected float dx, dy;
    protected Bullet(float x, float y, int width, int height, int resId, float dx, float dy) {
        super(x, y, width, height, resId, 60, 0);
        this.dx = dx;
        this.dy = dy;
        fab.reset();
    }

    public static Bullet get(float x, float y, int width, int height, int resId, float dx, float dy) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        Bullet bullet = (Bullet) rpool.get(Bullet.class);
        if (bullet == null) {
            bullet = new Bullet(x, y, width, height, resId, dx, dy);
        } else {
            bullet.x = x;
            bullet.y = y;
            bullet.width = width;
            bullet.height = height;
            bullet.fab.setBitmapResource(resId);
            bullet.fab.reset();
        }
        return bullet;
    }

    @Override
    public void recycle() {

    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();
        y += dy * seconds;
        if(y < 0 || y > UIBridge.metrics.size.y) {
            SecondScene.get().getGameWorld().removeObject(this);
        }
    }

    @Override
    public void getBox(RectF rect) {

    }

    @Override
    public void draw(Canvas canvas) {
        if(fab.done())
        {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;

            Rect srcRect = new Rect();
            srcRect.top = 0;
            srcRect.bottom = 41;
            srcRect.left = 23 * 3;
            srcRect.right = srcRect.left + 23;

            fab.draw(canvas, srcRect, dstRect, null);
        }
        else {
            super.draw(canvas);
        }
    }
}
