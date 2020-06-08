package kr.ac.kpu.game.andgp.donggyu.striker.framework.obj;

import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.bitmap.SharedBitmap;

public class ScoreObject extends GameObject {
    private static final String TAG = ScoreObject.class.getSimpleName();
    public static final int SCORE_INCREASE_DURATION = 100;
    private final SharedBitmap sbmp;
    private final int digitWidth;
    private RectF rightmostRect;
    private RectF rect = new RectF();
    private Rect srcRect = new Rect();
    private ObjectAnimator scoreAnimator = ObjectAnimator.ofInt(this, "displayedScore", 0, 1);

    public int getDisplayedScore() {
        return displayedScore;
    }

    public void setDisplayedScore(int displayedScore) {
        this.displayedScore = displayedScore;
    }
    public int getScoreValue() {
        return targetScore;
    }

    private int displayedScore, targetScore;

    public ScoreObject(int resId, RectF rightmostRect) {
        sbmp = SharedBitmap.load(resId, false);
        digitWidth = sbmp.getWidth() / 10;
        int digitHeight = sbmp.getHeight();
        this.rightmostRect = new RectF(rightmostRect);
        if (this.rightmostRect.bottom == 0) {
            float width = this.rightmostRect.width();
            float height = width / digitWidth * digitHeight;
            this.rightmostRect.bottom = this.rightmostRect.top + height;
        }
        srcRect.top = 0;
        srcRect.bottom = digitHeight;
        scoreAnimator.setDuration(SCORE_INCREASE_DURATION);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        rect.set(rightmostRect);
        float width = rect.width();
        int score = this.displayedScore;
        while (score > 0) {
            int digit = score % 10;
            srcRect.left = digitWidth * digit;
            srcRect.right = srcRect.left + digitWidth;
//            Log.d(TAG, "digit=" + digit + " src=" + srcRect + " dst=" + rect);
            canvas.drawBitmap(sbmp.getBitmap(), srcRect, rect, null);

            rect.left -= width;
            rect.right -= width;

            score /= 10;
        }
    }

    public void reset() {
        add(-targetScore);
    }
    public void add(int score) {
        int newScore = this.targetScore + score;
        scoreAnimator.setIntValues(this.targetScore, newScore);
        scoreAnimator.start();
        this.targetScore = newScore;
    }
}