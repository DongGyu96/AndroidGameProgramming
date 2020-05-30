package kr.ac.kpu.game.andgp.donggyu.touchsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "Touch Event: " + event.getAction() + " - " + event.getX(), + ", " + event.getY());
        return super.onTouchEvent(event);
    }
}
