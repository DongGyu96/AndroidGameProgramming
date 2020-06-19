package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;

public class SelectPlayer extends AnimObject {
    private final FrameAnimationBitmap fabF22;
    private int select;

    public SelectPlayer(float x, float y, int width, int height) {
        super(x, y, width, height, R.mipmap.select_f117, 30, 45);
        this.fabF22 = new FrameAnimationBitmap(R.mipmap.select_f22, 30, 45);
        this.select = 0;
        this.hp = 1;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        if(select == 0) {
            super.draw(canvas);
        }
        else if(select == 1) {
            fabF22.draw(canvas, dstRect, null);
        }
    }

    public void select(int select) {
        this.select = select;
    }

    public int getSelect() {return select;}
}
