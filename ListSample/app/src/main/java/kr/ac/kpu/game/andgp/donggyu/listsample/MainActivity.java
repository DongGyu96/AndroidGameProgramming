package kr.ac.kpu.game.andgp.donggyu.listsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.listsample.data.HighscoreItem;
import kr.ac.kpu.game.andgp.donggyu.listsample.data.Serializer;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<HighscoreItem> scores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scores = Serializer.Load(this);



        listView = findViewById(R.id.listView);

        listView.setAdapter(adapter);

    }

    private ListAdapter adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return scores.size();
        }

        @Override
        public Object getItem(int position) {
            TextView tv = new TextView(MainActivity.this);
            tv.setText("The Item " + position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "Pos:" + position + " convertView:" + convertView);
            View view = convertView;
            if(view == null) {
                Log.d(TAG, "Loading from XML for:" + position);
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.score_item, null);
            }
            TextView tv = view.findViewById(R.id.scoreItemTextView);
            ImageView iv = view.findViewById(R.id.scoreItemImageView);

            // 인덱스는 포지션으로부터 얻는다
            HighscoreItem s = scores.get(position);

            tv.setText((s.date.getMonth() + 1) + "월 " + s.date.getDate() + "일 " + s.date.getHours() + ":" + s.date.getMinutes() + ":" + s.date.getSeconds() + "\n" +
                    "User : " + s.name + "   Score : " + s.score
            );
            tv.setBackgroundColor(Color.LTGRAY);

            if(s.score < 50000)
            {
                iv.setImageResource(R.mipmap.tier_01);
                iv.setBackgroundColor(Color.GRAY);
            }
            else if(s.score < 100000)
            {
                iv.setImageResource(R.mipmap.tier_02);
                iv.setBackgroundColor(Color.GRAY);
            }
            else if(s.score < 200000)
            {
                iv.setImageResource(R.mipmap.tier_03);
                iv.setBackgroundColor(Color.GRAY);
            }
            else if(s.score < 300000)
            {
                iv.setImageResource(R.mipmap.tier_04);
                iv.setBackgroundColor(Color.GRAY);
            }
            else if(s.score < 400000)
            {
                iv.setImageResource(R.mipmap.tier_05);
                iv.setBackgroundColor(Color.GRAY);
            }
            else if(s.score < 500000)
            {
                iv.setImageResource(R.mipmap.tier_06);
                iv.setBackgroundColor(Color.GRAY);
            }
            else if(s.score < 600000)
            {
                iv.setImageResource(R.mipmap.tier_07);
                iv.setBackgroundColor(Color.GRAY);
            }
            else if(s.score < 700000)
            {
                iv.setImageResource(R.mipmap.tier_08);
                iv.setBackgroundColor(Color.GRAY);
            }
            else
            {
                iv.setImageResource(R.mipmap.tier_09);
                iv.setBackgroundColor(Color.GRAY);
            }

            return view;
        }
    };

    public void onBtnAdd(View view) {
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(R.string.highscore)
                .setMessage(R.string.add_highscore_message)
                .setView(et)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = et.getText().toString();
                        int score = new Random().nextInt(1000000);
                        addHighscore(name, score);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
        Serializer.Save(this, scores);

    }

    private void addHighscore(String name, int score) {
        scores.add(new HighscoreItem(name, new Date(), score));
        // 갱신됬음을 어댑터에게 알릴 필요가 있음
        // adapter.notify();
    }

    public void onBtnDelete(View view) {
    }
}
