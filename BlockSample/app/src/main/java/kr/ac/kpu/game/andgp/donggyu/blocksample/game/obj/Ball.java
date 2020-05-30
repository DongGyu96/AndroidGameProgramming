package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.graphics.Canvas;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.bitmap.FrameAnimationBitmap;

public class Ball implements GameObject {
    public static final int FRAME_PER_SECOND = 6;
    public static final int FRAME_COUNT = 24;
    private int halfSize;
    private final FrameAnimationBitmap fab;
    private float x;
    private float y;
    private float dx;
    private float dy;

    public Ball(float x, float y, float dx, float dy)
    {
        GameWorld gw = GameWorld.get();
        fab = new FrameAnimationBitmap(R.mipmap.fireball_128_24f, FRAME_PER_SECOND, FRAME_COUNT);
        halfSize = fab.getHeight() / 2;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void update()
    {
        GameWorld gw = GameWorld.get();
        x += dx;
        if(dx > 0 && x > gw.getRight() - halfSize || dx < 0 && x < gw.getLeft() + halfSize)
        {
            dx *= -1;
        }
        y += dy;
        if(dy > 0 && y > gw.getBottom() - halfSize  || dy < 0 && y < gw.getTop() + halfSize)
        {
            dy *= -1;
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        fab.draw(canvas, x, y);
    }
}
