package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;

import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.Button;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.SelectPlayer;

public class SelectScene extends GameScene {
    private static final String TAG = SelectScene.class.getSimpleName();
    private SelectPlayer selectPlayer;

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
        initObjects();
    }

    private void initObjects() {
        Random rand = new Random();
        gameWorld.add(Layer.bg.ordinal(), new CityBackground());

        int cx = UIBridge.metrics.center.x;
        int cy = UIBridge.metrics.center.y;
        int y = cy + UIBridge.y(250);

        selectPlayer = new SelectPlayer(cx, cy, UIBridge.x(400), UIBridge.y(220 - 44));
        gameWorld.add(Layer.ui.ordinal(), selectPlayer);

        Button button = new Button(cx, y, R.mipmap.btn_start_game, R.mipmap.blue_round_btn, R.mipmap.red_round_btn);
        button.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SecondScene scene = new SecondScene();
                scene.push();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), button);
    }
}