package org.jraf.android.nolock;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class NoLockService extends Service {
    private static final String a = ("NoLock/" + NoLockService.class.getSimpleName());
    private static final Object b = new Object();
    private KeyguardLock c;

    private void a() {
        boolean z = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("PREF_LOCKING", true);
        Log.d(a, "handleStart locking=" + z + " (" + (z ? "nolock DISABLED" : "nolock ENABLED") + ")");
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
        synchronized (b) {
            if (this.c != null) {
                Log.d(a, "handleStart mKeyguardLock != null: reenabling keyguard");
                this.c.reenableKeyguard();
                this.c = null;
            }
            if (z) {
                stopSelf();
            } else {
                Log.d(a, "locking == false: getting newKeyguardLock");
                this.c = keyguardManager.newKeyguardLock(getClass().toString());
                this.c.disableKeyguard();
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(a, "onCreate");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(a, "onDestroy");
        synchronized (b) {
            if (this.c != null) {
                Log.d(a, "handleStart mKeyguardLock != null: reenabling keyguard");
                this.c.reenableKeyguard();
                this.c = null;
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        Log.d(a, "onLowMemory");
        synchronized (b) {
            if (this.c != null) {
                Log.d(a, "handleStart mKeyguardLock != null: reenabling keyguard");
                this.c.reenableKeyguard();
                this.c = null;
            }
        }
    }

    public void onStart(Intent intent, int i) {
        Log.d(a, "onStart");
        a();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(a, "onStartCommand");
        a();
        return 1;
    }
}
