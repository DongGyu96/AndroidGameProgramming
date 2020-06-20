package kr.ac.kpu.game.andgp.donggyu.striker.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.input.sensor.GyroSensor;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.view.GameView;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.FirstScene;

public class GameActivity extends AppCompatActivity {

    private static final long BACKKEY_INTERVAL_MSEC = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIBridge.setActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));


        new FirstScene().run();



    }

    private long lastBackPressedOn;
    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastBackPressedOn;
        if (elapsed <= BACKKEY_INTERVAL_MSEC) {
            handleBackPressed();
            return;
        }
        Log.d("BackKey", "elapsed="+elapsed);
        Toast.makeText(this,
                "Press Back key twice quickly to exit",
                Toast.LENGTH_SHORT)
                .show();
        lastBackPressedOn = now;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (GyroSensor.isCreated()) {
//            GyroSensor.get().register();
//        }
    }

    @Override
    protected void onPause() {
//        if (GyroSensor.isCreated()) {
//            GyroSensor.get().unregister();
//        }
        super.onPause();
    }

    public void handleBackPressed() {
        GameScene.getTop().onBackPressed();
    }
}
