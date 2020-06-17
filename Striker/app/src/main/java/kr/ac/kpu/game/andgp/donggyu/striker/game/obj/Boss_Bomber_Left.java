package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;
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
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Boss_Bomber_Left extends AnimObject implements Recyclable, BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 1.2f;
    private float attackCoolTime;
    protected float dx, dy;
    protected Boss_Bomber_Left(float x, float y, float dx, float dy) {
        super(x, y, 55 * 4, 55 * 4, R.mipmap.boss1_left, 60, 8);
        this.dx = dx;
        this.dy = dy;
        this.attackCoolTime = MAX_ATTACK_COOLTIME;
        this.hp = 40;
        fab.reset();
    }

    public static Boss_Bomber_Left get(float x, float y, float dx, float dy) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        Boss_Bomber_Left enemy = (Boss_Bomber_Left) rpool.get(Boss_Bomber_Left.class);
        if (enemy == null) {
            enemy = new Boss_Bomber_Left(x, y, dx, dy);
        } else {
            enemy.x = x;
            enemy.y = y;
            enemy.width = 55 * 4;
            enemy.height = 55 * 4;
            enemy.hp = 40;
            enemy.attackCoolTime = MAX_ATTACK_COOLTIME;
            enemy.fab.setBitmapResource(R.mipmap.boss1_left);
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

        if(y < UIBridge.y(220)) {
            x += dx * seconds;
            y += dy * seconds;
            fab.reset();
            return;
        }

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
                            Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, xDir * 400.f, yDir * 400.f, false, 1));
                }
                attackCoolTime = MAX_ATTACK_COOLTIME;
            }
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

    @Override
    public void draw(Canvas canvas) {
        if (fab.done()) {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;

            Rect srcRect = new Rect();
            srcRect.top = 0;
            srcRect.bottom = 55;
            srcRect.left = 55 * 7;
            srcRect.right = srcRect.left + 55;

            fab.draw(canvas, srcRect, dstRect, null);
        }
        else {
            super.draw(canvas);
        }
    }
}
