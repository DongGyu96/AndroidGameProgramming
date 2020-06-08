//package kr.ac.kpu.game.andgp.donggyu.striker.game.map;
//
//import android.content.res.AssetManager;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//import kr.ac.kpu.game.andgp.donggyu.striker.R;
//import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
//import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
//import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameWorld;
//import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
//import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
//import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.BitmapObject;
//import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;
//
//public class TextMap {
//    private final int blockSize;
//    private final GameWorld gameWorld;
//    private final int createAtX;
//    private int currentX;
//    private int mapIndex;
//    private int columns, rows;
//    ArrayList<String> lines;
//    private double timeElapsed;
//
//    public TextMap(String assetFilename, GameWorld gameWorld) {
//        this.gameWorld = gameWorld;
//        AssetManager assets = UIBridge.getActivity().getAssets();
//        try {
//            InputStream is = assets.open(assetFilename);
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader reader = new BufferedReader(isr);
//            String header = reader.readLine();
//            String[] comps = header.split(" ");
//            columns = Integer.parseInt(comps[0]);
//            rows = Integer.parseInt(comps[1]);
//            lines = new ArrayList<>();
//            while (true) {
//                String line = reader.readLine();
//                if (line == null) {
//                    break;
//                }
//                lines.add(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        blockSize = UIBridge.metrics.size.y / rows;
//        createAtX = UIBridge.metrics.size.x + 2 * blockSize;
//        mapIndex = 0;
//
//        currentX = 0;
//        while (currentX <= createAtX) {
//            createColumn();
//        }
//    }
//
//    private void createColumn() {
//        float y = blockSize / 2;
//        for (int row = 0; row < rows; row++) {
//            char ch = getAt(mapIndex, row);
//            createObject(ch, currentX, y);
//            y += blockSize;
//        }
//        currentX += blockSize;
//        mapIndex++;
//    }
//
//    private void createObject(char ch, float x, float y) {
//        SecondScene.Layer layer = SecondScene.Layer.item;
//        GameObject obj = null;
//        switch (ch) {
//            case '1': case '2': case '3': case '4':
//                layer = SecondScene.Layer.item;
//                //obj = CandyItem.get(x, y, blockSize, blockSize, ch - '1');
//                break;
//            case 'O': case 'P': case 'Q':
//                layer = SecondScene.Layer.platform;
//                //obj = Platform.get(x, y, blockSize, ch - 'O');
//                break;
//            case 'x':
//                layer = SecondScene.Layer.obstacle;
//                obj = new AnimObject(x + 3 * blockSize / 2, y + 3 * blockSize / 2, 3 * blockSize, 3 * blockSize, R.mipmap.fireball_128_24f, 2, 0);
//                break;
//        }
//        if (obj != null) {
//            gameWorld.add(layer.ordinal(), obj);
//        }
//    }
//
//    private char getAt(int index, int row) {
//        try {
//            int line = index / columns * rows + row;
//            return lines.get(line).charAt(index % columns);
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    public void update(float dx) {
//        timeElapsed += GameTimer.getTimeDiffSeconds();
//        currentX += dx;
//        if (currentX < createAtX) {
//            createColumn();
//        }
//    }
//}
