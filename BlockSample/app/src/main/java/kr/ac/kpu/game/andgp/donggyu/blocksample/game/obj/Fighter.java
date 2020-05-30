package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.view.animation.BounceInterpolator;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.world.MainWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.bitmap.FrameAnimationBitmap;

public class Fighter implements GameObject {
    public static final int FRAME_PER_SECOND = 10;
    public static final int FRAME_COUNT = 5;
    private final int shootOffset;
    private int halfSize;
    private final FrameAnimationBitmap fabIdle;
    private final FrameAnimationBitmap fabShoot;
    private float x;
    private float y;

    private float scale;
    //private long firedOn;

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void fire() {
        if (state != State.idle) {
            return;
        }
        //firedOn = GameWorld.get().getCurrentTimeNanos();
        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "scale", 1.0f, 2.0f);
        oa.setDuration(300);
        oa.setInterpolator(new BounceInterpolator());
        oa.start();
        state = State.shoot;
        fabShoot.reset();

        SoundEffects.get().play(R.raw.hadouken);
    }

    private void addFireBall() {
        int fx = (int)(x + halfSize * 0.8f);
        int fy = (int)(y - halfSize * 0.1f);

        int speed = halfSize / 10;
        MainWorld gw = MainWorld.get();
        FireBall fb = new FireBall(fx, fy, speed);
        gw.add(MainWorld.Layer.Missile, fb);
    }

    private enum State {
        idle, shoot
    }

    private State state = State.idle;

    public Fighter(float x, float y)
    {
        GameWorld gw = GameWorld.get();
        fabIdle = new FrameAnimationBitmap(R.mipmap.ryu, FRAME_PER_SECOND, FRAME_COUNT);
        fabShoot = new FrameAnimationBitmap(R.mipmap.ryu_1, FRAME_PER_SECOND, FRAME_COUNT);
        shootOffset = fabShoot.getHeight() * 32 / 100;
        halfSize = fabIdle.getHeight() / 2;
        this.x = x;
        this.y = y;

        Context context = gw.getContext();
    }

    @Override
    public void update()
    {
        if(state == State.shoot) {
            boolean done = fabShoot.done();
            if(done) {
                state = State.idle;
                fabIdle.reset();
                addFireBall();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(state == State.idle) {
            fabIdle.draw(canvas, x, y);
        }
        else if(state == State.shoot) {
            float now = GameWorld.get().getCurrentTimeNanos();
            //float scale = (float)(1 + (now - firedOn) / 1_000_000_000.0);
            canvas.save();
            canvas.scale(scale, scale, x, y);
            fabShoot.draw(canvas, x + shootOffset, y);
            canvas.restore();
        }
    }
}
