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

public abstract class BaseDateTimeCustomization implements Parcelable {
    private transient SimpleDateFormat reuseableDateFormat = new SimpleDateFormat();
    private transient Paint paint = new Paint();

    public String color = "#FFFFFF";
    public int size = 100;
    public String shadowColor = "#000000";
    public int shadowRadius = 2;
    public int strokeSize = 0;
    public String strokeColor;
    public float x = -1;
    public float y = -1;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        dest.writeInt(size);
        dest.writeString(shadowColor);
        dest.writeInt(shadowRadius);
        dest.writeInt(strokeSize);
        dest.writeString(strokeColor);
        dest.writeFloat(x);
        dest.writeFloat(y);
    }

    protected BaseDateTimeCustomization(Parcel in) {
        color = in.readString();
        size = in.readInt();
        shadowColor = in.readString();
        shadowRadius = in.readInt();
        strokeSize = in.readInt();
        strokeColor = in.readString();
        x = in.readFloat();
        y = in.readFloat();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    private String getFormattedDateTime(Date date, String format) {
        reuseableDateFormat.applyPattern(format);
        return reuseableDateFormat.format(date);
    }


}
