package kr.ac.kpu.game.andgp.donggyu.tiledsample;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import kr.ac.kpu.game.andgp.donggyu.tiledsample.tile.TiledMap;

class TileView extends View {

    private static final String TAG = TileView.class.getSimpleName();
    private final TiledMap map;
    private Point screenSize = new Point();
    private int yDiff;

    private void postFrameCallback() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                update(frameTimeNanos);
                invalidate();
                postFrameCallback();
            }
        });
    }

    public TileView(Context context) {
        super(context);
//        Point screenSize = new Point();
        WindowManager wm = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(screenSize);
        int tileSize = screenSize.x / 16;
        Log.d(TAG, "dstTileWith=" + tileSize);
        if (screenSize.x % 16 > 0) {
            tileSize++;
            Log.d(TAG, "dstTileWith=" + tileSize);
        }
        map = TiledMap.load(getResources(), R.raw.earth, true);
        map.loadImage(getResources(), new int[] { R.mipmap.forest_tiles }, tileSize, tileSize);
        Log.d(TAG, "Done?");

        postFrameCallback();
    }

    public static final int FPS_DURATION = 10;
    private int count;
    private IndexTimer timer = new IndexTimer(FPS_DURATION, 1);
    public void update(long frameTimeNanos) {
//        gameWorld.update(frameTimeNanos);
        IndexTimer.currentTimeNanos = frameTimeNanos;

        count++;
        if (timer.done()) {
            Log.d(TAG, "Frame Count = " + (count / (float)FPS_DURATION));
            count = 0;
            timer.reset();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        map.draw(canvas);
    }

    PointF ptDown = new PointF();
    ObjectAnimator objectAnimator;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int endScroll = map.getFullHeight();
//        Log.d(TAG, "EndScroll = " + endScroll);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            // y dy
            ptDown.x = event.getX();
            ptDown.y = event.getY();
            yDiff = (int) (map.getY() + ptDown.y);
//            Log.d(TAG, "D yDiff=" + yDiff + " eventY=" + event.getY() + " mapY=" + map.getY());
            objectAnimator = ObjectAnimator.ofInt(map, "y", map.getY(), endScroll);
            objectAnimator.setDuration(3000);
            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.start();
        } else if (action == MotionEvent.ACTION_MOVE) {
            // -ey + dy + y
            objectAnimator.cancel();
//            Log.d(TAG, "M yDiff=" + yDiff + " eventY=" + event.getY() + " mapY=" + map.getY());
            map.setY((int) (yDiff - event.getY()));
        }
        return true;
    }
}
