package xyz.gatling.ora;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import xyz.gatling.ora.customization.CustomizationHandler;

/**
 * Created by gimmi on 7/1/2016.
 */
public class OraWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        Log.v("TAVON", "Widget enabled");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("TAVON", "received " + intent.getAction());
        context.startService(new Intent(context, OraWorkerService.class));
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for(int id : appWidgetIds) {
            CustomizationHandler.getInstance(context).removeCustomizationForWidget(id);
        }
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, OraWorkerService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        CustomizationHandler.getInstance(context).updateWidgets(context, appWidgetManager, appWidgetIds);
    }

}
