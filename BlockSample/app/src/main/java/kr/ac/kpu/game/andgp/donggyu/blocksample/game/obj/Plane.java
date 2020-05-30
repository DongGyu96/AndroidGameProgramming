package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.world.MainWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.util.CollisionHelper;

public class Plane implements GameObject, BoxCollidable {
    public static final int BULLET_FIRE_INTERVAL_MSEC = 100_000_000;
    private static final int SPEED = 100;
    private static Bitmap bitmap;
//    private final Matrix matrix;
    private static int halfSize;
    private float x;
    private float y;
    private final float dx;
    private final float dy;
    private long lastFire;
    private Joystick joystick;

    public Plane(float x, float y, float dx, float dy)
    {
        GameWorld gw = GameWorld.get();
        if(bitmap == null) {
            bitmap = BitmapFactory.decodeResource(gw.getResources(), R.mipmap.plane_240);
            halfSize = bitmap.getWidth() / 2;
        }
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
//        this.matrix = new Matrix();
//        matrix.preTranslate(x - halfSize, y - halfSize);
    }

    @Override
    public void update()
    {
//        x += dx;
//        y += dy;
//        matrix.preRotate(5.0f);
        MainWorld gw = MainWorld.get();
        long now = gw.getCurrentTimeNanos();
        long elapsed = now - lastFire;
        if(elapsed > BULLET_FIRE_INTERVAL_MSEC) {
            fire();
            lastFire = now;
        }

        int xdir = joystick.getHorzDirection();
        x += xdir * SPEED * gw.getTimeDiffInSecond();
        if(x < 0) {
            x = 0;
        } else if(x > gw.getRight()) {
            x = gw.getRight();
        }

        ArrayList<GameObject> enemies = gw.objectsAtLayer(MainWorld.Layer.Enemy);
        for(GameObject e : enemies) {
            if(!(e instanceof Enemy)) {
                //Log.e()
                continue;
            }
            Enemy enemy = (Enemy) e;
            if(CollisionHelper.collides(enemy, this)) {
//                enemy.decreaseLift(this.power);
//                toBeDeleted = true;
                gw.endGame();
                break;
            }
        }
    }

    private void fire() {
        Bullet bullet = Bullet.get(x, y - halfSize);
        MainWorld.get().add(MainWorld.Layer.Missile, bullet);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, x - halfSize, y - halfSize, null);
    }

    public void head(float x, float y) {
        this.x = x;
    }

    @Override
    public void getBox(RectF rect) {
        int hw = bitmap.getWidth() / 2;
        int hh = bitmap.getHeight() / 2;
        rect.left = x - hw;
        rect.right = x + hw;
        rect.top = y - hh;
        rect.bottom = y + hh;
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }
}
