package xyz.gatling.ora;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import xyz.gatling.ora.customization.Customization;
import xyz.gatling.ora.customization.CustomizationHandler;

/**
 * Created by gimmi on 7/2/2016.
 */

public class OraWorkerService extends Service {

    private OraTimeReceiver broadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("TAVON", "Service commanded....");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.v("TAVON", "Service started");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction("android.appwidget.action.APPWIDGET_UPDATE");
        broadcastReceiver = new OraTimeReceiver();
        registerReceiver(broadcastReceiver, intentFilter);
        Log.v("TAVON", "registered intent filter");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        Log.v("TAVON", "unregistered intent filter");
    }

}
