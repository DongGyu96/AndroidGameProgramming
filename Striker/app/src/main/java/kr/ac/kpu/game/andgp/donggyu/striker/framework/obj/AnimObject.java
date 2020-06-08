package kr.ac.kpu.game.andgp.donggyu.striker.framework.obj;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;

public class AnimObject extends GameObject {
    private static final String TAG = AnimObject.class.getSimpleName();
    protected FrameAnimationBitmap fab;
    protected final RectF dstRect;
    protected final int width, height;

    public AnimObject(float x, float y, int width, int height, int resId, int fps, int count) {
        fab = new FrameAnimationBitmap(resId, fps, count);
        this.x = x;
        this.y = y;
        this.dstRect = new RectF();
        if (width == 0) {
            width = UIBridge.x(fab.getWidth());
        } else if (width < 0) {
            width = UIBridge.x(fab.getWidth()) * -width / 100;
        }
        this.width = width;
        if (height == 0) {
            height = UIBridge.x(fab.getHeight());
        } else if (height < 0) {
            height = UIBridge.x(fab.getHeight()) * -height / 100;
        }
        this.height = height;
    }

    @Override
    public float getRadius() {
        return this.width / 2;
    }

    public void draw(Canvas canvas) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        dstRect.left = x - halfWidth;
        dstRect.top = y - halfHeight;
        dstRect.right = x + halfWidth;
        dstRect.bottom = y + halfHeight;
        fab.draw(canvas, dstRect, null);
    }
}
