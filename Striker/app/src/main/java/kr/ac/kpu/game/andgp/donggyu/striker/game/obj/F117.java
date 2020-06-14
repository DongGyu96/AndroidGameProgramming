package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Touchable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class F117 extends AnimObject implements Touchable {
    private static final int MAX_POWER = 4;
    private static final float ATTACK_COOL_TIME = 0.20f;
    private float attackCoolTime;
    private State state;
    private final int power;
    private final FrameAnimationBitmap fabRight;
    private final FrameAnimationBitmap fabLeft;
    protected float dx, dy;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            int cx = UIBridge.metrics.center.x;
            if(e.getX() > cx) {
                state = State.right;
                fabRight.reset();
                fabLeft.reset();
            }
            else {
                state = State.left;
                fabRight.reset();
                fabLeft.reset();
            }
            return true;
        }
        if(action == MotionEvent.ACTION_MOVE) {
            int cx = UIBridge.metrics.center.x;

            if(e.getX() > cx) {
                state = State.right;
                fabLeft.reset();
            }
            else {
                state = State.left;
                fabRight.reset();
            }
            return true;
        }
        if(action == MotionEvent.ACTION_UP) {
            state = State.idle;
            return false;
        }
        return true;
    }

    private enum State {
        idle, left, right, skill, boost
    }

    private final int[] BULLET_IMAGE = {
            R.mipmap.attack1_f117,
            R.mipmap.attack2_f117,
            R.mipmap.attack3_f117,
            R.mipmap.attack4_f117,
    };

    public F117(float x, float y, float dx, float dy) {
        super(x, y, 124, 192, R.mipmap.f117, 10, 7);
        this.fabRight = new FrameAnimationBitmap(R.mipmap.f117_right, 10, 3);
        this.fabLeft = new FrameAnimationBitmap(R.mipmap.f117_left, 10, 3);
        this.fabLeft.SetReverse(true);
        this.dx = dx;
        this.dy = dy;
        this.state = State.idle;
        this.power = 1;
        this.attackCoolTime = ATTACK_COOL_TIME;
    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();
        if(state == State.right) {
            if(x < UIBridge.metrics.size.x) {
                x += dx * seconds;
            }
        }
        else if(state == State.left) {
            if(x > 0) {
                x -= dx * seconds;
            }
        }

        attackCoolTime -= seconds;
        if(attackCoolTime < 0.f) {
            SecondScene.get().getGameWorld().add(SecondScene.Layer.bullet.ordinal(), Bullet.get(x, y, 92, 164, BULLET_IMAGE[power - 1], 0.f, -800.f));
            attackCoolTime = ATTACK_COOL_TIME;
        }
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
        else if(state == State.left) {
            if(fabLeft.done())
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
                srcRect.left = 31 * 0;
                srcRect.right = srcRect.left + 31;

                fabLeft.draw(canvas, srcRect, dstRect, null);
            }
            else {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;
                fabLeft.draw(canvas, dstRect, null);
            }
        }
        else if(state == State.right) {
            if(fabRight.done()) {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;

                Rect srcRect = new Rect();
                srcRect.top = 0;
                srcRect.bottom = 48;
                srcRect.left = 31 * 2;
                srcRect.right = srcRect.left + 31;

                fabRight.draw(canvas, srcRect, dstRect, null);
            }
            else {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;
                fabRight.draw(canvas, dstRect, null);
            }
        }
    }
}
