package com.example.registerapplication.Adapters;

import android.app.Activity;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainCollector {
    public static CopyOnWriteArrayList<Activity> activities = new CopyOnWriteArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            try {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        activities.clear();
    }
}