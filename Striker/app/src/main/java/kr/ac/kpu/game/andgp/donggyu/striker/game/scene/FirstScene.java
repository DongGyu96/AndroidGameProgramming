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

public class FirstScene extends GameScene {
    private static final String TAG = FirstScene.class.getSimpleName();
    private MediaPlayer SceneBGM;

    public enum Layer {
        bg, enemy, player, ui, COUNT
    }
    private GameTimer timer;

    @Override
    protected int getLayerCount() {
        return Layer.COUNT.ordinal();
    }

    @Override
    public void update() {
        super.update();
//        Log.d(TAG, "Score: " + timer.getRawIndex());
        if (timer.done()) {
            timer.reset();
        }
    }

    @Override
    public void enter() {
        super.enter();
        //titleBGM = SoundEffects.get().play(R.raw.titlebgm, 0, 0, 1);
        SceneBGM = MediaPlayer.create(SoundEffects.get().getContext(), R.raw.titlebgm);
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
//        int mdpi_100 = UIBridge.x(100);
//        for (int i = 0; i < 10; i++) {
//            int dx = rand.nextInt(2 * mdpi_100) - 1 * mdpi_100;
//            if (dx >= 0) dx++;
//            int dy = rand.nextInt(2 * mdpi_100) - 1 * mdpi_100;
//            if (dy >= 0) dy++;
//            ball = new Ball(mdpi_100, mdpi_100, dx, dy);
//            gameWorld.add(Layer.enemy.ordinal(), ball);
//        }
        gameWorld.add(Layer.bg.ordinal(), new CityBackground());
        BitmapObject title = new BitmapObject(UIBridge.metrics.center.x, UIBridge.metrics.center.y, UIBridge.metrics.size.x, UIBridge.metrics.size.y, R.mipmap.title);
        gameWorld.add(Layer.bg.ordinal(), title);
        timer = new GameTimer(2, 1);

        int cx = UIBridge.metrics.center.x;
        int y = UIBridge.metrics.center.y + 360;
//        y += UiBridge.y(100);
        Button button = new Button(cx, y, R.mipmap.btn_start_game, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        button.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SceneBGM.stop();
                SelectScene scene = new SelectScene();
                scene.push();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), button);

        y += UIBridge.y(100);
        Button highscoreButton = new Button(cx, y, R.mipmap.btn_highscore, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        highscoreButton.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SceneBGM.stop();
                HighScoreScene scene = new HighScoreScene();
                scene.push();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), highscoreButton);
    }
}