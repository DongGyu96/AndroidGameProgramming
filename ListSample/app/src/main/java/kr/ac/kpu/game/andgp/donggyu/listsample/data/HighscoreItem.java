package kr.ac.kpu.game.andgp.donggyu.listsample.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class HighscoreItem {
    public String name;
    public Date date;
    public int score;
    public HighscoreItem(String name, Date date, int score) {
        this.name = name;
        this.date = date;
        this.score = score;
    }
    public HighscoreItem(JSONObject o) throws JSONException {
        this.name = o.getString("name");
        long dateValue = o.getLong("date");
        this.date = new Date(dateValue);
        this.score = o.getInt("score");
    }

    public String toJsonString() {
        return "{\"name\":\""+name+"\", \"date\":" + date.getTime() + ",\"score\":" + score + "}";
    }

    public JSONObject toJsonObject() {
        JSONObject s = new JSONObject();
        try {
            s.put("name", name);
            s.put("date", date.getTime());
            s.put("score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s;
    }
}
