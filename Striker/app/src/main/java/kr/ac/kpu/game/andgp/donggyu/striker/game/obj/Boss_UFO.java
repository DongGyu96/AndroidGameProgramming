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

public class Boss_UFO extends AnimObject implements BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 1.f;
    private static final float MAX_EXPLOSION_COOLTIME = 0.25f;
    private static final float MAX_ATTACK2_COOLTIME = 0.75f;
    private final FrameAnimationBitmap fabOpen;
    private final FrameAnimationBitmap fabFinal;
    private final FrameAnimationBitmap fabDead;
    private final Random random;
    private float explosionCoolTime;
    private State state;
    private float attackCoolTime;
    protected float dx, dy;
    private Boss_UFO_Turret turret = null;
    private boolean turretDead;
    private boolean turretCreate = false;
    private float attack2CoolTime;

    public Boss_UFO(float x, float y, float dx, float dy) {
        super(x, y, 143 * 4, 127 * 4, R.mipmap.boss2, 15, 8);
        this.dx = dx;
        this.dy = dy;
        this.attackCoolTime = MAX_ATTACK_COOLTIME;
        this.attack2CoolTime = MAX_ATTACK2_COOLTIME;
        this.hp = 200;
        fab.reset();
        this.fabOpen = new FrameAnimationBitmap(R.mipmap.boss2_hatchopen, 10, 8);
        this.fabFinal = new FrameAnimationBitmap(R.mipmap.boss2_final, 15, 8);
        this.fabDead = new FrameAnimationBitmap(R.mipmap.boss2_dead, 10, 8);
        this.state = State.Idle;
        this.random = new Random();
        this.explosionCoolTime = MAX_EXPLOSION_COOLTIME;
    }

    private enum State {
        Idle, HatchOpen, Final, Dead, End;
    };

    private void CreateTurret() {
        turret = new Boss_UFO_Turret(x, y, dx, dy, R.mipmap.boss2_hatch);
        turretDead = false;
        turretCreate = true;
        SecondScene.get().getGameWorld().add(SecondScene.Layer.boss.ordinal(), turret);
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
            explosionCoolTime -= seconds;
            if(explosionCoolTime < 0.f) {
                SoundEffects.get().play(R.raw.long_bomb, 0.7f, 0.7f, 3, 0, 1);
                SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(),
                        Explosion.get(x + random.nextInt(200) - 100, y + random.nextInt(200) - 100, 100 + random.nextInt(50), 100 + random.nextInt(50)));
                explosionCoolTime = MAX_EXPLOSION_COOLTIME;
            }
            if(fabDead.done()) {
                y += dy * seconds * 1.5f;
                width -= 40.f * seconds;
                height -= 40.f * seconds;
                if(y > UIBridge.metrics.size.y) {
                    remove();
                    SecondScene.get().addScore(1200);
                    SecondScene.get().pause(false);
                }
            }
            return;
        }

        if(y < UIBridge.y(200)) {
            x += dx * seconds;
            y += dy * seconds;
            return;
        }

        if(y > 0.f) {
            if(!turretCreate) {
                CreateTurret();
                return;
            }
            if(turret.getDead() && turretCreate && state == State.Idle) {
                state = State.HatchOpen;
                fabOpen.reset();
            }
            if(state == State.HatchOpen && fabOpen.done()) {
                state = State.Final;
                fabFinal.reset();
            }

            attackCoolTime -= seconds;
            if(attackCoolTime < 0.f) {
                if(state == State.Final) {
                    int angle = random.nextInt(30);
                    for(int i = 0; i < 36; ++i) {
                        float xPos = x + (float)Math.cos(Math.toRadians((i + 1) * 10) + angle) * 100.f;
                        float yPos = y + (float)Math.sin(Math.toRadians((i + 1) * 10) + angle) * 100.f;
                        float xDir = xPos - x;
                        float yDir = yPos - y;

                        float temp = (xDir * xDir) + (yDir * yDir);
                        temp = (float) Math.sqrt(temp);

                        xDir = xDir / temp;
                        yDir = yDir / temp;

                        SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                                Bullet.get(x, y + UIBridge.y(15), 30, 30, R.mipmap.enemy_bullet, xDir * 400.f, yDir * 400.f, false, 1, 1));
                    }
                }
                SoundEffects.get().play(R.raw.attack, 0.8f, 0.8f, 2, 0, 1);
                attackCoolTime = MAX_ATTACK_COOLTIME;
            }
            attack2CoolTime -= seconds;
            if(attack2CoolTime < 0.f) {
                if(state == State.Final) {
                    ArrayList<GameObject> player = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.player.ordinal());
                    for (GameObject obj : player) {
                        float xDir = obj.getX() - x;
                        float yDir = obj.getY() - y;

                        float temp = (xDir * xDir) + (yDir * yDir);
                        temp = (float) Math.sqrt(temp);

                        xDir = xDir / temp;
                        yDir = yDir / temp;

                        SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy_bullet.ordinal(),
                                Bullet.get(x, y, 60, 60, R.mipmap.enemy_bullet, xDir * 500.f, yDir * 500.f, false, 1, 1));
                    }
                    SoundEffects.get().play(R.raw.attack, 0.8f, 0.8f, 2, 0, 1);
                    attack2CoolTime = MAX_ATTACK_COOLTIME;
                }
            }
        }

        checkBulletCollision();
    }

    private void checkBulletCollision() {
        if(this.turret == null) {
            return;
        }
        if(!turret.getDead()) {
            return;
        }
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
            fabDead.reset();
            state = State.Dead;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(state == State.Idle) {
            super.draw(canvas);
        }
        else if(state == State.HatchOpen) {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;
            fabOpen.draw(canvas, dstRect, null);
        }
        else if(state == State.Final) {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;
            fabFinal.draw(canvas, dstRect, null);
        }
        else if(state == State.Dead) {
            if(fabDead.done()) {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;

                Rect srcRect = new Rect();
                srcRect.top = 0;
                srcRect.bottom = 127;
                srcRect.left = 143 * 7;
                srcRect.right = srcRect.left + 143;

                fabDead.draw(canvas, srcRect, dstRect, null);
            }
            else {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;
                fabDead.draw(canvas, dstRect, null);
            }
        }
    }
}
