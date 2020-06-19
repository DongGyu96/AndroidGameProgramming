package kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;

import kr.ac.kpu.game.andgp.donggyu.striker.R;

public class TextButton extends Button {
    protected Bitmap bitmap;
    public TextButton(float x, float y, String text, int textSize) {
        super(x, y, 0, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        setSize(textBounds.width() * 2, textBounds.height() * 2);

        bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);

        int cx = width / 2;
        int cy = height / 2;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        int left = (int)this.x - this.width / 2, top = (int)this.y - this.height / 2;
        this.bgNormal.setBounds(left, top, left + this.width, top + this.height);
        this.bgPress.setBounds(left, top, left + this.width, top + this.height);
    }

    @Override
    public void draw(Canvas canvas) {
        NinePatchDrawable bg = pressed ? bgPress : bgNormal;
        bg.draw(canvas);

        int halfWidth = bitmap.getWidth() / 2;
        int halfHeight = bitmap.getHeight() / 2;
        dstRect.left = x - halfWidth;
        dstRect.top = y - halfHeight;
        dstRect.right = x + halfWidth;
        dstRect.bottom = y + halfHeight;

        canvas.drawBitmap(bitmap, null, dstRect, null);
    }
}
