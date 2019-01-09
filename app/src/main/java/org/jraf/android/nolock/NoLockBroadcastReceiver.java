package org.jraf.android.nolock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import static android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP;

public class NoLockBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NoLock/" + NoLockBroadcastReceiver.class.getSimpleName();

    private static void init(Context context) {
        AppWidgetManager instance = AppWidgetManager.getInstance(context);
        for (int a : instance.getAppWidgetIds(new ComponentName(context, AppWidget.class))) {
            AppWidget.a(context, instance, a);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int rstring;
        Log.d(TAG, "onReceive " + intent);
        if ("org.jraf.android.nolock.ACTION_LOCKED".equals(intent.getAction())) {
            Log.d(TAG, "ACTION_LOCKED");
            Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
            edit.putBoolean("PREF_LOCKING", true);
            edit.commit();
            context.startService(new Intent(context, NoLockService.class));
            init(context);
            rstring = R.string.locking;
        } else {
            if ("org.jraf.android.nolock.ACTION_UNLOCKED".equals(intent.getAction())) {
                Log.d(TAG, "ACTION_UNLOCKED");
                Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                edit.putBoolean("PREF_LOCKING", false);
                edit.commit();
                context.startService(new Intent(context, NoLockService.class));
                init(context);
            } else {
                if ("org.jraf.android.nolock.ACTION_TOGGLE".equals(intent.getAction())) {
                    Log.d(TAG, "ACTION_TOGGLE");
                    SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean prefLocking = defaultSharedPreferences.getBoolean("PREF_LOCKING", true);
                    Editor edit = defaultSharedPreferences.edit();
                    String str = "PREF_LOCKING";
                    if (prefLocking) {
                        prefLocking = false;
                    }
                    edit.putBoolean(str, prefLocking);
                    edit.commit();
                    context.startService(new Intent(context, NoLockService.class));
                    init(context);
                    if (!prefLocking) {
                        rstring = R.string.locking;
                    }
                }
                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setRepeating(ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 600000, PendingIntent.getService(context, 0, new Intent(context, NoLockService.class), 0));
            }
            rstring = R.string.unlocking;
        }
        Toast.makeText(context, rstring, Toast.LENGTH_SHORT).show();
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setRepeating(ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 600000, PendingIntent.getService(context, 0, new Intent(context, NoLockService.class), 0));
    }
}
