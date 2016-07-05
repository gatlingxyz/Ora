package xyz.gatling.ora;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gimmi on 7/2/2016.
 */

public class OraWorkerService extends Service {

    public static ObjectListing s3listings;
    public static Map<String, String> organizationToGreekMapping = new HashMap<>();
    public static Map<String, List<String>> organizationToHeroMapping = new HashMap<>();
    S3Task s3Task;
    int count = 0;

    private OraTimeReceiver broadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        s3Task = new S3Task(this);
        s3Task.execute();
//        Log.v("TAVON", "Service started");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction("android.appwidget.action.APPWIDGET_UPDATE");
        broadcastReceiver = new OraTimeReceiver();
        registerReceiver(broadcastReceiver, intentFilter);
//        Log.v("TAVON", "registered intent filter");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.v("TAVON", "Service commanded....");
        if(s3Task.getStatus() == AsyncTask.Status.FINISHED && (s3listings == null || count++ == 60)){
            s3Task.execute();
            count = 0;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
//        Log.v("TAVON", "unregistered intent filter");
    }


}
