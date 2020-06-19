package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;

import android.animation.ValueAnimator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.Button;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.TextButton;

public class GameOverScene extends GameScene {
    private static final String TAG = GameOverScene.class.getSimpleName();

    public enum Layer {
        bg, enemy, player, ui, COUNT
    }

    @Override
    protected int getLayerCount() {
        return Layer.COUNT.ordinal();
    }

    @Override
    public void enter() {
        super.enter();
        setTransparent(true);
        initObjects();

        int mdpi_100 = UIBridge.y(100);
        //ValueAnimator animator =
        ValueAnimator anim = ValueAnimator.ofFloat(UIBridge.metrics.size.y, mdpi_100);
        anim.setDuration(500);
        anim.setInterpolator(new OvershootInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                scrollTo(value);
            }
        });
        anim.start();
    }

    private void scrollTo(float y) {
        int mdpi_100 = UIBridge.y(100);
        ArrayList<GameObject> objs = gameWorld.objectsAtLayer(Layer.ui.ordinal());
        int count = objs.size();
        for (int i = 0; i < count; i++) {
            Button btn = (Button)objs.get(i);
            float diff = y - btn.getY();
            btn.move(0, diff);

            y += mdpi_100;
        }
    }

    private void initObjects() {
        int screenWidth = UIBridge.metrics.size.x;
        int screenHeight = UIBridge.metrics.size.y;

        int cx = UIBridge.metrics.center.x;
        int y = UIBridge.metrics.center.y;
        int mdpi_100 = UIBridge.x(100);

        int buttonWidth = screenWidth * 2 / 5;
        int buttonHeight = screenHeight / 5;

        gameWorld.add(Layer.bg.ordinal(), new BitmapObject(cx, y, screenWidth, screenHeight, R.mipmap.black_transparent));
//        y += UiBridge.y(100);
        TextButton button;
        int textSize = mdpi_100 / 3;
        button = new TextButton(cx, y, "Game Over", textSize);
        button.setSize(buttonWidth, buttonHeight);
        gameWorld.add(Layer.ui.ordinal(), button);

        y += UIBridge.y(100);
        button = new TextButton(cx, y, "Restart", textSize);
        button.setSize(buttonWidth, buttonHeight);
        button.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SecondScene.get().restart();
                pop();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), button);

        y += UIBridge.y(100);
        button = new TextButton(cx, y, "Another", textSize);
        button.setSize(buttonWidth, buttonHeight);
        gameWorld.add(Layer.ui.ordinal(), button);
    }
}
