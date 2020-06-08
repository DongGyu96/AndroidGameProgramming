package kr.ac.kpu.game.andgp.donggyu.striker.framework.main;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclePool {
    private static final String TAG = RecyclePool.class.getSimpleName();
    private HashMap<Class, ArrayList<Object>> map = new HashMap<>();
    public RecyclePool() {
    }
    public void add(Object obj) {
        Class clazz = obj.getClass();
        ArrayList<Object> list = map.get(clazz);
        if (list == null) {
            list = new ArrayList<>();
            map.put(clazz, list);
        }
        list.add(obj);
//        Log.d(TAG, "Adding " + obj);
    }
    public Object get(Class clazz) {
        ArrayList<Object> list = map.get(clazz);
        Object obj = null;
        if (list != null) {
            int count = list.size();
            if (count > 0) {
                obj = list.remove(0);
            }
        }
        if (obj != null) {
//            Log.d(TAG, "Reusing obj " + obj);
//            Log.d(TAG, "Recycling " + obj);
            return obj;
        }

        return null;
    }
}

