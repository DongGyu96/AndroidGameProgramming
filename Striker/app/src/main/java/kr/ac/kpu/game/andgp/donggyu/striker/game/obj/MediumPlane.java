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

public class MediumPlane extends BitmapObject implements Recyclable, BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 1.75f;
    private float attackCoolTime;
    protected float dx, dy;
    protected MediumPlane(float x, float y, float dx, float dy) {
        super(x, y, 172, 164, R.mipmap.enemy3);
        this.dx = dx;
        this.dy = dy;
        hp = 3;
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
            enemy.hp = 4;
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
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                        Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, 0.f, 500.f, false, 1, 1));
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                        Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, 75.f, 500.f, false, 1, 1));
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                        Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, -75.f, 500.f, false, 1, 1));
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
            SecondScene.get().addScore(250);
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
