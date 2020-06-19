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
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Boss_UFO extends AnimObject implements BoxCollidable {
    private static final float MAX_ATTACK_COOLTIME = 1.2f;
    private final FrameAnimationBitmap fabOpen;
    private final FrameAnimationBitmap fabFinal;
    private final FrameAnimationBitmap fabDead;
    private final Random random;
    private State state;
    private float attackCoolTime;
    protected float dx, dy;
    private Boss_UFO_Turret turret = null;
    private boolean turretDead;
    private boolean turretCreate = false;

    public Boss_UFO(float x, float y, float dx, float dy) {
        super(x, y, 143 * 4, 127 * 4, R.mipmap.boss2, 15, 8);
        this.dx = dx;
        this.dy = dy;
        this.attackCoolTime = MAX_ATTACK_COOLTIME;
        this.hp = 50;
        fab.reset();
        this.fabOpen = new FrameAnimationBitmap(R.mipmap.boss2_hatchopen, 10, 8);
        this.fabFinal = new FrameAnimationBitmap(R.mipmap.boss2_final, 15, 8);
        this.fabDead = new FrameAnimationBitmap(R.mipmap.boss2_dead, 10, 8);
        this.state = State.Idle;
        this.random = new Random();
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
        if(state == State.Dead) {
            SecondScene.get().getGameWorld().add(SecondScene.Layer.enemy.ordinal(),
                    Explosion.get(x + random.nextInt(200) - 100, y + random.nextInt(200) - 100, 100 + random.nextInt(50), 100 + random.nextInt(50)));
            if(fabDead.done()) {
                remove();
                SecondScene.get().addScore(1200);
                SecondScene.get().pause(false);
            }
            return;
        }
        float seconds = GameTimer.getTimeDiffSeconds();

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
