package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Touchable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class F117 extends AnimObject implements BoxCollidable {
    private static final int MAX_POWER = 4;
    private static final float ATTACK_COOL_TIME = 0.15f;
    private static final float INVINCIBLE_TIME = 3.f;
    private final FrameAnimationBitmap fabSkill;
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

//
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        if(state == State.skill || state == State.boost) {
//            return false;
//        }
//        int action = e.getAction();
//        if(action == MotionEvent.ACTION_DOWN) {
//            int cx = UIBridge.metrics.center.x;
//            if(e.getX() > cx) {
//                state = State.right;
//                fabRight.reset();
//                fabLeft.reset();
//            }
//            else {
//                state = State.left;
//                fabRight.reset();
//                fabLeft.reset();
//            }
//            return false;
//        }
//        if(action == MotionEvent.ACTION_MOVE) {
//            int cx = UIBridge.metrics.center.x;
//
//            if(e.getX() > cx) {
//                state = State.right;
//                fabLeft.reset();
//            }
//            else {
//                state = State.left;
//                fabRight.reset();
//            }
//            return true;
//        }
//        if(action == MotionEvent.ACTION_UP) {
//            state = State.idle;
//            return false;
//        }
//        return false;
//    }

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
            state = State.skill;
            fabSkill.reset();
        }
    }

    private enum State {
        idle, left, right, skill, boost
    }

    private final int[] BULLET_IMAGE = {
            R.mipmap.attack1_f117,
            R.mipmap.attack2_f117,
            R.mipmap.attack3_f117,
            R.mipmap.attack4_f117,
    };

    public F117(float x, float y, float dx, float dy) {
        super(x, y, 124, 192, R.mipmap.f117, 10, 7);
        this.fabRight = new FrameAnimationBitmap(R.mipmap.f117_right, 10, 3);
        this.fabLeft = new FrameAnimationBitmap(R.mipmap.f117_left, 10, 3);
        this.fabSkill = new FrameAnimationBitmap(R.mipmap.skill_f117, 15, 47);
        this.fabLeft.SetReverse(true);
        this.dx = dx;
        this.dy = dy;
        this.state = State.idle;
        this.power = 1;
        this.hp = 5;
        this.attackCoolTime = ATTACK_COOL_TIME;
        this.invincible = false;
        this.invincibleTime = INVINCIBLE_TIME;
        this.invincibleCount = 0;
    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();

        if(state == State.skill)
        {
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
                    SecondScene.get().getGameWorld().add(SecondScene.Layer.bullet.ordinal(), Bullet.get(x, y, 92, 164, BULLET_IMAGE[power - 1], 0.f, -1000.f, true, power, 4));
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
        if(invincible) {
            return;
        }
        invincible = true;
        invincibleTime = INVINCIBLE_TIME;
        invincibleCount = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        if(invincible) {
            invincibleCount++;
            if (invincibleCount % 2 == 0) {
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
            return;
        }

        if(state == State.idle)
        {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;

            Rect srcRect = new Rect();
            srcRect.top = 0;
            srcRect.bottom = 48;
            srcRect.left = 31 * 3;
            srcRect.right = srcRect.left + 31;

            fab.draw(canvas, srcRect, dstRect, null);
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
                srcRect.bottom = 48;
                srcRect.left = 31 * 0;
                srcRect.right = srcRect.left + 31;

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
                srcRect.bottom = 48;
                srcRect.left = 31 * 2;
                srcRect.right = srcRect.left + 31;

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
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }
}
