package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;

import android.media.MediaPlayer;

import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.Button;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.TextButton;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.Ranking;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;

public class GameClearScene extends GameScene {
    private static final String TAG = GameClearScene.class.getSimpleName();
    private final int type;
    private MediaPlayer SceneBGM;

    public GameClearScene(int type) {
        super();
        this.type = type;
    }

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
    }

    @Override
    public void enter() {
        super.enter();
        SceneBGM = MediaPlayer.create(SoundEffects.get().getContext(), R.raw.ingamebgm2);
        SceneBGM.setLooping(true);
        SceneBGM.start();

        Ranking.get().AddRanking();
        Ranking.get().SaveRanking();

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
        SceneBGM = MediaPlayer.create(SoundEffects.get().getContext(), R.raw.ingamebgm2);
        SceneBGM.setLooping(true);
        SceneBGM.start();
    }

    private void initObjects() {
        Random rand = new Random();

        if(type == 0) {
            BitmapObject title = new BitmapObject(UIBridge.metrics.center.x, UIBridge.metrics.center.y, UIBridge.metrics.size.x, UIBridge.metrics.size.y, R.mipmap.f117_ending);
            gameWorld.add(Layer.bg.ordinal(), title);
        }
        else {
            BitmapObject title = new BitmapObject(UIBridge.metrics.center.x, UIBridge.metrics.center.y, UIBridge.metrics.size.x, UIBridge.metrics.size.y, R.mipmap.f22_ending);
            gameWorld.add(Layer.bg.ordinal(), title);
        }

        int screenWidth = UIBridge.metrics.size.x;
        int screenHeight = UIBridge.metrics.size.y;

        int cx = UIBridge.metrics.center.x;
        int y = UIBridge.metrics.center.y;
        int mdpi_100 = UIBridge.x(100);

        int buttonWidth =  UIBridge.x(200);
        int buttonHeight = UIBridge.y(50);

        TextButton button;
        int textSize = mdpi_100 / 3;

        button = new TextButton(cx, y, "MainMenu", textSize);
        button.setSize(buttonWidth, buttonHeight);
        button.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                pop();
                pop();
                pop();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), button);
    }
}