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

public class MediumPlane extends BitmapObject implements Recyclable, BoxCollidable {
    protected float dx, dy;
    protected MediumPlane(float x, float y, float dx, float dy) {
        super(x, y, 172, 164, R.mipmap.enemy3);
        this.dx = dx;
        this.dy = dy;
    }

    public static MediumPlane get(float x, float y, float dx, float dy) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        MediumPlane enemy = (MediumPlane) rpool.get(MediumPlane.class);
        if (enemy == null) {
            enemy = new MediumPlane(x, y, dx, dy);
        } else {
            enemy.x = x;
            enemy.y = y;
            enemy.width = 172;
            enemy.height = 164;
            enemy.sbmp = SharedBitmap.load(R.mipmap.enemy3);
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
