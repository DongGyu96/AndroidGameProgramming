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
    private boolean animated;
    private int power;
    protected float dx, dy;
    protected Bullet(float x, float y, int width, int height, int resId, float dx, float dy, boolean animated, int power, int count) {
        super(x, y, width, height, resId, 10, count);
        this.dx = dx;
        this.dy = dy;
        this.power = power;
        this.animated = animated;
        this.fab.reset();
    }

    public static Bullet get(float x, float y, int width, int height, int resId, float dx, float dy, boolean animated, int power, int count) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        Bullet bullet = (Bullet) rpool.get(Bullet.class);
        if (bullet == null) {
            bullet = new Bullet(x, y, width, height, resId, dx, dy, animated, power, count);
        } else {
            bullet.x = x;
            bullet.y = y;
            bullet.width = width;
            bullet.height = height;
            bullet.fab.setBitmapResource(resId);
            bullet.fab.setFrames(count);
            bullet.dx = dx;
            bullet.dy = dy;
            bullet.power = power;
            bullet.fab.reset();
            bullet.animated = animated;
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
        x += dx * seconds;
        if(y < 0 || y > UIBridge.metrics.size.y) {
            SecondScene.get().getGameWorld().removeObject(this);
        }
    }

    @Override
    public void getBox(RectF rect) {
        int width = UIBridge.x(fab.getWidth()) / 2;
        int height = UIBridge.y(fab.getHeight()) / 2;

        int hw = width / 2;
        int hh = height / 2;
        rect.left = x - hw;
        rect.top = y - hh;
        rect.right = x + hw;
        rect.bottom = y + hh;
    }

    @Override
    public void draw(Canvas canvas) {
        if(animated) {
            if (fab.done()) {
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
            } else {
                super.draw(canvas);
            }
        }
        else
            super.draw(canvas);
    }

    public int getPower() {return power;}
}
