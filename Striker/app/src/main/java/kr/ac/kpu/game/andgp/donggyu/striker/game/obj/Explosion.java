package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Touchable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Explosion extends AnimObject implements Recyclable {
    public Explosion(float x, float y, int width, int height) {
        super(x, y, width, height, R.mipmap.explosion,
                12, 12);
        this.hp = 1;
    }
    public static Explosion get(float x, float y, int width, int height) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        Explosion explosion = (Explosion) rpool.get(Explosion.class);
        if (explosion == null) {
            explosion = new Explosion(x, y, width, height);
        } else {
            explosion.x = x;
            explosion.y = y;
            explosion.width = width;
            explosion.height = height;
            explosion.fab.setBitmapResource(R.mipmap.explosion);
            explosion.fab.reset();
        }
        return explosion;
    }

    @Override
    public void update() {
        if(fab.done()) {
            remove();
        }
    }

    @Override
    public void recycle() {

    }
}
