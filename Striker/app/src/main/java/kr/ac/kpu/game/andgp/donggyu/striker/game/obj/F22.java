package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.GameOverScene;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class F22 extends AnimObject implements BoxCollidable {
    private static final int MAX_POWER = 3;
    private static final float ATTACK_COOL_TIME = 0.1f;
    private static final float INVINCIBLE_TIME = 3.f;
    private static final int MAX_SKILL_COUNT = 7;
    private static final int MAX_HP = 5;
    private final FrameAnimationBitmap fabSkill;
    private final FrameAnimationBitmap fabSkillIcon;
    private final FrameAnimationBitmap fabHpIcon;
    private final FrameAnimationBitmap fabName;
    private int skillCount;
    private boolean invincible;
    private float invincibleTime;
    private int invincibleCount;
    private float attackCoolTime;
    private State state;
    private final int power;
    private final FrameAnimationBitmap fabRight;
    private final FrameAnimationBitmap fabLeft;
    protected float dx, dy;
    private Joystick joystick;
    private Random random;

    @Override
    public void getBox(RectF rect) {
        int width = UIBridge.x(fab.getWidth()) / 2;
        int height = UIBridge.y(fab.getHeight()) / 2;

        int hw = width / 4;
        int hh = height / 4;
        rect.left = x - hw;
        rect.top = y - hh;
        rect.right = x + hw;
        rect.bottom = y + hh;
    }

    public void activeSkill() {
        if(state != State.skill) {
            if(skillCount > 0) {
                skillCount--;
                state = State.skill;
                fabSkill.reset();
            }
        }
    }

    public void restart() {
        int sh = UIBridge.metrics.size.y;
        int cx = UIBridge.metrics.center.x;

        hp = MAX_HP;
        skillCount = MAX_SKILL_COUNT;

        x = cx;
        y = sh - UIBridge.x(100);;
    }

    private enum State {
        idle, left, right, skill, boost
    }

    private final int[] BULLET_IMAGE = {
            R.mipmap.attack1_f22,
            R.mipmap.attack2_f22,
            R.mipmap.attack3_f22,
    };

    public F22(float x, float y, float dx, float dy) {
        super(x, y, 124, 192, R.mipmap.f22, 10, 1);
        this.fabRight = new FrameAnimationBitmap(R.mipmap.f22_right, 10, 3);
        this.fabLeft = new FrameAnimationBitmap(R.mipmap.f22_left, 10, 3);
        this.fabSkill = new FrameAnimationBitmap(R.mipmap.skill_f22, 15, 47);
        this.fabLeft.SetReverse(true);
        this.dx = dx;
        this.dy = dy;
        this.state = State.idle;
        this.power = 1;
        this.hp = MAX_HP;
        this.attackCoolTime = ATTACK_COOL_TIME;
        this.invincible = false;
        this.invincibleTime = INVINCIBLE_TIME;
        this.invincibleCount = 0;
        this.random = new Random();
        this.fabSkillIcon = new FrameAnimationBitmap(R.mipmap.bombicon, 0, 1);
        this.fabHpIcon = new FrameAnimationBitmap(R.mipmap.f22, 0, 1);
        this.fabName = new FrameAnimationBitmap(R.mipmap.f22_title, 0, 1);
        this.skillCount = MAX_SKILL_COUNT;
    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();

        if(state == State.skill)
        {
            if(47 / 2 == fabSkill.getFrame()) {
                SecondScene.get().getGameWorld().add(SecondScene.Layer.bullet.ordinal(), Bomb.get(UIBridge.metrics.center.x, UIBridge.metrics.center.y, UIBridge.metrics.size.x, UIBridge.metrics.size.x, false));
                ArrayList<GameObject> enemys = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.enemy.ordinal());
                ArrayList<GameObject> bullets = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.enemy_bullet.ordinal());
                for (GameObject obj : enemys) {
                    if (obj instanceof Helicopter) {
                        Helicopter enemy = (Helicopter) obj;
                        enemy.Damage(10);
                    }
                    else if (obj instanceof SmallPlane) {
                        SmallPlane enemy = (SmallPlane) obj;
                        enemy.Damage(10);
                    }
                    else if (obj instanceof MediumPlane) {
                        MediumPlane enemy = (MediumPlane) obj;
                        enemy.Damage(10);
                    }
                    else {
                        continue;
                    }
                }
                for (GameObject obj : bullets) {
                    if (!(obj instanceof Bullet)) {
                        continue;
                    }
                    Bullet bullet = (Bullet) obj;
                    bullet.remove();
                    SecondScene.get().getGameWorld().add(SecondScene.Layer.bullet.ordinal(), Explosion.get(bullet.getX(), bullet.getY(), 40, 40));
                }
            }
            if(fabSkill.done())
                state = State.idle;
        }

        if(state != State.boost) {
            if(invincible) {
                invincibleTime -= seconds;
                if(invincibleTime < 0.f) {
                    invincible = false;
                }
            }

            float xdir = joystick.getHorzDirection();
            float ydir = joystick.getVerticalDirection();
            x += xdir * dx * seconds;
            y += ydir * dy * seconds;
            if(state != State.skill) {
                if (xdir < 0.f) {
                    state = State.left;
                    fabRight.reset();
                } else if (xdir > 0.f) {
                    state = State.right;
                    fabLeft.reset();
                } else {
                    state = State.idle;
                    fabRight.reset();
                    fabLeft.reset();
                }
            }

            if(x < 0) {
                x = 0;
            } else if(x > UIBridge.metrics.size.x) {
                x = UIBridge.metrics.size.x;
            }
            if(y < 0) {
                y = 0;
            } else if(y > UIBridge.metrics.size.y) {
                y = UIBridge.metrics.size.y;
            }

            if(state != State.skill) {
                attackCoolTime -= seconds;
                if (attackCoolTime < 0.f) {
                    SecondScene.get().getGameWorld().add(SecondScene.Layer.bullet.ordinal(), Bullet.get(x, y, 92, 164, BULLET_IMAGE[power], 0.f, -1000.f, true, power, 4));
                    attackCoolTime = ATTACK_COOL_TIME;
                }
                if (!invincible) {
                    checkBulletCollision();
                    checkEnemyCollision();
                }
            }
        }
    }

    private void checkEnemyCollision() {
        ArrayList<GameObject> Enemys = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.enemy.ordinal());
        for (GameObject obj : Enemys) {
            if (obj instanceof Helicopter) {
                Helicopter enemy = (Helicopter) obj;
                if (CollisionHelper.collides(this, enemy)) {
                    enemy.Damage(5);
                    Damage();
                }
            }
            else if (obj instanceof SmallPlane) {
                SmallPlane enemy = (SmallPlane) obj;
                if (CollisionHelper.collides(this, enemy)) {
                    enemy.Damage(5);
                    Damage();
                }
            }
            else if (obj instanceof MediumPlane) {
                MediumPlane enemy = (MediumPlane) obj;
                if (CollisionHelper.collides(this, enemy)) {
                    enemy.Damage(5);
                    Damage();
                }
            }
            else {
                continue;
            }
        }
    }

    private void checkBulletCollision() {
        ArrayList<GameObject> bullets = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.enemy_bullet.ordinal());
        for (GameObject obj : bullets) {
            if (!(obj instanceof Bullet)) {
                continue;
            }
            Bullet bullet = (Bullet) obj;
            if (CollisionHelper.collides(this, bullet)) {
                bullet.remove();
                Damage();
            }
        }
    }

    private void Damage() {
        if(hp > 0) {
            if (invincible) {
                return;
            }
            invincible = true;
            invincibleTime = INVINCIBLE_TIME;
            invincibleCount = 0;
            hp--;
        }
        else {
            // GameOver
            GameOverScene scene = new GameOverScene();
            scene.push();
            return;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        RectF iconRect = new RectF();

        if(invincible) {
            invincibleCount++;
            if (invincibleCount % 2 == 0) {
                DrawUIIcon(canvas);
                return;
            }
        }
        if(state == State.skill) {
            float halfWidth = width * 2;
            float halfHeight = height * 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;
            fabSkill.draw(canvas, dstRect, null);
        }

        if(state == State.idle)
        {
            super.draw(canvas);
        }
        else if(state == State.left) {
            if(fabLeft.done())
            {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;

                Rect srcRect = new Rect();
                srcRect.top = 0;
                srcRect.bottom = 40;
                srcRect.left = 32 * 0;
                srcRect.right = srcRect.left + 32;

                fabLeft.draw(canvas, srcRect, dstRect, null);
            }
            else {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;
                fabLeft.draw(canvas, dstRect, null);
            }
        }
        else if(state == State.right) {
            if(fabRight.done()) {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;

                Rect srcRect = new Rect();
                srcRect.top = 0;
                srcRect.bottom = 40;
                srcRect.left = 32 * 2;
                srcRect.right = srcRect.left + 32;

                fabRight.draw(canvas, srcRect, dstRect, null);
            }
            else {
                float halfWidth = width / 2;
                float halfHeight = height / 2;
                dstRect.left = x - halfWidth;
                dstRect.top = y - halfHeight;
                dstRect.right = x + halfWidth;
                dstRect.bottom = y + halfHeight;
                fabRight.draw(canvas, dstRect, null);
            }
        }

        DrawUIIcon(canvas);
    }

    private void DrawUIIcon(Canvas canvas) {
        RectF iconRect = new RectF();
        for (int i = 0; i < skillCount; ++i) {
            iconRect.left = UIBridge.x(100) + UIBridge.x(30 * (i + 1));
            iconRect.right = iconRect.left + UIBridge.x(30);
            iconRect.top = UIBridge.metrics.size.y - UIBridge.y(55);
            iconRect.bottom = UIBridge.metrics.size.y - UIBridge.y(25);
            fabSkillIcon.draw(canvas, iconRect, null);
        }
        for (int i = 0; i < hp; ++i) {
            iconRect.left = UIBridge.x(30 * (i + 1)) - UIBridge.x(15);
            iconRect.right = iconRect.left + UIBridge.x(30);
            iconRect.top = UIBridge.y(55);
            iconRect.bottom = UIBridge.y(85);
            fabHpIcon.draw(canvas, iconRect, null);
        }
        iconRect.left = UIBridge.x(60);
        iconRect.right = iconRect.left + UIBridge.x(110);
        iconRect.top = UIBridge.y(15);
        iconRect.bottom = UIBridge.y(50);
        fabName.draw(canvas, iconRect, null);
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }
}
