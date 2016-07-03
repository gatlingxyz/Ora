package xyz.gatling.ora.customization;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xyz.gatling.ora.R;

/**
 * Created by gimmi on 7/2/2016.
 */

public class CustomizationHandler {

    private static CustomizationHandler instance;
    public static CustomizationHandler getInstance(Context context){
        if(instance == null){
            instance = new CustomizationHandler();
            instance.restore(context);
        }
        return instance;
    }

    private SparseArray<Customization> customizationSparseArray = new SparseArray<>();

    public CustomizationHandler() {
        Log.v("TAVON", "Drawer created");
    }

    public Customization getCustomizationForWidget(int id){
        return customizationSparseArray.get(id, new Customization());
    }

    public void addOrUpdateCustomizationForWidget(int id, Customization customization){
        customizationSparseArray.put(id, customization);
    }

    public void removeCustomizationForWidget(int appWidgetId) {
        customizationSparseArray.delete(appWidgetId);
    }

    public void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] ids){
        RemoteViews remoteViews;
        for(int id : ids) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.include_widget_b);
            updateWidget(context, id, remoteViews);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
        save(context);
    }

    private RemoteViews updateWidget(Context context, int id, RemoteViews remoteViews){
        Customization customization = customizationSparseArray.get(id, new Customization());
        remoteViews.setImageViewBitmap(R.id.widget_consolidated, customization.draw(context));
        return remoteViews;
    }

    private void save(Context context){
        String serialized = new Gson().toJson(customizationSparseArray);
        SharedPreferences sharedPreferences = context.getSharedPreferences("OraCustomizations", 0);
        sharedPreferences
                .edit()
                .putString(CustomizationHandler.class.getName(), serialized)
                .apply();
        Log.v("TAVON", "Saving: " + serialized);
    }

    private void restore(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("OraCustomizations", 0);
        String serialized = sharedPreferences.getString(CustomizationHandler.class.getName(), null);
        if(serialized == null){
            customizationSparseArray = new SparseArray<>();
            Log.v("TAVON", "No restore. New sparse array.");
        }
        else{
            try {
                customizationSparseArray = new SparseArray<>();

                JSONObject jsonObject = new JSONObject(serialized);
                JSONArray mKeys = jsonObject.getJSONArray("mKeys");
                JSONArray mValues = jsonObject.getJSONArray("mValues");
                for(int i = 0; i < mKeys.length(); i++){
                    int id = mKeys.getInt(i);
                    String value = mValues.getString(i);
                    if(!TextUtils.isEmpty(value)){
                        customizationSparseArray.put(id, new Gson().fromJson(value, Customization.class));
                    }
                }

                Log.v("TAVON", "Successfully restored");
            }
            catch (JsonSyntaxException jse){
                customizationSparseArray = new SparseArray<>();
                Log.v("TAVON", "Syntax error. New sparse array");
                jse.printStackTrace();
            }
            catch (JSONException je){
                customizationSparseArray = new SparseArray<>();
                Log.v("TAVON", "Json exception. New sparse array");
                je.printStackTrace();
            }
        }
    }


}
