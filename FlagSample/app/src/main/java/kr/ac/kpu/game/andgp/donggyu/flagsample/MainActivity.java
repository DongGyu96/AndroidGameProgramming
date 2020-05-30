package kr.ac.kpu.game.andgp.donggyu.flagsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    //private JSONArray continents;
    private JSONObject json;
    //private ImageView imageView;
    private HashMap<String, Bitmap> imageCache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startJsonDownloadThread();

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

//        imageView = findViewById(R.id.titleImageView);

        //startDownloadThread();

//        Bitmap bitmap  = loadBitmapFromNetwork();
//        imageView.setImageBitmap(bitmap);

        //loadContries();
    }

    private void startJsonDownloadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json  = loadJsonFromNetwork();

                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.json = json;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private JSONObject loadJsonFromNetwork() {
        try {
            String strUrl = "http://scgyong.net/thumbs/";
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader streamReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            String inputStr;
            while((inputStr = streamReader.readLine()) != null)
                sb.append(inputStr);

            JSONObject jobj = new JSONObject(sb.toString());
            return jobj;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startImageDownloadThread(final String strUrl, final ImageView imageView, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap  = loadBitmapFromNetwork(strUrl);
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 다운로드 완료 시점
                        imageCache.put(strUrl, bitmap);
                        int first = listView.getFirstVisiblePosition();
                        int last = listView.getLastVisiblePosition();
                        if(position < first || position > last) {
                            return;
                        }
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    private Bitmap loadBitmapFromNetwork(String strUrl) {
        try {
            // 네트워크는 메인쓰레드에서 실행되면 안된다
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private void loadContries() {
//        AssetManager assets = getAssets();
//        try {
//            InputStream is = assets.open("nations.js");
//            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
//            BufferedReader streamReader = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//
//            String inputStr;
//            while((inputStr = streamReader.readLine()) != null)
//                sb.append(inputStr);
//
//            JSONArray jarr = new JSONArray(sb.toString());
//            this.continents = jarr;
//
//            adapter.notifyDataSetChanged();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if(json == null) return 0;
            try {
                JSONArray albums = json.getJSONArray("albums");
                return albums.length();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // part 1 : 재활용
            View view = convertView;
            if(view == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.country_item, null);
            }
            String artistName = "", albumTitle = "";
            // part 2 : data at position
            try {
                JSONArray albums = json.getJSONArray("albums");
                JSONObject album = albums.getJSONObject(position);

                artistName = album.getString("artistName");
                albumTitle = album.getString("albumTitle");

                String imageUrl = album.getString("image");

                ImageView iv = view.findViewById(R.id.imageView);

                Bitmap bitmap = imageCache.get(imageUrl);
                if(bitmap != null) {
                    iv.setImageBitmap(bitmap);
                }
                else {
                    iv.setImageResource(R.mipmap.note);
                    startImageDownloadThread(imageUrl, iv, position);
                }
            } catch (JSONException e) {
            }

            // part 3 : connection
            TextView attv = view.findViewById(R.id.albumTitleTextView);
            ImageView iv = view.findViewById(R.id.imageView);
            attv.setText(albumTitle);

            TextView antv = view.findViewById(R.id.artistNameTextView);
            antv.setText(artistName);

//            AssetManager assets = getAssets();
//            try {
//                InputStream is = assets.open("flag/" + file);
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                iv.setImageBitmap(bitmap);
//                //Drawable drawable = Drawable.createFromStream(is, null);
//                //iv.setImageDrawable(drawable);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return view;
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    };
}
