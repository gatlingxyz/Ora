package xyz.gatling.ora.customization;

import android.os.Parcel;

/**
 * Created by gimmi on 7/3/2016.
 */

public class TimeCustomization extends BaseDateTimeCustomization {
    String format = "KK:mm";
    boolean isAmPmCapitalized = true;

    public TimeCustomization(){
        format = "KK:mm";
    }

    @Override
    public String getFormat() {
        return format;
    }
}
