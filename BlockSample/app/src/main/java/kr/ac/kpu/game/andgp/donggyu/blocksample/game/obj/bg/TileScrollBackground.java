package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.bg;

import android.content.res.Resources;
import android.graphics.Canvas;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.ScrollableObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.tile.TiledMap;

public class TileScrollBackground implements GameObject, ScrollableObject {
    private final TiledMap map;
//    private final int pageSize;
    private int speed;
    private float scrollX;
    private float scrollY;
    private boolean horizontal;

    public enum Orientation { horizontal, vertical };
    public TileScrollBackground(int resId, Orientation orientation, int speed) {
        GameWorld gw = GameWorld.get();
        int tileSize = gw.getRight() / 16;
        if (tileSize % 16 != 0) {
            tileSize++;
        }
        Resources res = gw.getResources();
        this.map = TiledMap.load(res, resId, true);
        this.map.loadImage(res, new int[] { R.mipmap.forest_tiles }, tileSize, tileSize);

        this.horizontal = orientation == Orientation.horizontal;
        this.speed = speed;
    }

    @Override
    public void update() {
        if (speed == 0) return;
        GameWorld gw = GameWorld.get();
        float amount = speed * gw.getTimeDiffInSecond();
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

    @Override
    public void scrollTo(int x, int y) {
        this.scrollX = x;
        this.scrollY = y;
        map.scrollTo(x, y);
    }
}
