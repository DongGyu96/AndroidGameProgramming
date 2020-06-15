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
    private static final float MAX_ATTACK_COOLTIME = 0.5f;
    private float attackCoolTime;
    protected float dx, dy;
    protected MediumPlane(float x, float y, float dx, float dy) {
        super(x, y, 172, 164, R.mipmap.enemy3);
        this.dx = dx;
        this.dy = dy;
        this.attackCoolTime = MAX_ATTACK_COOLTIME;
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
            enemy.attackCoolTime = MAX_ATTACK_COOLTIME;
            enemy.sbmp = SharedBitmap.load(R.mipmap.enemy3);
        }
        return enemy;
    }

    @Override
    public void getBox(RectF rect) {
        int width = UIBridge.x(sbmp.getWidth()) / 2;
        int height = UIBridge.y(sbmp.getHeight()) / 2;

        int hw = width / 2;
        int hh = height / 2;
        rect.left = x - hw;
        rect.top = y - hh;
        rect.right = x + hw;
        rect.bottom = y + hh;
    }

    @Override
    public void recycle() {

    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();
        x += dx * seconds;
        y += dy * seconds;

        if(y > 0.f) {
            attackCoolTime -= seconds;
            if(attackCoolTime < 0.f) {
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(), Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, 0.f, 500.f, false));
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(), Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, 25.f, 500.f, false));
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(), Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, -25.f, 500.f, false));
                attackCoolTime = MAX_ATTACK_COOLTIME;
            }
        }

        if(x < -50.f || x > UIBridge.metrics.size.x + 50.f || y > UIBridge.metrics.size.y) {
            SecondScene.get().getGameWorld().removeObject(this);
        }
    }
}
