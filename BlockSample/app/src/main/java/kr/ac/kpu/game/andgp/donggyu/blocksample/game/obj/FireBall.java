package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.content.res.Resources;
import android.graphics.Canvas;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.bitmap.FrameAnimationBitmap;

public class FireBall implements GameObject {
    public static final int FRAME_PER_SECOND = 10;
    public static final int FRAME_COUNT_FIRE = 2;
    public static final int FRAME_COUNT_FLY = 6;
    private final float speed;
    private final FrameAnimationBitmap fabFire;
    private final FrameAnimationBitmap fabFly;
    private float x;
    private float y;

    public void fire() {
        if(state != State.fire) {
            return;
        }
        state = State.fly;
        fabFly.reset();
    }

    private enum State {
        fire, fly
    }

    private State state = State.fire;

    public FireBall(float x, float y, float speed)
    {
        Resources res = GameWorld.get().getResources();
        fabFire = new FrameAnimationBitmap(R.mipmap.hadoken1, FRAME_PER_SECOND, FRAME_COUNT_FIRE);
        fabFly = new FrameAnimationBitmap(R.mipmap.hadoken2, FRAME_PER_SECOND, FRAME_COUNT_FLY);
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    @Override
    public void update()
    {
        x += speed;
        GameWorld gw = GameWorld.get();
        if(x > gw.getRight()) {
            gw.remove(this);
            return;
        }
        if(state == State.fire) {
            boolean done = fabFire.done();
            if(done) {
                state = State.fly;
                fabFly.reset();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(state == State.fire) {
            fabFire.draw(canvas, x, y);
        }
        else if(state == State.fly) {
            fabFly.draw(canvas, x, y);
        }
    }
}
