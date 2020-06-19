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
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.Button;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.TextButton;
import kr.ac.kpu.game.andgp.donggyu.striker.game.map.TextMap;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.F117;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.Helicopter;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.MediumPlane;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.SmallPlane;

public class SecondScene extends GameScene {
    private static final String TAG = SecondScene.class.getSimpleName();
    private TextMap map;
    private int mdpi_100;

    private RectF rect = new RectF();
    private ScoreObject scoreObject;

    private static SecondScene instance;

    private int playerType;

    public enum Layer {
        bg, item, boss, enemy, enemy_bullet, bullet, player, ui, COUNT
    }

//    private GameTimer timer;

    @Override
    protected int getLayerCount() {
        return Layer.COUNT.ordinal();
    }

    @Override
    public void update() {
        super.update();
        float dx = -2 * mdpi_100 * GameTimer.getTimeDiffSeconds();
        map.update(dx);
    }

    public void pause(boolean pause) {
        map.setPause(pause);
    }

    @Override
    public void enter() {
        super.enter();
        instance = this;
//        GyroSensor.get();
        initObjects();
        map = new TextMap("stage_01.txt", gameWorld);
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

//        gameWorld.add(Layer.bg.ordinal(), new ImageScrollBackground(R.mipmap.cookie_run_bg_1, ImageScrollBackground.Orientation.horizontal, -100));
//        gameWorld.add(Layer.bg.ordinal(), new ImageScrollBackground(R.mipmap.cookie_run_bg_1_2, ImageScrollBackground.Orientation.horizontal, -200));
//        gameWorld.add(Layer.bg.ordinal(), new ImageScrollBackground(R.mipmap.cookie_run_bg_1_3, ImageScrollBackground.Orientation.horizontal, -300));
        gameWorld.add(Layer.bg.ordinal(), new CityBackground());

        if(playerType == 0) {
            gameWorld.add(Layer.player.ordinal(), new F117(cx, sh - mdpi_100, 400.0f, 0));
        }

        RectF rbox = new RectF(UIBridge.x(-52), UIBridge.y(20), UIBridge.x(-20), UIBridge.y(62));
        scoreObject = new ScoreObject(R.mipmap.number_64x84, rbox);
        gameWorld.add(SecondScene.Layer.ui.ordinal(), scoreObject);

        Button btnSettings = new Button(UIBridge.metrics.size.x - UIBridge.x(30), UIBridge.y(30), R.mipmap.btn_settings, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        btnSettings.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                DialogScene scene = new DialogScene();
                scene.push();
                return;
            }
        });
        gameWorld.add(SecondScene.Layer.ui.ordinal(), btnSettings);
    }

    public void addScore(int amount) {
        scoreObject.add(amount);
    }

    public static SecondScene get() {
        return instance;
    }

    public void restart() {
        scoreObject.reset();
        for (int layer = Layer.item.ordinal(); layer <= Layer.bullet.ordinal(); layer++) {
            gameWorld.removeAllObjectsAt(layer);
        }
        map.reset();
    }
}
