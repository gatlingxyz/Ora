package xyz.gatling.ora;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import xyz.gatling.ora.customization.Customization;
import xyz.gatling.ora.customization.CustomizationHandler;

/**
 * Created by gimmi on 7/2/2016.
 */

public class OraTimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        CustomizationHandler.getInstance(context).updateWidgets(context, manager, manager.getAppWidgetIds(new ComponentName(context, OraWidget.class)));
    }
}
