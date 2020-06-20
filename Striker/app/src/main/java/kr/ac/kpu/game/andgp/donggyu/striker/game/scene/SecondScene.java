package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;


import android.graphics.RectF;
import android.media.MediaPlayer;
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
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.game.map.TextMap;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.F117;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.F22;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.Helicopter;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.Joystick;
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
    private GameObject player;
    private MediaPlayer SceneBGM;

    public SecondScene(int select) {
        super();
        playerType = select;
    }

    public void BGMStop() {
        SceneBGM.stop();
    }

    public void CHEAT() {
        if(playerType == 0) {
            F117 f117 = (F117) player;
            f117.cheat();

        }
        else {
            F22 f22 = (F22) player;
            f22.cheat();
        }
    }

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
        SceneBGM = MediaPlayer.create(SoundEffects.get().getContext(), R.raw.ingamebgm);
        SceneBGM.setLooping(true);
        SceneBGM.start();

        initObjects();

        map = new TextMap("stage_01.txt", gameWorld);
    }

    @Override
    public void exit() {
//        GyroSensor.get().destroy();
        super.exit();
        SceneBGM.stop();
    }

    @Override
    public void resume() {
        super.resume();
        SceneBGM = MediaPlayer.create(SoundEffects.get().getContext(), R.raw.ingamebgm);
        SceneBGM.setLooping(true);
        SceneBGM.start();
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

        gameWorld.add(Layer.bg.ordinal(), new CityBackground());
        gameWorld.add(Layer.bg.ordinal(), new ImageScrollBackground(R.mipmap.clouds, ImageScrollBackground.Orientation.vertical, 100));

        RectF rbox = new RectF(UIBridge.x(-52), UIBridge.y(20), UIBridge.x(-20), UIBridge.y(62));
        scoreObject = new ScoreObject(R.mipmap.number_64x84, rbox);
        gameWorld.add(SecondScene.Layer.ui.ordinal(), scoreObject);

        Button btnSettings = new Button(UIBridge.x(30), UIBridge.y(30), R.mipmap.btn_settings, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        btnSettings.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SceneBGM.stop();
                DialogScene scene = new DialogScene();
                scene.push();
                return;
            }
        });
        gameWorld.add(SecondScene.Layer.ui.ordinal(), btnSettings);

        TextButton btnSkill = new TextButton(UIBridge.x(60), UIBridge.metrics.size.y - UIBridge.y(50), "Skill", UIBridge.x(100) / 3);
        btnSkill.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                if(playerType == 0) {
                    F117 f117 = (F117) player;
                    f117.activeSkill();

                }
                else {
                    F22 f22 = (F22) player;
                    f22.activeSkill();
                }
                return;
            }
        });
        gameWorld.add(SecondScene.Layer.ui.ordinal(), btnSkill);


        if(playerType == 0) {
            player = new F117(cx, sh - mdpi_100, 400.0f, 400.0f);
            gameWorld.add(Layer.player.ordinal(), player);
            Joystick joystick = new Joystick(-500, -500, Joystick.Direction.normal, 100);
            gameWorld.add(Layer.ui.ordinal(), joystick);
            F117 f117 = (F117)player;
            f117.setJoystick(joystick);
        }
        else {
            player = new F22(cx, sh - mdpi_100, 600.0f, 600.0f);
            gameWorld.add(Layer.player.ordinal(), player);
            Joystick joystick = new Joystick(-500, -500, Joystick.Direction.normal, 100);
            gameWorld.add(Layer.ui.ordinal(), joystick);
            F22 f22 = (F22)player;
            f22.setJoystick(joystick);
        }
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
        if(playerType == 0) {
            F117 f117 = (F117)player;
            f117.restart();
        }
        else {
            F22 f22 = (F22)player;
            f22.restart();
        }
        map.setPause(false);
        map.reset();
    }

    public int getPlayerType() {return playerType;}
}
