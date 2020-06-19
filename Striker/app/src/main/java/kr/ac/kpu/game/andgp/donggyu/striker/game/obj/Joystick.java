package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Touchable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.SharedBitmap;

public class Joystick extends GameObject implements Touchable {
    private static final String TAG = Joystick.class.getSimpleName();
    private static final float MAX_SHOW_TIME = 0.1f;
    private final SharedBitmap sbmp;
    private final int size;
    private final Direction direction;
    private final SharedBitmap handle;
    private float showTime;
    private boolean down;
    private float xDown, yDown;
    private float dx, dy;
    private double angle;
    private float draw_y;
    private float draw_x;

    public enum Direction {
        normal, horizontal, vertical
    }
    public Joystick(float x, float y, Direction dir, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.direction = dir;
        this.sbmp = SharedBitmap.load(R.mipmap.joystick);
        this.handle = SharedBitmap.load(R.mipmap.joystick_handle);
        this.down = false;
        this.showTime = MAX_SHOW_TIME;
    }

    @Override
    public void update() {
        float second = GameTimer.getTimeDiffSeconds();
        if(down) {
            showTime -= second;
            if(showTime < 0.f) {
                x = -500;
                y = -500;
                draw_x = -500;
                draw_y = -500;
                dx = 0;
                dy = 0;
                stickMove(0, 0);
                down = false;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sbmp.draw(canvas, draw_x, draw_y);
        if (down) {
            Log.d(TAG, "angle = " + angle + " dx=" + dx + " dy=" + dy);
            handle.draw(canvas, draw_x + dx + UIBridge.x(25), draw_y + dy + UIBridge.y(25));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = UIBridge.x((int)event.getX());
                yDown = UIBridge.y((int)event.getY());
                draw_x = event.getX() - UIBridge.x(50);
                draw_y = event.getY() - UIBridge.y(50);
                dx = dy = 0;
                x = UIBridge.x((int)event.getX());
                y = UIBridge.y((int)event.getY());
                down = true;
                showTime = MAX_SHOW_TIME;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = UIBridge.x((int)event.getX()) - xDown;
                float dy = UIBridge.y((int)event.getY()) - yDown;
                stickMove(dx, dy);
                showTime = MAX_SHOW_TIME;
                return true;
            case MotionEvent.ACTION_UP:
                x = -500;
                y = -500;
                draw_x = -500;
                draw_y = -500;
                dx = 0;
                dy = 0;
                stickMove(0, 0);
                down = false;
                return false;
        }
        return false;
    }

    private void stickMove(float dx, float dy) {
        if (direction == Direction.vertical) {
            dx = 0;
            if (dy < -size) {
                dy = -size;
            } else if (dy > size) {
                dy = size;
            }
        } else if (direction == Direction.horizontal) {
            dy = 0;
            if (dx < -size) {
                dx = -size;
            } else if (dx > size) {
                dx = size;
            }
        } else {
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > size) {
                dx = dx * size / dist;
                dy = dy * size / dist;
            }
        }
        this.dx = dx;
        this.dy = dy;
        this.angle = Math.atan2(dy, dx);
//        Log.d(TAG, "angle = " + angle + " dx=" + dx + " dy=" + dy);
    }

    public float getHorzDirection() {
        return dx / size;
    }

    public float getVerticalDirection() {
        return dy / size;
    }

}
