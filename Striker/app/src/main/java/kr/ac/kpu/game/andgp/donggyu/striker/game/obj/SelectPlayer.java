package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.FrameAnimationBitmap;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.SharedBitmap;

public class SelectPlayer extends AnimObject {
    private final FrameAnimationBitmap fabF22;
    private final FrameAnimationBitmap fabF117Title;
    private final FrameAnimationBitmap fabF117Status;
    private final FrameAnimationBitmap fabF22Title;
    private final FrameAnimationBitmap fabF22Status;
    private int select;

    public SelectPlayer(float x, float y, int width, int height) {
        super(x, y, width, height, R.mipmap.select_f117, 25, 45);
        this.fabF22 = new FrameAnimationBitmap(R.mipmap.select_f22, 25, 45);

        this.fabF117Title = new FrameAnimationBitmap(R.mipmap.f117_title, 0, 1);
        this.fabF117Status = new FrameAnimationBitmap(R.mipmap.f117_status, 0, 1);

        this.fabF22Title = new FrameAnimationBitmap(R.mipmap.f22_title, 0, 1);
        this.fabF22Status = new FrameAnimationBitmap(R.mipmap.f22_status, 0, 1);

        this.select = 0;
        this.hp = 1;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        if(select == 0) {
            RectF tempRect = dstRect;
            super.draw(canvas);

            tempRect.left = UIBridge.metrics.center.x - UIBridge.x(110);
            tempRect.right = UIBridge.metrics.center.x + UIBridge.x(110);
            tempRect.top = UIBridge.metrics.center.y + UIBridge.y(10);
            tempRect.bottom = tempRect.top + UIBridge.y(100);

            fabF117Title.draw(canvas, tempRect, null);

            tempRect.top += UIBridge.y(100);
            tempRect.bottom += UIBridge.y(100);

            fabF117Status.draw(canvas, tempRect, null);

        }
        else if(select == 1) {
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            dstRect.left = x - halfWidth;
            dstRect.top = y - halfHeight;
            dstRect.right = x + halfWidth;
            dstRect.bottom = y + halfHeight;

            RectF tempRect = dstRect;

            fabF22.draw(canvas, dstRect, null);

            tempRect.left = UIBridge.metrics.center.x - UIBridge.x(110);
            tempRect.right = UIBridge.metrics.center.x + UIBridge.x(110);
            tempRect.top = UIBridge.metrics.center.y + UIBridge.y(10);
            tempRect.bottom = tempRect.top + UIBridge.y(100);

            fabF22Title.draw(canvas, tempRect, null);

            tempRect.top += UIBridge.y(100);
            tempRect.bottom += UIBridge.y(100);

            fabF22Status.draw(canvas, tempRect, null);
        }
    }

    public void select(int select) {
        this.select = select;
    }

    public int getSelect() {return select;}
}
