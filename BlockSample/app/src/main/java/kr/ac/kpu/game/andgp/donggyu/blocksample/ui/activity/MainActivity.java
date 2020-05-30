package kr.ac.kpu.game.andgp.donggyu.blocksample.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import kr.ac.kpu.game.andgp.donggyu.blocksample.game.world.MainWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.blocksample.ui.view.GameView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GameView gameView;
//    private TiledMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        MainWorld.create();
//        WindowManager wm = getWindowManager();
        gameView = new GameView(this);
        setContentView(gameView);
//        Log.d(TAG, "map = " + map);

        SoundEffects se = SoundEffects.get();
        se.init(this);
        se.loadAll();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        gameView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        gameView.resume();
        super.onResume();
    }
}
