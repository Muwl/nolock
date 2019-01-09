package org.jraf.android.nolock;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class NoLockService extends Service {
    private static final String TAG = "NoLock/" + NoLockService.class.getSimpleName();
    private static final Object object = new Object();
    private KeyguardLock keyguardLock;

    private void init() {
        boolean z = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("PREF_LOCKING", true);
        Log.d(TAG, "handleStart locking=" + z + " (" + (z ? "nolock DISABLED" : "nolock ENABLED") + ")");
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        synchronized (object) {
            if (this.keyguardLock != null) {
                Log.d(TAG, "handleStart mKeyguardLock != null: reenabling keyguard");
                this.keyguardLock.reenableKeyguard();
                this.keyguardLock = null;
            }
            if (z) {
                stopSelf();
            } else {
                Log.d(TAG, "locking == false: getting newKeyguardLock");
                this.keyguardLock = keyguardManager.newKeyguardLock(getClass().toString());
                this.keyguardLock.disableKeyguard();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        synchronized (object) {
            if (this.keyguardLock != null) {
                Log.d(TAG, "handleStart mKeyguardLock != null: reenabling keyguard");
                this.keyguardLock.reenableKeyguard();
                this.keyguardLock = null;
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory");
        synchronized (object) {
            if (this.keyguardLock != null) {
                Log.d(TAG, "handleStart mKeyguardLock != null: reenabling keyguard");
                this.keyguardLock.reenableKeyguard();
                this.keyguardLock = null;
            }
        }
    }

    @Override
    public void onStart(Intent intent, int i) {
        Log.d(TAG, "onStart");
        init();
    }
    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "onStartCommand");
        init();
        return START_STICKY;
    }
}
