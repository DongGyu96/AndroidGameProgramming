package kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

import kr.ac.kpu.game.andgp.donggyu.striker.R;

public class SoundEffects {
    private static final String TAG = SoundEffects.class.getSimpleName();
    private static SoundEffects singleton;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundIdMap = new HashMap<>();
    private static final int[] SOUND_IDS = {
            R.raw.titlebgm,
            R.raw.ingamebgm,
            R.raw.ingamebgm2,
            R.raw.attack,
            R.raw.attack2,
            R.raw.bomb,
            R.raw.bomb2,
            R.raw.bomb3,
            R.raw.boss_intro,
            R.raw.emp,
            R.raw.long_bomb,
            R.raw.long_bomb2,
            R.raw.select,
            R.raw.shoot,
            R.raw.shoot2,
            R.raw.nuclear,
    };
    private Context context;

    public static SoundEffects get() {
        if (singleton == null) {
            singleton = new SoundEffects();
        }
        return singleton;
    }
    private SoundEffects() {
        AudioAttributes audioAttributes;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            this.soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(3)
                    .build();
        } else {
            this.soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }
    }
    public void loadAll(Context context) {
        this.context = context;
        for (int resId: SOUND_IDS) {
            int soundId = soundPool.load(context, resId, 1);
            soundIdMap.put(resId, soundId);
        }
    }

    public int play(int resId, float min, float max, int priority, int loop, float rate) {
        int soundId = soundIdMap.get(resId);
        int streamId = soundPool.play(soundId, min, max, priority, loop, rate);
        return streamId;
    }

    public void stop(int streamId) {
        soundPool.stop(streamId);
    }

    public Context getContext() {return context;}
}
