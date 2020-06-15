package kr.ac.kpu.game.andgp.donggyu.striker.framework.main;

import android.graphics.Canvas;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Touchable;

public class GameObject {
    protected float x, y;
    protected int hp = 1;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() { return 0; }
    public void update() {}
    public void draw(Canvas canvas) {}

    public void captureTouch() {
        if (!(this instanceof Touchable)) {
            return;
        }
        GameScene.getTop().getGameWorld().captureTouch((Touchable) this);
    }
    public void releaseTouch() {
        GameScene.getTop().getGameWorld().releaseTouch();
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public void remove() {
        GameWorld gw = GameScene.getTop().getGameWorld();
        gw.removeObject(this);
    }

}

