package kr.ac.kpu.game.andgp.donggyu.editsample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Animation rotateAnim;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PieView pieView = new PieView(this);
        PieView pieView2 = new PieView(this);
        PieView pieView3 = new PieView(this);
//        setContentView(pieView);

        FrameLayout parentView = findViewById(R.id.pieParent);
        parentView.addView(pieView);
        FrameLayout parentView2 = findViewById(R.id.pieParent2);
        parentView2.addView(pieView2);
        FrameLayout parentView3 = findViewById(R.id.pieParent3);
        parentView3.addView(pieView3);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        editText.setOnEditorActionListener(editorActionListner);

        final TextView animTextView = findViewById(R.id.animTextView);
        animTextView.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable dog = (AnimationDrawable) animTextView.getBackground();
                dog.start();
            }
        });

        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);

        handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.v("MainActivity", "Later");
//            }
//        });
        postCodes();

        Log.v("MainActivity", "First");

    }

    private void postCodes() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("MainActivity", "Later");
                postCodes();
            }
        }, 100);
    }

    private TextView.OnEditorActionListener editorActionListner = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String text = editText.getText().toString();
            textView.setText("With Editor Action : " + text);
            return false;
        }
    };

    public void onBtnPush(View view) {
        String text = editText.getText().toString();
        textView.setText("You entered : " + text);
        textView.startAnimation(rotateAnim);
    }
}
