package xyz.gatling.ora.customization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by gimmi on 7/2/2016.
 */

public class Customization implements Parcelable{

    private transient Date snapshot;

    private transient Float multiplier;
    private transient int appWidgetId;

    public DateCustomization date;
    public TimeCustomization time;
    public HeroCustomization hero;

    public Customization(){
        date = new DateCustomization();
        time = new TimeCustomization();
        hero = new HeroCustomization();
    }

    protected Customization(Parcel in) {
        appWidgetId = in.readInt();
    }

    public static final Creator<Customization> CREATOR = new Creator<Customization>() {
        @Override
        public Customization createFromParcel(Parcel in) {
            return new Customization(in);
        }

        @Override
        public Customization[] newArray(int size) {
            return new Customization[size];
        }
    };

    public Bitmap draw(Context context){
        Bitmap bitmap = hero.createBitmap(context);
        Canvas canvas = hero.createCanvas(bitmap);

        setDefaultsIfNeeded(canvas);

        snapshot = new Date();
        time.addCustomization(canvas, snapshot);
        date.addCustomization(canvas, snapshot);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(appWidgetId);
    }

}
