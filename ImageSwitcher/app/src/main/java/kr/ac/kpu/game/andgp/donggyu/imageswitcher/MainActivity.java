package kr.ac.kpu.game.andgp.donggyu.imageswitcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private int pageNumber;
    private static int[] IMAGE_RES_IDS = {
            R.mipmap.dog1,
            R.mipmap.dog2,
            R.mipmap.dog3,
            R.mipmap.dog4,
            R.mipmap.dog5,
            R.mipmap.dog6,
            R.mipmap.dog7,
            R.mipmap.dog8,
    };
    private ImageView mainImageView;
    private TextView headerTextView;
    private String headerFormatString;
    private ImageButton prevButton;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageNumber = 1;
        Resources res = getResources();
        headerFormatString = getString(R.string.header_title_fmt);

        mainImageView = findViewById(R.id.mainImageView);
        headerTextView = findViewById(R.id.headerTextView);

        prevButton = findViewById(R.id.prevbutton);
        nextButton = findViewById(R.id.nextButton);

        ShowPage();
    }

    public void onBtnPrev(View view) {
     //   if(pageNumber > 0) {
     //       pageNumber--;
     //       ShowPage();
     //   }
        pageNumber--;
        ShowPage();
    }

    public void onBtnNext(View view) {
     //   if(pageNumber < IMAGE_RES_IDS.length) {
     //       pageNumber++;
     //          ShowPage();
     //   }
        pageNumber++;
        ShowPage();
    }

    private void ShowPage() {
        prevButton.setEnabled(pageNumber > 1);
        nextButton.setEnabled(pageNumber < IMAGE_RES_IDS.length);

        int resId = IMAGE_RES_IDS[pageNumber - 1];
        mainImageView.setImageResource(resId);
        String text = String.format(headerFormatString, pageNumber, IMAGE_RES_IDS.length);
        headerTextView.setText(text);
    }
}
