package xyz.gatling.ora;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import xyz.gatling.ora.customization.Customization;
import xyz.gatling.ora.customization.CustomizationHandler;

public class MainActivity extends AppCompatActivity {

    private int appWidgetId;
    private Customization customization;
    ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appWidgetId = getIntent().getExtras().getInt("appWidgetId", -1);
        if(appWidgetId == -1){
            finish();
        }
        setContentView(R.layout.activity_main);
        setTitle("Customizing Widget #" + appWidgetId);
        startService(new Intent(this, OraWorkerService.class));

        //Org
        //Background
        //Trans
        //Overflow
        //Time
        // - Size
        // - Color
        // - Shadow
        // - Shadow Rad
        // - Strokke
        // + Stroke color
        // ? Format
        // ? Ampm cap
        // ? position, xy
        //Date
        // ? Day of week
        // ? Month/Day
        // ? year
        // ? position, xy
//        settings.setRightPreview(((BitmapDrawable)getResources().getDrawable(R.drawable.iota_bg_two)).getBitmap());
//        settings.setRightPreview("100%");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SettingsAdapter());

        mainImage = (ImageView) findViewById(R.id.widget_image);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        if(Build.VERSION.SDK_INT >= 16) {
            mainImage.setBackground(wallpaperManager.getDrawable());
        }
        else{
            mainImage.setBackgroundDrawable(wallpaperManager.getDrawable());
        }

        Log.v("TAVON", "About to request Drawer");

        customization = CustomizationHandler.getInstance(this).getCustomizationForWidget(appWidgetId);

        mainImage.setImageBitmap(customization.draw(this));

        mainImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                customization.time.x = event.getX();
                customization.time.y = event.getY();
                customization.date.x = event.getX();
                customization.date.y = event.getY()+customization.time.size;
                mainImage.setImageBitmap(customization.draw(MainActivity.this));
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        CustomizationHandler.getInstance(this).addOrUpdateCustomizationForWidget(appWidgetId, customization);
        Intent resultValue = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(resultValue);
        setResult(RESULT_OK, resultValue);
        super.onBackPressed();
    }
}
