package kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;

public class TextObject extends GameObject {
    private static final String TAG = TextObject.class.getSimpleName();
    private final String text;
    private final int textSize;
    private final Paint paint;

    public TextObject(float x, float y, String text, int textSize, int color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.textSize = textSize;
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, x, y, paint);
    }
}
