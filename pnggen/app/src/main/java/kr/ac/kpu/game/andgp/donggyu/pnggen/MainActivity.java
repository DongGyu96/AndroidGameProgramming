package kr.ac.kpu.game.andgp.donggyu.pnggen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int BITMAP_HEIGHT = 128;
    private static final int BITMAP_WIDTH = 128;
    private static final int FRAME_COUNT = 8;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int MARGIN = 10;
    private static final float CORNER = 6;
    private static final float STROKE_WIDTH = 3;
    public static final int ANGLE_MULTIPLIER = 2;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private RectF rect;
//    private Drawable roundrectDrawable;
//    private View mainView;
    private ImageView mainImageView;
    private final Rect textBounds = new Rect(); //don't new this up in a draw method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mainView = findViewById(R.id.mainLayout);
        mainImageView = findViewById(R.id.mainImageView);
//        roundrectDrawable = getResources().getDrawable(R.drawable.roundrect);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(64);
//        paint.setTextAlign(Paint.Align.CENTER);
        rect = new RectF();
    }

    public void onBtnMake(View view) {
        drawImageSet(String.valueOf(3), Color.GRAY, Color.BLUE, Color.BLACK);
    }

    public void onBtnSave(View view) {
        int now = (int) (System.currentTimeMillis() / 1000 % 86400) + 100000;
        String name = "F_" + now + ".png";
        save(name);
    }

    private void drawImageSet(String text, int bgColor, int fgColor, int textColor) {
        bitmap = Bitmap.createBitmap(FRAME_COUNT * BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);

        for (int i = 0; i < FRAME_COUNT; i++) {
            draw(i, bgColor, fgColor, textColor, text);
        }

        mainImageView.setImageBitmap(bitmap);
    }

    private static int[] angles = {
            0, 1, 2, 1, 0, -1, -2, -1,
    };

    private void draw(int index, int bgColor, int fgColor, int textColor, String text) {
        rect.left = index * BITMAP_WIDTH + MARGIN;
        rect.right = (index + 1) * BITMAP_WIDTH - MARGIN;
        rect.top = MARGIN;
        rect.bottom = BITMAP_HEIGHT - MARGIN;
        float cx = index * BITMAP_WIDTH + BITMAP_WIDTH / 2;
        float cy = BITMAP_HEIGHT / 2;
        canvas.save();
        canvas.rotate(ANGLE_MULTIPLIER * angles[index], cx, cy);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(bgColor);
        canvas.drawRoundRect(rect, CORNER, CORNER, paint);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(fgColor);
        canvas.drawRoundRect(rect, CORNER, CORNER, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);

        canvas.restore();
    }

    private void save(String filename) {
        try {
            File root = Environment.getExternalStorageDirectory();
            Log.d(TAG, "Root = " + root + " canWrite=" + root.canWrite());
            if (root.canWrite()) {
                File f = new File(root, filename);
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                Log.d(TAG, "Saved to: " + f);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBtnRequestPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission is granted");
            return;
        }
        // Permission is not granted
        Log.d(TAG, "Permission is not granted");

        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Log.d(TAG, "shouldShowRequestPermissionRationale");
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            Log.d(TAG, "requestPermissions");
        }
    }

// 	var colores_g = [
// 	"0x3366cc", "0xdc3912", "0xff9900", "0x109618", "0x990099",
// 	"0x0099c6", "0xdd4477", "0x66aa00", "0xb82e2e", "0x316395",
// 	"0x994499", "0x22aa99", "0xaaaa11", "0x6633cc", "0xe67300",
// 	"0x8b0707", "0x651067", "0x329262", "0x5574a6", "0x3b3eac"];
    private static int[][] data = {
        { 0xccffffff, 0xFF3366cc, 0xFF3366cc },
        { 0xccffffff, 0xFFdc3912, 0xFFdc3912 },
        { 0xccffffff, 0xFFff9900, 0xFFff9900 },
        { 0xccffffff, 0xFF109618, 0xFF109618 },
        { 0xccffffff, 0xFF990099, 0xFF990099 },
        { 0xccffffff, 0xFF0099c6, 0xFF0099c6 },
        { 0xccffffff, 0xFFdd4477, 0xFFdd4477 },
        { 0xccffffff, 0xFF66aa00, 0xFF66aa00 },
        { 0xccffffff, 0xFFb82e2e, 0xFFb82e2e },
        { 0xccffffff, 0xFF316395, 0xFF316395 },
        { 0xccffffff, 0xFF994499, 0xFF994499 },
        { 0xccffffff, 0xFF22aa99, 0xFF22aa99 },
        { 0xccffffff, 0xFFaaaa11, 0xFFaaaa11 },
        { 0xccffffff, 0xFF6633cc, 0xFF6633cc },
        { 0xccffffff, 0xFFe67300, 0xFFe67300 },
        { 0xccffffff, 0xFF8b0707, 0xFF8b0707 },
        { 0xccffffff, 0xFF651067, 0xFF651067 },
        { 0xccffffff, 0xFF329262, 0xFF329262 },
        { 0xccffffff, 0xFF5574a6, 0xFF5574a6 },
        { 0xccffffff, 0xFF3b3eac, 0xFF3b3eac },
    };
    public void onBtnMakeAll(View view) {
        for (int i = 0; i < data.length; i++) {
            drawImageSet(String.valueOf(i + 1), data[i][0], data[i][1], data[i][2]);
            save(String.format("enemy_%02d.png", i + 1));
        }
    }
}
