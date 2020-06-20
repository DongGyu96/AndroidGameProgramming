package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Boss_Bomber extends AnimObject implements BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 1.2f;
    private static final float MAX_EXPLOSION_COOLTIME = 0.25f;
    private final FrameAnimationBitmap fabDead;
    private final Random random;
    private float explosionCoolTime;
    private State state;
    private float attackCoolTime;
    protected float dx, dy;
    private Boss_Bomber_Wing leftWing;
    private Boss_Bomber_Wing rightWing;

    public Boss_Bomber(float x, float y, float dx, float dy) {
        super(x, y, 204 * 4, 110 * 4, R.mipmap.boss1, 20, 7);
        this.dx = dx;
        this.dy = dy;
        this.attackCoolTime = MAX_ATTACK_COOLTIME;
        this.hp = 80;
        fab.reset();
        this.fabDead = new FrameAnimationBitmap(R.mipmap.boss1_dead, 0, 1);
        this.state = State.Idle;
        this.random = new Random();
        this.explosionCoolTime = MAX_EXPLOSION_COOLTIME;
        AddLeftRightWing();
    }

    private enum State {
        Idle, Dead, End
    };

    private void AddLeftRightWing() {
        leftWing = new Boss_Bomber_Wing(x - UIBridge.x(110), y + UIBridge.y(30), dx, dy, R.mipmap.boss1_left);
        SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(), leftWing);

        rightWing = new Boss_Bomber_Wing(x + UIBridge.x(115), y + UIBridge.y(30), dx, dy, R.mipmap.boss1_right);
        SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(), rightWing);
    }

    @Override
    public void getBox(RectF rect) {
        int width = UIBridge.x(fab.getWidth()) / 2;
        int height = UIBridge.y(fab.getHeight()) / 2;

        int hw = width;
        int hh = height / 2;
        rect.left = x - hw;
        rect.top = y - hh;
        rect.right = x + hw;
        rect.bottom = y + hh;
    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();

        if(state == State.Dead) {
            y += dy * seconds * 1.5f;
            width -= 40.f * seconds;
            height -= 40.f * seconds;
            explosionCoolTime -= seconds;
            if(explosionCoolTime < 0.f) {
                SoundEffects.get().play(R.raw.long_bomb2, 0.7f, 0.7f, 1, 0, 1);
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(),
                        Explosion.get(x + random.nextInt(200) - 100, y + random.nextInt(200) - 100, 100 + random.nextInt(50), 100 + random.nextInt(50)));
                explosionCoolTime = MAX_EXPLOSION_COOLTIME;
            }
            if(y > UIBridge.metrics.size.y) {
                remove();
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(), Explosion.get(x, y, width, height));
                SecondScene.get().addScore(1200);
                SecondScene.get().pause(false);
            }
            return;
        }

        if(y < UIBridge.y(200)) {
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
                            Bullet.get(x, y, 30, 30, R.mipmap.enemy_bullet, xDir * 400.f, yDir * 400.f, false, 1, 1));
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
            leftWing.Damage(9999);
            rightWing.Damage(9999);
            this.state = State.Dead;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(state == State.Dead) {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;

            fabDead.draw(canvas, dstRect, null);
            return;
        }
        if (fab.done()) {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;

            Rect srcRect = new Rect();
            srcRect.top = 0;
            srcRect.bottom = 110;
            srcRect.left = 204 * 6;
            srcRect.right = srcRect.left + 204;

            fab.draw(canvas, srcRect, dstRect, null);
        }
        else {
            super.draw(canvas);
        }
    }
}
