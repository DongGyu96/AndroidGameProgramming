package kr.ac.kpu.game.andgp.donggyu.striker.framework.util;

import android.content.Context;
import android.content.SharedPreferences;

import kr.ac.kpu.game.andgp.donggyu.striker.R;

public class Ranking {
    private static final String TAG = Ranking.class.getSimpleName();
    private static final String PERFS_NAME = "pref";
    private static Ranking singleton;
    private static final int[] RANKING_SCORE = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
    };
    private static final String[] RANKING = {
            "First",
            "Second",
            "Third",
            "Fourth",
            "Fifth",
            "Sixth",
            "Seventh",
            "Eighth",
            "Ninth",
            "Tenth",
    };
    private static final String[] PLAY_TYPE = {
            "First_Type",
            "Second_Type",
            "Third_Type",
            "Fourth_Type",
            "Fifth_Type",
            "Sixth_Type",
            "Seventh_Type",
            "Eighth_Type",
            "Ninth_Type",
            "Tenth_Type",
    };
    private static final int[] PLAY_TYPE_LIST = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
    };

    private int score;
    private Context context;
    private SharedPreferences pref;
    private int type;

    public static Ranking get() {
        if (singleton == null) {
            singleton = new Ranking();
        }
        return singleton;
    }
    private Ranking() {
        this.score = 0;
        this.type = 0;
    }

    public void Init(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PERFS_NAME, context.MODE_PRIVATE);

        for(int i = 0; i < 10; ++i) {
            RANKING_SCORE[i] = pref.getInt(RANKING[i], 0);
            PLAY_TYPE_LIST[i] = pref.getInt(PLAY_TYPE[i], 0);
        }
    }

    public void SetScore(int score, int type) {
        this.score = score;
        this.type = type;
    }

    public void AddRanking() {
        for(int i = 0; i < 10; ++i) {
            if(score > RANKING_SCORE[i]) {
                for(int j = 9; j > i; --j) {
                    RANKING_SCORE[j] = RANKING_SCORE[j - 1];
                    PLAY_TYPE_LIST[j] = PLAY_TYPE_LIST[j - 1];
                }
                RANKING_SCORE[i] = score;
                PLAY_TYPE_LIST[i] = type;
                break;
            }
        }
    }

    public void SaveRanking() {
        pref = context.getSharedPreferences(PERFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(int i = 0; i < 10; ++i) {
            editor.putInt(RANKING[i], RANKING_SCORE[i]);
            editor.putInt(PLAY_TYPE[i], PLAY_TYPE_LIST[i]);
        }
        editor.commit();
    }

    public int getScore(int index) {return RANKING_SCORE[index];}
    public int getType(int index) {return PLAY_TYPE_LIST[index];}

    public Context getContext() {return context;}
}
