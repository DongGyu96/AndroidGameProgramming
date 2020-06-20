package kr.ac.kpu.game.andgp.donggyu.striker.game.scene;

import android.graphics.Color;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.TextButton;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.ui.TextObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.Ranking;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.CityBackground;

public class HighScoreScene extends GameScene {
    private static final String TAG = HighScoreScene.class.getSimpleName();

    public enum Layer {
        bg, ui, COUNT
    }

    @Override
    protected int getLayerCount() {
        return Layer.COUNT.ordinal();
    }

    @Override
    public void enter() {
        super.enter();
        initObjects();

    }
    private void initObjects() {
        int screenWidth = UIBridge.metrics.size.x;
        int screenHeight = UIBridge.metrics.size.y;

        int cx = UIBridge.metrics.center.x + UIBridge.x(30);
        int y = UIBridge.y(50);
        int mdpi_100 = UIBridge.x(100);

        TextObject text;
        int textSize = mdpi_100 / 3;

        gameWorld.add(SecondScene.Layer.bg.ordinal(), new CityBackground());

        for(int i = 0; i < 10; ++i) {
            gameWorld.add(DialogScene.Layer.bg.ordinal(),
                    new BitmapObject(UIBridge.metrics.center.x, y - UIBridge.y(13), screenWidth - UIBridge.x(80), UIBridge.y(45), R.mipmap.black_transparent));

            int color = Color.GRAY;
            if(i == 0) {color = Color.YELLOW;}
            else if(i == 1) {color = Color.WHITE;}
            else if(i > 1 && i < 5) { color = Color.LTGRAY;}

            text = new TextObject(cx - UIBridge.x(160), y, Integer.toString(i + 1), textSize, color);
            gameWorld.add(Layer.ui.ordinal(), text);

            int type = Ranking.get().getType(i);
            if(type == 0) {
                gameWorld.add(DialogScene.Layer.bg.ordinal(),
                        new BitmapObject(UIBridge.metrics.center.x - UIBridge.x(35), y - UIBridge.y(13), UIBridge.x(100), UIBridge.y(35), R.mipmap.f117_title));
            }
            else {
                gameWorld.add(DialogScene.Layer.bg.ordinal(),
                        new BitmapObject(UIBridge.metrics.center.x - UIBridge.x(35), y - UIBridge.y(13), UIBridge.x(100), UIBridge.y(35), R.mipmap.f22_title));
            }

            String score = Integer.toString(Ranking.get().getScore(i));
            text = new TextObject(cx, y, score, textSize, color);
            gameWorld.add(Layer.ui.ordinal(), text);
            y += UIBridge.y(60);
        }

        TextButton btnBack = new TextButton(UIBridge.metrics.size.x - UIBridge.x(80), UIBridge.metrics.size.y - UIBridge.y(40), "Back", UIBridge.x(100) / 3);
        btnBack.setOnClickRunnable(new Runnable() {
            @Override
            public void run() {
                pop();
                return;
            }
        });
        gameWorld.add(Layer.ui.ordinal(), btnBack);
    }
}
