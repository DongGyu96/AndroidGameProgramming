package kr.ac.kpu.game.andgp.donggyu.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"Message from onCreate()");
        TextView tv = findViewById(R.id.textView3);
        tv.setText("Launched");


    }

    public void onBtnFirst(View v) {
        Log.d(TAG, "onBtnFirst()")
        TextView tv = findViewById(R.id.textViewMessage);
        tv.setText("First Button Pressed");

        ImageView iv = findViewById(R.id.catImageView);
        iv.setImageResource(R.mipmap.download);
    }

    public void onBtnThird(View view) {
        TextView tv = findViewById(R.id.textView);
        int count = 0;
        try {
            count = Integer.parseInt((String) tv.getText());
        }catch(Exception e) {
        }
        count++;
        tv.setText((String.valueOf(count));

        // Message Box
        new AlertDialog.Builder(this)
                .setTitle("Hello")
                .setMessage("World")
                .setPositiveButton("Hahaha", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        TextView tv = findViewById(R.id.textViewMessage);
                        tv.setText("Hahaha Dialog Button Pressed");
                    }
                })
                .setNegativeButton("Nooooo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView tv = findViewById(R.id.textViewMessage);
                        tv.setText("Nooooo Dialog Button Pressed");
                    }
                })
                .create()
                .show();
    }

    public void onBtnSecond(View view) {
        ImageView iv = findViewById(R.id.catImageView);
        iv.setImageResource(R.mipmap.download2);

        // Random
        Random random = new Random();
        final int value = random.nextInt(100) + 1; // 0~99 + 1

        final TextView tv = findViewById(R.id.textViewMessage);
        tv.setText("Random Number" + value);

        // Timer
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setText("Timer has changed: " + (value + 100)); // 100이 더해져서 등장
            }
        }, 1000); // 1초 후에
    }
}
