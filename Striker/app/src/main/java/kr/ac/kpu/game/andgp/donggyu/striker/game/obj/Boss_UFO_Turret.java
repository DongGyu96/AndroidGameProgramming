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
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Boss_UFO_Turret extends AnimObject implements BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 0.8f;
    private boolean dead;
    private float attackCoolTime;
    protected float dx, dy;
    public Boss_UFO_Turret(float x, float y, float dx, float dy, int resId) {
        super(x, y, 57 * 4, 27 * 4, resId, 20, 8);
        this.dx = dx;
        this.dy = dy;
        this.attackCoolTime = MAX_ATTACK_COOLTIME;
        this.hp = 150;
        fab.reset();
        this.dead = false;
    }

    public boolean getDead() {
        return dead;
    }
    @Override
    public void getBox(RectF rect) {
        int width = UIBridge.x(fab.getWidth());
        int height = UIBridge.y(fab.getHeight()) / 2;

        int hw = width / 2;
        int hh = height / 2;
        rect.left = x - hw;
        rect.top = y - hh;
        rect.right = x + hw;
        rect.bottom = y + hh;
    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();

        if(!fab.done()) {
            return;
        }

        if(y > 0.f) {
            attackCoolTime -= seconds;
            if(attackCoolTime < 0.f) {
                ArrayList<GameObject> player = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.player.ordinal());
                float xDir, yDir;
                for (GameObject obj : player) {
                    for(int i = -3; i <= 3; ++i) {
                        xDir = (obj.getX() + (UIBridge.x(50) * i)) - x;
                        yDir = obj.getY() - y;

                        float temp = (xDir * xDir) + (yDir * yDir);
                        temp = (float) Math.sqrt(temp);

                        xDir = xDir / temp;
                        yDir = yDir / temp;

                        SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                                Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, xDir * 400.f, yDir * 400.f, false, 1, 1));
                    }
                }
                SoundEffects.get().play(R.raw.attack2, 0.8f, 0.8f, 2, 0, 1);
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
            dead = true;
            SoundEffects.get().play(R.raw.long_bomb2, 0.9f, 0.9f, 3, 0, 1);
            SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(), Explosion.get(x, y, width, height));
            SecondScene.get().addScore(550);
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
            srcRect.bottom = 27;
            srcRect.left = 57 * 7;
            srcRect.right = srcRect.left + 57;

            fab.draw(canvas, srcRect, dstRect, null);
        }
        else {
            super.draw(canvas);
        }
    }
}
