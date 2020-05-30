package kr.ac.kpu.game.andgp.donggyu.listsample.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Serializer {

    public static final String PREFS_NAME = "Highscore";
    private static final String PREFS_KEY = "scores";

    public static void Save(Context context, ArrayList<HighscoreItem> items) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String jsonString = convertJson(items);

        editor.putString(PREFS_KEY, jsonString);
        editor.commit();
    }

    private static String convertJson(ArrayList<HighscoreItem> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String comma = "";
        for(HighscoreItem item : items) {
            sb.append(comma);
            sb.append(item.toJsonString());
            comma = ",";
        }
        sb.append("]");

        return sb.toString();
    }

    public static ArrayList<HighscoreItem> Load(Context context) {
        ArrayList<HighscoreItem> items = new ArrayList<>();

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(PREFS_KEY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            int count = jsonArray.length();
            for(int i = 0; i < count; ++i) {
                JSONObject s = jsonArray.getJSONObject(i);
                HighscoreItem item = new HighscoreItem(s);
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

}
