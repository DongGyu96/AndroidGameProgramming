package kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj;

import android.util.Log;

import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.world.MainWorld;

public class EnemyGenerator {
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private static final long INITIAL_GENERATE_INTERVAL = 5_000_000_000L;
    private static final long MINIMUM_GENERATE_INTERVAL = 1_000_000_000L;
    private static final double INTERVAL_REDUCE_RATE_PER_WAVE = 0.995;
    private static final int MAX_SPEED = 2000;
    private static final int MAX_LEVEL = Enemy.RES_IDS.length;
    private long lastGenerated;
    private long generationInterval;
    private final Random rand;
    private int wave;

    public EnemyGenerator() {
        //this.lastGenerated = GameWorld.get().getCurrentTimeNanos();
        generationInterval = INITIAL_GENERATE_INTERVAL;
        rand = new Random();
    }

    public void update() {
        GameWorld gw = GameWorld.get();
        long now = gw.getCurrentTimeNanos();
        if (lastGenerated == 0) {
            lastGenerated = now;
            return;
        }
        long elapsed = now - lastGenerated;
        if (elapsed > generationInterval) {
            generateWave();
            lastGenerated = now;
        }
    }

    private void generateWave() {
        wave++;
        String msg = "Wave " + wave + " Generated: /";
        for (int i = 0; i < 5; i++) {
            int level = generateEnemy(120 + i * 200);
            msg += level + "/";
        }
        generationInterval *= INTERVAL_REDUCE_RATE_PER_WAVE;
        if (generationInterval < MINIMUM_GENERATE_INTERVAL) {

        }
        msg += " Interval=" + (generationInterval / 1_000_000_000.0);
        Log.d(TAG, msg);
    }

    private static int[] DIFFS = {
            -3, -3, -2, -2, -2, -2, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2
    };
    private int generateEnemy(int x) {
        int speed = 500 + 10 * wave;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        int level = wave / 10 + DIFFS[rand.nextInt(DIFFS.length)];
        //int level = (int) (rand.nextGaussian() * 11 + wave - 5) / 10 + 1;
        if (level < 1) {
            level = 1;
        }
        if (level > MAX_LEVEL) {
            level = MAX_LEVEL;
        }

        Enemy e = Enemy.get(x, level, speed);
        MainWorld gw = MainWorld.get();
        gw.add(MainWorld.Layer.Enemy, e);
//        Log.d(TAG, "" + e);

        return level;
    }
}
