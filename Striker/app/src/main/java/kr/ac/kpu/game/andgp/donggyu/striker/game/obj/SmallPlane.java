package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.SharedBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class SmallPlane extends BitmapObject implements Recyclable, BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 1.f;
    private float attackCoolTime;
    protected float dx, dy;
    protected SmallPlane(float x, float y, float dx, float dy) {
        super(x, y, 132, 108, R.mipmap.enemy1);
        this.dx = dx;
        this.dy = dy;
        this.hp = 1;
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
            enemy.hp = 1;
            enemy.sbmp = SharedBitmap.load(R.mipmap.enemy1);
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
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                        Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, 0.f, 500.f, false, 1, 1));
                attackCoolTime = MAX_ATTACK_COOLTIME;
            }
        }

        if(x < -20.f || x > UIBridge.metrics.size.x + 20.f || y > UIBridge.metrics.size.y) {
            remove();
        }

        checkBulletCollision();
    }

    public void Damage(int damage) {
        hp -= damage;
        if(hp < 0) {
            remove();
            SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(), Explosion.get(x, y, width, height));
        }
    }

    private void checkBulletCollision() {
        ArrayList<GameObject> bullets = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.bullet.ordinal());
        for (GameObject obj : bullets) {
            if (!(obj instanceof Bullet)) {
                continue;
            }
            Bullet bullet = (Bullet) obj;
            if (CollisionHelper.collides(this, bullet)) {
                bullet.remove();
                Damage(bullet.getPower());
            }
        }
    }
}
