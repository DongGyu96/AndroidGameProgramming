package kr.ac.kpu.game.andgp.donggyu.editsample;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class PieView extends View {
    private static final String TAG = PieView.class.getSimpleName();
    private float rectX;

    public PieView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()!= MotionEvent.ACTION_DOWN)
        {
            return false;
        }

        ValueAnimator anim = ValueAnimator.ofFloat(100, 500);
        anim.setDuration(2000);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rectX = (Float)animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        Log.d(TAG, "onDraw()");

//        Resources res = getResources();
//        Bitmap b = BitmapFactory.decodeResource(res, R.mipmap.dog1);
//        Matrix m = new Matrix();
//        m.preScale(2.0f, 1.5f);
//        m.preTranslate(100f, 200f);
//        m.preRotate(45f);
//
//        canvas.drawBitmap(b, m, null);
//
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(5.0f);
//        canvas.drawLine(pl, pt, w, h, paint);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            paint.setColor(0x7F7FFF7F);
//            canvas.drawRoundRect(rectX, 30, rectX + w/2, h/2, 30, 30, paint);
//        }
//        paint.setColor(Color.GREEN);
//        paint.setTextSize(100.f);
//        canvas.drawText("Hello", w / 4.f * 3.f, h / 4.f, paint);
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(w / 4.f, h / 4.f * 3.f, h / 4.f, paint);
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(w/2.f + 20, h/2.f + 30, w - 20, h - 30, paint);

        super.onDraw(canvas);
    }
}
