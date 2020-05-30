package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;

public class ScoreObject implements GameObject {
    private final Bitmap bitmap;
    private final int width;
    private final int height;
    private final float right;
    private final float top;
    private final ObjectAnimator scoreAnimator;
    private int scoreDisplay;
    private int scoreValue;
    private Rect srcRect = new Rect();
    private RectF dstRect = new RectF();

    public ScoreObject(float right, float top, int resId) {
        GameWorld gw = GameWorld.get();
        this.bitmap = BitmapFactory.decodeResource(gw.getResources(), resId);
        width = bitmap.getWidth() / 10;
        height = bitmap.getHeight();

        this.right = right;
        this.top = top;
        scoreAnimator = ObjectAnimator.ofInt(this, "scoreDisplay", 0);

        srcRect.top = 0;
        srcRect.bottom = height;
    }

    public void setScoreDisplay(int scoreDisplay) {
        this.scoreDisplay = scoreDisplay;
    }
    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        dstRect.right = right;
        dstRect.top = top;
        dstRect.left = right - width;
        dstRect.bottom = top + height;
        int value = scoreDisplay;
        if(value == 0) {
            srcRect.left = 0;
            srcRect.right = width;
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            return;
        }
        while(value > 0) {
            int digit = value % 10;
            srcRect.left = digit * width;
            srcRect.right = srcRect.left + width;
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            value /= 10;
            dstRect.left -= width;
            dstRect.right -= width;
        }
    }

    public void addScore(int score) {
        scoreValue += score;
        scoreAnimator.setIntValues(scoreDisplay, scoreValue);
        scoreAnimator.setDuration(500);
        scoreAnimator.start();
    }

    public void reset() {
        addScore(-scoreValue);
    }

    public void setScore(int score) {
        addScore(-scoreValue);
        addScore(score);
    }

    public int getScore() {
        return scoreValue;
    }
}
