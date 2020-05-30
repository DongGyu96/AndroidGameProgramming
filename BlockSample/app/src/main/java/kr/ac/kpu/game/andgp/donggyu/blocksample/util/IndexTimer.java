package kr.ac.kpu.game.andgp.donggyu.blocksample.util;

import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;

public class IndexTimer {
    private static final String TAG = IndexTimer.class.getSimpleName();
    private final int count;
    private final int fps;
    private long time;

    public IndexTimer(int count, int framesPerSecond) {
        this.count = count;
        this.fps = framesPerSecond;
        this.time = GameWorld.get().getCurrentTimeNanos();
    }

    public int getIndex(){
        long elapsed =GameWorld.get().getCurrentTimeNanos() - time;
        int index = (int)((elapsed * fps + 500000000) / 1000000000 );
//        Log.d(TAG, "e * f=" + (elapsed * fps) + "index="+index);
        return index % count;
    }

    public void reset() {
        this.time = GameWorld.get().getCurrentTimeNanos();
    }

    public boolean done() {
        long elapsed = GameWorld.get().getCurrentTimeNanos() - time;
        int index = (int)((elapsed * fps + 500000000) / 1000000000 );
        return index >= count;
    }
}
