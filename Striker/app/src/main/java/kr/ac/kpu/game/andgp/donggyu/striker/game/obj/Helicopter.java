package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Helicopter extends AnimObject implements Recyclable, BoxCollidable {
    protected float dx, dy;
    protected Helicopter(float x, float y, float dx, float dy) {
        super(x, y, 123, 135, R.mipmap.enemy2, 20, 3);
        this.dx = dx;
        this.dy = dy;
        fab.reset();
    }

    public static Helicopter get(float x, float y, float dx, float dy) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        Helicopter enemy = (Helicopter) rpool.get(Helicopter.class);
        if (enemy == null) {
            enemy = new Helicopter(x, y, dx, dy);
        } else {
            enemy.x = x;
            enemy.y = y;
            enemy.width = 123;
            enemy.height = 135;
            enemy.fab.setBitmapResource(R.mipmap.enemy2);
            enemy.fab.reset();
        }
        return enemy;
    }

    @Override
    public void getBox(RectF rect) {

    }

    @Override
    public void recycle() {

    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();
        x += dx * seconds;
        y += dy * seconds;
        if(x < -50.f || x > UIBridge.metrics.size.x + 50.f || y > UIBridge.metrics.size.y) {
            SecondScene.get().getGameWorld().removeObject(this);
        }
    }
}
