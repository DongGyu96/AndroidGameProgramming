package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Helicopter extends AnimObject implements Recyclable, BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 1.2f;
    private float attackCoolTime;
    protected float dx, dy;
    protected Helicopter(float x, float y, float dx, float dy) {
        super(x, y, 123, 135, R.mipmap.enemy2, 20, 3);
        this.dx = dx;
        this.dy = dy;
        this.attackCoolTime = MAX_ATTACK_COOLTIME;
        this.hp = 1;
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
            enemy.hp = 1;
            enemy.attackCoolTime = MAX_ATTACK_COOLTIME;
            enemy.fab.setBitmapResource(R.mipmap.enemy2);
            enemy.fab.reset();
        }
        return enemy;
    }

    @Override
    public void getBox(RectF rect) {
        int width = UIBridge.x(fab.getWidth()) / 2;
        int height = UIBridge.y(fab.getHeight()) / 2;

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
                ArrayList<GameObject> player = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.player.ordinal());
                float xDir, yDir;
                for (GameObject obj : player) {
                    xDir = obj.getX() - x;
                    yDir = obj.getY() - y;

                    float temp = (xDir*xDir)+(yDir*yDir);
                    temp = (float)Math.sqrt(temp);

                    xDir = xDir / temp;
                    yDir = yDir / temp;

                    SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                            Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, xDir * 400.f, yDir * 400.f, false, 1, 1));
                }
                attackCoolTime = MAX_ATTACK_COOLTIME;
            }
        }

        if(x < -20.f || x > UIBridge.metrics.size.x + 20.f || y > UIBridge.metrics.size.y) {
            remove();
        }

        checkBulletCollision();
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

    public void Damage(int damage) {
        hp -= damage;
        if(hp < 0) {
            remove();
            SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(), Explosion.get(x, y, width, height));
        }
    }
}
