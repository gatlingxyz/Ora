package xyz.gatling.ora.customization;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gimmi on 7/3/2016.
 */

public abstract class BaseDateTimeCustomization {
    private transient SimpleDateFormat reuseableDateFormat = new SimpleDateFormat();
    private transient Paint paint = new Paint();

    public String color = "#FFFFFF";
    public int size = 100;
    public int opacity = 255;
    public String shadowColor = "#000000";
    public int shadowRadius = 2;
    public int strokeSize = 0;
    public String strokeColor;
    public float x = -1;
    public float y = -1;

    protected BaseDateTimeCustomization(){

    }

    public abstract String getFormat();

    public void addCustomization(Canvas canvas, Date snapshot){
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
//        paint.setTypeface(font);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(color));
        paint.setShadowLayer((float) shadowRadius, 2.0f, 2.0f, Color.parseColor(shadowColor));
        paint.setTextSize((float) size);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(getFormattedDateTime(snapshot, getFormat()), x, y, paint);
        if (strokeSize > 0) {
            paint.setColor(Color.parseColor(strokeColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) strokeSize);
            canvas.drawText(getFormattedDateTime(snapshot, getFormat()), x, y, paint);
        }
    }

    private String getFormattedDateTime(Date date, String format) {
        reuseableDateFormat.applyPattern(format);
        return reuseableDateFormat.format(date);
    }


}
