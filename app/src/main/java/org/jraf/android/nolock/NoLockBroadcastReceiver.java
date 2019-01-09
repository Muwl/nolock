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

public class NoLockBroadcastReceiver extends BroadcastReceiver {
    private static final String a = ("NoLock/" + NoLockBroadcastReceiver.class.getSimpleName());

    private static void a(Context context) {
        AppWidgetManager instance = AppWidgetManager.getInstance(context);
        for (int a : instance.getAppWidgetIds(new ComponentName(context, AppWidget.class))) {
            AppWidget.a(context, instance, a);
        }
    }

    public void onReceive(Context context, Intent intent) {
        int i;
        Context context2;
        boolean z = true;
        Log.d(a, "onReceive " + intent);
        Editor edit;
        if ("org.jraf.android.nolock.ACTION_LOCKED".equals(intent.getAction())) {
            Log.d(a, "ACTION_LOCKED");
            edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
            edit.putBoolean("PREF_LOCKING", true);
            edit.commit();
            context.startService(new Intent(context, NoLockService.class));
            a(context);
            i = R.string.locking;
            context2 = context;
        } else {
            Context context3;
            if ("org.jraf.android.nolock.ACTION_UNLOCKED".equals(intent.getAction())) {
                Log.d(a, "ACTION_UNLOCKED");
                Editor edit2 = PreferenceManager.getDefaultSharedPreferences(context).edit();
                edit2.putBoolean("PREF_LOCKING", false);
                edit2.commit();
                context.startService(new Intent(context, NoLockService.class));
                a(context);
                context3 = context;
            } else {
                if ("org.jraf.android.nolock.ACTION_TOGGLE".equals(intent.getAction())) {
                    Log.d(a, "ACTION_TOGGLE");
                    SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean z2 = defaultSharedPreferences.getBoolean("PREF_LOCKING", true);
                    edit = defaultSharedPreferences.edit();
                    String str = "PREF_LOCKING";
                    if (z2) {
                        z = false;
                    }
                    edit.putBoolean(str, z);
                    edit.commit();
                    context.startService(new Intent(context, NoLockService.class));
                    a(context);
                    if (z2) {
                        context3 = context;
                    } else {
                        i = R.string.locking;
                        context2 = context;
                    }
                }
                ((AlarmManager) context.getSystemService("alarm")).setRepeating(2, SystemClock.elapsedRealtime() + 1000, 600000, PendingIntent.getService(context, 0, new Intent(context, NoLockService.class), 0));
            }
            context2 = context3;
            i = R.string.unlocking;
        }
        Toast.makeText(context2, i, 0).show();
        ((AlarmManager) context.getSystemService("alarm")).setRepeating(2, SystemClock.elapsedRealtime() + 1000, 600000, PendingIntent.getService(context, 0, new Intent(context, NoLockService.class), 0));
    }
}
