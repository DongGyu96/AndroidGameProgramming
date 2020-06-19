package kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui;

import android.graphics.Canvas;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
import android.view.MotionEvent;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Touchable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;

public class Button extends BitmapObject implements Touchable {
    private static final String TAG = Button.class.getSimpleName();
    protected final NinePatchDrawable bgNormal;
    protected final NinePatchDrawable bgPress;
    protected boolean capturing, pressed;
    private Runnable onClickRunnable;
    private boolean runOnDown;

    public Button(float x, float y, int resId, int bgNormalResId, int bgPressResId) {
        super(x, y, 0, 0, resId);

        this.bgNormal = (NinePatchDrawable) UIBridge.getResources().getDrawable(bgNormalResId);
        this.bgPress = (NinePatchDrawable) UIBridge.getResources().getDrawable(bgPressResId);
        int left = (int)this.x - this.width / 2, top = (int)this.y - this.height / 2;
        this.bgNormal.setBounds(left, top, left + this.width, top + this.height);
        this.bgPress.setBounds(left, top, left + this.width, top + this.height);
    }

    @Override
    public void move(float dx, float dy) {
        super.move(dx, dy);
        int left = (int)this.x - this.width / 2, top = (int)this.y - this.height / 2;
        this.bgNormal.setBounds(left, top, left + this.width, top + this.height);
        this.bgPress.setBounds(left, top, left + this.width, top + this.height);
    }

    @Override
    public void draw(Canvas canvas) {
        NinePatchDrawable bg = pressed ? bgPress : bgNormal;
        bg.draw(canvas);
        super.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.bgNormal.getBounds().contains((int)e.getX(), (int)e.getY())) {
//                    Log.d(TAG, "Down");
                    captureTouch();
                    capturing = true;
                    pressed = true;

                    if (onClickRunnable != null && runOnDown) {
                        onClickRunnable.run();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                pressed = this.bgNormal.getBounds().contains((int)e.getX(), (int)e.getY());
                break;
            case MotionEvent.ACTION_UP:
//                Log.d(TAG, "Up");
                releaseTouch();
                capturing = false;
                pressed = false;
                if (this.bgNormal.getBounds().contains((int)e.getX(), (int)e.getY())) {
//                    Log.d(TAG, "TouchUp Inside");
                    if (onClickRunnable != null && !runOnDown) {
                        onClickRunnable.run();
                    }
                }
                return true;
        }
        return false;
    }

    public void setOnClickRunnable(Runnable runnable) {
        setOnClickRunnable(false, runnable);
//        this.runOnDown = false;
//        this.onClickRunnable = runnable;
    }

    public void setOnClickRunnable(boolean runOnDown, Runnable runnable) {
        this.runOnDown = runOnDown;
        this.onClickRunnable = runnable;
    }
}

