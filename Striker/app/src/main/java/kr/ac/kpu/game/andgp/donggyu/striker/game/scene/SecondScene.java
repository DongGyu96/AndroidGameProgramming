package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;


import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ScoreObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.bg.ImageScrollBackground;
//import kr.ac.kpu.game.andgp.donggyu.striker.game.map.TextMap;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.F117;

public class SecondScene extends GameScene {
    private static final String TAG = SecondScene.class.getSimpleName();
    //private TextMap map;
    private int mdpi_100;

    private RectF rect = new RectF();
    private ScoreObject scoreObject;

    public enum Layer {
        bg, item, enemy, player, ui, COUNT
    }

//    private GameTimer timer;

    @Override
    protected int getLayerCount() {
        return Layer.COUNT.ordinal();
    }

    @Override
    public void update() {
        super.update();
//        Log.d(TAG, "Score: " + timer.getRawIndex());
//        if (timer.done()) {
//            pop();
//        }
        float dx = -2 * mdpi_100 * GameTimer.getTimeDiffSeconds();
//        map.update(dx);
//        for (int layer = Layer.platform.ordinal(); layer <= Layer.obstacle.ordinal(); layer++) {
//            ArrayList<GameObject> objects = gameWorld.objectsAtLayer(layer);
//            for (GameObject obj : objects) {
//                obj.move(dx, 0);
//            }
//        }
    }

    @Override
    public void enter() {
        super.enter();
//        GyroSensor.get();
        initObjects();
//        map = new TextMap("stage_01.txt", gameWorld);
    }

    @Override
    public void exit() {
//        GyroSensor.get().destroy();
        super.exit();
    }

    private void initObjects() {
//        timer = new GameTimer(60, 1);
        Random rand = new Random();
        mdpi_100 = UIBridge.x(100);
        Log.d(TAG, "mdpi_100: " + mdpi_100);
        int sw = UIBridge.metrics.size.x;
        int sh = UIBridge.metrics.size.y;
        int cx = UIBridge.metrics.center.x;
        int cy = UIBridge.metrics.center.y;
//        cookie = new Cookie(mdpi_100, mdpi_100);
//        gameWorld.add(Layer.player.ordinal(), cookie);
//        gameWorld.add(Layer.bg.ordinal(), new ImageScrollBackground(R.mipmap.cookie_run_bg_1, ImageScrollBackground.Orientation.horizontal, -100));
//        gameWorld.add(Layer.bg.ordinal(), new ImageScrollBackground(R.mipmap.cookie_run_bg_1_2, ImageScrollBackground.Orientation.horizontal, -200));
//        gameWorld.add(Layer.bg.ordinal(), new ImageScrollBackground(R.mipmap.cookie_run_bg_1_3, ImageScrollBackground.Orientation.horizontal, -300));
        gameWorld.add(Layer.bg.ordinal(), new CityBackground());

        gameWorld.add(Layer.player.ordinal(), new F117(cx, sh - mdpi_100, 0, 0));

        RectF rbox = new RectF(UIBridge.x(-52), UIBridge.y(20), UIBridge.x(-20), UIBridge.y(62));
        scoreObject = new ScoreObject(R.mipmap.number_64x84, rbox);
        gameWorld.add(SecondScene.Layer.ui.ordinal(), scoreObject);

    }

    public void addScore(int amount) {
        scoreObject.add(amount);
    }

    public static SecondScene get() {
        return (SecondScene) GameScene.getTop();
    }
}
