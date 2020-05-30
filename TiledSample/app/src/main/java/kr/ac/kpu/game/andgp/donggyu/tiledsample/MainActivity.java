package kr.ac.kpu.game.andgp.donggyu.tiledsample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import kr.ac.kpu.game.andgp.donggyu.tiledsample.tile.TiledMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
//    private TiledMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


//        WindowManager wm = getWindowManager();
        TileView tileView = new TileView(this);
        setContentView(tileView);
//        Log.d(TAG, "map = " + map);
    }
}
