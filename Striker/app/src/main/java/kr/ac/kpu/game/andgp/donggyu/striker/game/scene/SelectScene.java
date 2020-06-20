package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;

import android.media.MediaPlayer;

import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.Button;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.SelectPlayer;

public class SelectScene extends GameScene {
    private static final String TAG = SelectScene.class.getSimpleName();
    private SelectPlayer selectPlayer;

    private MediaPlayer SceneBGM;

    public enum Layer {
        bg, enemy, player, ui, COUNT
    }

    @Override
    protected int getLayerCount() {
        return Layer.COUNT.ordinal();
    }

    @Override
    public void update() {
        super.update();
//        Log.d(TAG, "Score: " + timer.getRawIndex());
    }

    @Override
    public void enter() {
        super.enter();

        SceneBGM = MediaPlayer.create(SoundEffects.get().getContext(), R.raw.selectbgm);
        SceneBGM.setLooping(true);
        SceneBGM.start();

        initObjects();
    }

    @Override
    public void exit() {
        super.exit();
        SceneBGM.stop();
    }

    @Override
    public void resume() {
        super.resume();
        SceneBGM = MediaPlayer.create(SoundEffects.get().getContext(), R.raw.titlebgm);
        SceneBGM.setLooping(true);
        SceneBGM.start();
    }

    private void initObjects() {
        Random rand = new Random();
        gameWorld.add(Layer.bg.ordinal(), new CityBackground());

        int cx = UIBridge.metrics.center.x;
        int cy = UIBridge.metrics.center.y;
        int y = cy + UIBridge.y(250);

        selectPlayer = new SelectPlayer(cx, cy - UIBridge.y(100), UIBridge.x(380), UIBridge.y(200));
        gameWorld.add(Layer.ui.ordinal(), selectPlayer);

        Button button = new Button(cx, y, R.mipmap.btn_start_game, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        button.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SceneBGM.stop();
                SoundEffects.get().play(R.raw.select, 1.f, 1.f, 1, 0, 1);
                SecondScene scene = new SecondScene(selectPlayer.getSelect());
                scene.push();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), button);

        Button leftButton = new Button(UIBridge.x(30), cy, R.mipmap.left, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        leftButton.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SoundEffects.get().play(R.raw.select, 0.6f, 0.6f, 1, 0, 1);
                selectPlayer.select(0);
            }
        });
        gameWorld.add(Layer.ui.ordinal(), leftButton);

        Button rightButton = new Button(UIBridge.metrics.size.x - UIBridge.x(30), cy, R.mipmap.right, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        rightButton.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SoundEffects.get().play(R.raw.select, 0.6f, 0.6f, 1, 0, 1);
                selectPlayer.select(1);
            }
        });
        gameWorld.add(Layer.ui.ordinal(), rightButton);
    }
}