package xyz.gatling.ora.customization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;

/**
 * Created by gimmi on 7/2/2016.
 */

public class Customization{

    private transient Date snapshot;

    private transient Float multiplier;

    public DateCustomization date;
    public TimeCustomization time;
    public HeroCustomization hero;

    public Customization(){
        date = new DateCustomization();
        time = new TimeCustomization();
        hero = new HeroCustomization();
    }

    public Bitmap draw(Context context){
        String tag = hero.organization + "/" + hero.whichImage;
        Log.v("TAVON | " + tag, "About to draw Customization");
        Bitmap bitmap = hero.createBitmap(context);
        Canvas canvas = hero.createCanvas(bitmap);

        setDefaultsIfNeeded(canvas);

        snapshot = new Date();
        time.addCustomization(canvas, snapshot);
        date.addCustomization(canvas, snapshot);
        Log.v("TAVON | " + tag, "customization drawn");
        return bitmap;
    }

    private void setDefaultsIfNeeded(Canvas canvas){
        if(time.x == -1) {
            time.x = canvas.getWidth() / 2;
        }
        if(time.y == -1) {
            time.y = canvas.getHeight() / 2;
        }
        if(date.x == -1) {
            date.x = time.x;
        }
        if(date.y == -1) {
            date.y = time.y + time.size;
        }
    }
}
