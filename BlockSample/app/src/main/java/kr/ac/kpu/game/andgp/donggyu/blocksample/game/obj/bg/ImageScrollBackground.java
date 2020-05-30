package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.bg;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.ScrollableObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.bitmap.SharedBitmap;

public class ImageScrollBackground implements GameObject, ScrollableObject {
    private final SharedBitmap sbmp;
//    private final int pageSize;
    private int speed;
    private float scrollX;
    private float scrollY;
    private boolean horizontal;
    private Rect srcRect = new Rect();
    private RectF dstRect = new RectF();

    public enum Orientation { horizontal, vertical };
    public ImageScrollBackground(int resId, Orientation orientation, int speed) {
        this.sbmp = SharedBitmap.load(resId);
        this.horizontal = orientation == Orientation.horizontal;
        this.speed = speed;
        GameWorld gw = GameWorld.get();
        int screenWidth = gw.getRight() - gw.getLeft();
        int screenHeight = gw.getBottom() - gw.getTop();
//        if (orientation == Orientation.vertical) {
//            pageSize = sbmp.getHeight() * screenWidth / sbmp.getWidth();
//        } else {
//            pageSize = sbmp.getWidth() * screenHeight / sbmp.getHeight();
//        }
        srcRect.set(0, 0, sbmp.getWidth(), sbmp.getHeight());
    }

    @Override
    public void update() {
        if (speed == 0) return;
        GameWorld gw = GameWorld.get();
        float amount = speed * gw.getTimeDiffInSecond();
        if (horizontal) {
            scrollX += amount;
        } else {
            scrollY += amount;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (horizontal)
            drawHorizontal(canvas);
        else
            drawVertical(canvas);
    }

    private void drawVertical(Canvas canvas) {
        GameWorld gw = GameWorld.get();
        int left = gw.getLeft();
        int top = gw.getTop();
        int right = gw.getRight();
        int bottom = gw.getBottom();
        int pageSize = sbmp.getHeight() * (right - left) / sbmp.getWidth();

        canvas.save();
        canvas.clipRect(left, top, right, bottom);

        float curr = scrollY % pageSize;
        if (curr > 0) curr -= pageSize;
        curr += top;
        while (curr < bottom) {
            dstRect.set(left, curr, right, curr + pageSize);
            curr += pageSize;
            canvas.drawBitmap(sbmp.getBitmap(), srcRect, dstRect, null);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas) {
        GameWorld gw = GameWorld.get();
        int left = gw.getLeft();
        int top = gw.getTop();
        int right = gw.getRight();
        int bottom = gw.getBottom();
        int pageSize = sbmp.getWidth() * (bottom - top) / sbmp.getHeight();

        canvas.save();
        canvas.clipRect(left, top, right, bottom);

        float curr = scrollX % pageSize;
        if (curr > 0) curr -= pageSize;
        curr += left;
        while (curr < right) {
            dstRect.set(curr, top, curr + pageSize, bottom);
            curr += pageSize;
            canvas.drawBitmap(sbmp.getBitmap(), srcRect, dstRect, null);
        }
        canvas.restore();
    }

    @Override
    public void scrollTo(int x, int y) {
        this.scrollX = x;
        this.scrollY = y;
    }
}
