package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.SharedBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class SmallPlane extends BitmapObject implements Recyclable, BoxCollidable {
    protected float dx, dy;
    protected SmallPlane(float x, float y, float dx, float dy) {
        super(x, y, 132, 108, R.mipmap.enemy1);
        this.dx = dx;
        this.dy = dy;
    }

    public static SmallPlane get(float x, float y, float dx, float dy) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        SmallPlane enemy = (SmallPlane) rpool.get(SmallPlane.class);
        if (enemy == null) {
            enemy = new SmallPlane(x, y, dx, dy);
        } else {
            enemy.x = x;
            enemy.y = y;
            enemy.width = 132;
            enemy.height = 108;
            enemy.sbmp = SharedBitmap.load(R.mipmap.enemy1);
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
