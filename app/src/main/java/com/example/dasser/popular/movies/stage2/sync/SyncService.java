package com.example.dasser.popular.movies.stage2.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("Z_SunshineSyncService", "onCreate - SyncService");
        synchronized (sSyncAdapterLock) {
            if (syncAdapter == null) {
                Log.d("Z_SunshineSyncService", "SyncService");
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}