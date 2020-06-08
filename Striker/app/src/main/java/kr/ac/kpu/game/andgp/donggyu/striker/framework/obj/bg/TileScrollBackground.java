package kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.bg;

import android.content.res.Resources;
import android.graphics.Canvas;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.tile.TiledMap;

public class TileScrollBackground extends GameObject {
    private final TiledMap map;
    //    private final int pageSize;
    private int speed;
    private float scrollX;
    private float scrollY;
    private boolean horizontal;

    public enum Orientation { horizontal, vertical };
    public TileScrollBackground(int jsonResId, int imageResId, Orientation orientation, int speed) {
        int screenWidth = UIBridge.metrics.size.x;
        int tileSize = screenWidth / 16;
        if (tileSize % 16 != 0) {
            tileSize++;
        }
        Resources res = UIBridge.getResources();
        this.map = TiledMap.load(res, jsonResId, true);
        this.map.loadImage(res, new int[] { imageResId }, tileSize, tileSize);

        this.horizontal = orientation == Orientation.horizontal;
        this.speed = speed;
    }

    @Override
    public void update() {
        if (speed == 0) return;
        float amount = speed * GameTimer.getTimeDiffSeconds();
        if (horizontal) {
            scrollX += amount;
            map.setX((int) scrollX);
        } else {
            scrollY += amount;
            map.setY((int) scrollY);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        map.draw(canvas, 0, 0);
    }

    public void scrollTo(int x, int y) {
        this.scrollX = x;
        this.scrollY = y;
        map.scrollTo(x, y);
    }
}
