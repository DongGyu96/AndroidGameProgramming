package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;

public class F117 extends AnimObject {
    private static final int MAX_POWER = 4;
    private final State state;
    private final int power;
    protected float dx, dy;

    private enum State {
        idle, left, right, skill, boost
    }

    public F117(float x, float y, float dx, float dy) {
        super(x, y, 124, 192, R.mipmap.f117, 10, 7);
        this.dx = dx;
        this.dy = dy;
        this.state = State.idle;
        this.power = 1;
    }

    public void update() {


    }

    @Override
    public void draw(Canvas canvas) {
        if(state == State.idle)
        {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;

            Rect srcRect = new Rect();
            srcRect.top = 0;
            srcRect.bottom = 48;
            srcRect.left = 31 * 3;
            srcRect.right = srcRect.left + 31;

            fab.draw(canvas, srcRect, dstRect, null);
        }
    }
}
