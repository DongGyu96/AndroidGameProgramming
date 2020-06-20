package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;

import android.animation.ValueAnimator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.Button;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.TextButton;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.SelectPlayer;

public class DialogScene extends GameScene {
    private static final String TAG = DialogScene.class.getSimpleName();

    public enum Layer {
        bg, item, boss, enemy, enemy_bullet, bullet, player, ui, COUNT
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

        int buttonWidth =  UIBridge.x(200);
        int buttonHeight = UIBridge.y(50);

        gameWorld.add(Layer.bg.ordinal(), new BitmapObject(cx, y, screenWidth, screenHeight, R.mipmap.black_transparent));
//        y += UiBridge.y(100);

        TextButton button;
        int textSize = mdpi_100 / 3;
        button = new TextButton(cx, y, "Back", textSize);
        button.setSize(buttonWidth, buttonHeight);
        button.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                pop();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), button);

        y += UIBridge.y(60);
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

        y += UIBridge.y(60);
        button = new TextButton(cx, y, "CHEAT!", textSize);
        button.setSize(buttonWidth, buttonHeight);
        button.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                SecondScene.get().CHEAT();
            }
        });
        gameWorld.add(Layer.ui.ordinal(), button);
    }
}
