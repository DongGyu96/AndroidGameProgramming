package kr.ac.kpu.game.andgp.donggyu.striker.game.map;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.Boss_Bomber;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.Boss_UFO;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.Helicopter;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.MediumPlane;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.SmallPlane;
import kr.ac.kpu.game.andgp.donggyu.striker.game.obj.Warning;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class TextMap {
    private final int blockSize;
    private final GameWorld gameWorld;
    private final int createAtX;
    private boolean pause;
    private int currentX;
    private int mapIndex;
    private int columns, rows;
    ArrayList<String> lines;
    private double timeElapsed;

    public TextMap(String assetFilename, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        AssetManager assets = UIBridge.getActivity().getAssets();
        try {
            InputStream is = assets.open(assetFilename);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String header = reader.readLine();
            String[] comps = header.split(" ");
            columns = Integer.parseInt(comps[0]);
            rows = Integer.parseInt(comps[1]);
            lines = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        blockSize = UIBridge.metrics.size.x / rows;
        createAtX = UIBridge.metrics.size.y + 2 * blockSize;
        mapIndex = 0;

        currentX = 0;
        while (currentX <= createAtX) {
            createColumn();
        }
        this.pause = false;
    }

    private void createColumn() {
        float y = blockSize / 2;
        for (int row = 0; row < rows; row++) {
            char ch = getAt(mapIndex, row);
            createObject(ch, currentX, y);
            y += blockSize;
        }
        currentX += blockSize;
        mapIndex++;
    }

    private void createObject(char ch, float x, float y) {
        SecondScene.Layer layer = SecondScene.Layer.item;
        GameObject obj = null;
        switch (ch) {
            case '1':
                layer = SecondScene.Layer.enemy;
                obj = Helicopter.get(y, -100, 0.f, 3 * blockSize);
                break;
            case '2':
                layer = SecondScene.Layer.enemy;
                obj = Helicopter.get(y, -100, 75.f, 3 * blockSize);
                break;
            case '3':
                layer = SecondScene.Layer.enemy;
                obj = Helicopter.get(y, -100, -75.f, 3 * blockSize);
                break;
            case 'S':
                layer = SecondScene.Layer.enemy;
                obj = SmallPlane.get(y, -100, 0.f, 3 * blockSize);
                break;
            case 'R':
                layer = SecondScene.Layer.enemy;
                obj = SmallPlane.get(y, -100, 75.f, 3 * blockSize);
                break;
            case 'L':
                layer = SecondScene.Layer.enemy;
                obj = SmallPlane.get(y, -100, -75.f, 3 * blockSize);
                break;
            case 'M':
                layer = SecondScene.Layer.enemy;
                obj = MediumPlane.get(y, -100, 0.f, 3 * blockSize * 0.75f);
                break;
            case 'B':
                layer = SecondScene.Layer.boss;
                obj = new Warning(UIBridge.metrics.center.x, UIBridge.metrics.center.y, UIBridge.x(300), UIBridge.y(200), 0);
                setPause(true);
                break;
            case 'P':
                layer = SecondScene.Layer.boss;
                obj = new Warning(UIBridge.metrics.center.x, UIBridge.metrics.center.y, UIBridge.x(300), UIBridge.y(200), 1);
                setPause(true);
                break;
        }
        if (obj != null) {
            gameWorld.add(layer.ordinal(), obj);
        }
    }

    private char getAt(int index, int row) {
        try {
            int line = index / columns * rows + row;
            return lines.get(line).charAt(index % columns);
        } catch (Exception e) {
            return 0;
        }
    }

    public void update(float dx) {
        if(pause) {
            return;
        }
        timeElapsed += GameTimer.getTimeDiffSeconds();
        currentX += dx;
        if (currentX < createAtX) {
            createColumn();
        }
    }

    public void setPause(boolean pause) {this.pause = pause;}
    public boolean getPause() {return this.pause;}

    public void reset() {
        mapIndex = 0;

        currentX = 0;
        while (currentX <= createAtX) {
            createColumn();
        }
    }
}
