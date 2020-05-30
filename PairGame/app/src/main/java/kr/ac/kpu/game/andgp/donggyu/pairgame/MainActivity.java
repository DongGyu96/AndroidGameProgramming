package kr.ac.kpu.game.andgp.donggyu.pairgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int[] BUTTON_IDS = {
            R.id.b_00, R.id.b_01, R.id.b_02, R.id.b_03, R.id.b_04, R.id.b_05,
            R.id.b_10, R.id.b_11, R.id.b_12, R.id.b_13, R.id.b_14, R.id.b_15,
            R.id.b_20, R.id.b_21, R.id.b_22, R.id.b_23, R.id.b_24, R.id.b_25,
            R.id.b_30, R.id.b_31, R.id.b_32, R.id.b_33, R.id.b_34, R.id.b_35,
    };

    private static final int[] IMAGE_RES_IDS = {
            R.mipmap.card_2c, R.mipmap.card_3d, R.mipmap.card_4h, R.mipmap.card_5s,
            R.mipmap.card_jc, R.mipmap.card_qh, R.mipmap.card_kd, R.mipmap.card_as,
            R.mipmap.dog1, R.mipmap.dog2, R.mipmap.dog3, R.mipmap.dog4,
    };

    private ImageButton lastButton;
    private int filps;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // #findViewByID는 미리 변수로 저장해서 사용하는게 오버헤드가 적다
        scoreTextView = findViewById(R.id.ScoreTextView);

        StartGame();
    }

    private void StartGame() {
        int[] buttonIds = ShuffleButtonIds();

        for(int i = 0; i < BUTTON_IDS.length; i++) {
            ImageButton btn = findViewById(BUTTON_IDS[i]);
            int resId = buttonIds[i / 2];
            btn.setTag(resId);
            btn.setImageResource(R.mipmap.gray_back);
            btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btn.setEnabled(true);
        }

        lastButton = null;
        filps = 0;
        ShowScore();
    }

    private void ShowScore() {
        TextView textView = findViewById(R.id.ScoreTextView);
        // this == Activity
        Resources res = getResources();
        String fmt = res.getString(R.string.filps_fmt);
        String text = String.format(fmt, filps);
        textView.setText(text);
    }

    private int[] ShuffleButtonIds() {
        int[] buttonIds = Arrays.copyOf(BUTTON_IDS, BUTTON_IDS.length);
        Random rand = new Random();
        for(int i = 0; i < BUTTON_IDS.length; i++) {
            int r = rand.nextInt(buttonIds.length);
            int temp = buttonIds[i];
            buttonIds[i] = buttonIds[r];
            buttonIds[r] = temp;
        }
        return buttonIds;
    }

    public void onBtnItem(View view) {
        Log.d(TAG, "Button ID = " + view.getId());
        Log.d(TAG, "Button Index = " + (view.getId() - R.id.b_00));

        ImageButton btn = (ImageButton) view;
        int resId = (int) btn.getTag();
        btn.setImageResource(resId);
        btn.setEnabled(false);

        if(lastButton == null) {
            lastButton = btn;
            return;
        }

        if((int)lastButton.getTag() == (int)btn.getTag()) {
            lastButton = null;
            return;
        }

        lastButton.setImageResource(R.mipmap.gray_back);
        lastButton.setEnabled(true);
        lastButton = btn;

        filps++;
        ShowScore();
    }

    public void onBtnRestart(View view) {
        Log.d(TAG, "onBtnRestart()");

//        StartGame();
        new AlertDialog.Builder(this)
                .setTitle(R.string.restartTitle)
                .setMessage(R.string.restartMessage)
                .setNegativeButton(R.string.restartNo, null)
                .setPositiveButton(R.string.restartYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartGame();
                    }
                })
                .create()
                .show();
    }
}

