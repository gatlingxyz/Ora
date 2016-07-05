package xyz.gatling.ora;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import xyz.gatling.ora.customization.Customization;
import xyz.gatling.ora.customization.CustomizationHandler;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnSettingsUpdatedListener {

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

        mainImage = (ImageView) findViewById(R.id.widget_image);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        if(Build.VERSION.SDK_INT >= 16) {
            mainImage.setBackground(wallpaperManager.getDrawable());
        }
        else{
            mainImage.setBackgroundDrawable(wallpaperManager.getDrawable());
        }

        customization = CustomizationHandler.getInstance(this).getCustomizationForWidget(appWidgetId);

        onSettingsUpdated(customization);

        if(OraWorkerService.organizationToHeroMapping == null || OraWorkerService.organizationToHeroMapping.isEmpty()){
            new S3Task(this, new S3Task.OnFinished() {
                @Override
                public void onFinished() {
                    showSettings();
                }
            }).execute();
        }
        else{
           showSettings();
        }

//        mainImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                customization.time.x = event.getX();
//                customization.time.y = event.getY();
//                customization.date.x = event.getX();
//                customization.date.y = event.getY()+customization.time.size;
//                onSettingsUpdated(customization);
//                return false;
//            }
//        });

    }

    private void showSettings(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.right_panel, SettingsFragment.newInstance(this,
                        appWidgetId))
                .commit();
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

    @Override
    public void onSettingsUpdated(final Customization customization) {
        this.customization = customization;
        new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                return customization.draw(MainActivity.this);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                mainImage.setImageBitmap(bitmap);
            }
        }.execute();
    }
}
