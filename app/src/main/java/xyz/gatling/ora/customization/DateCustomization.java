package xyz.gatling.ora.customization;

import android.os.Parcel;

/**
 * Created by gimmi on 7/3/2016.
 */

public class DateCustomization extends BaseDateTimeCustomization {
    String format = "MMMM dd, y";
    String formatMonth = "MMMM";
    String formatDay = "dd";
    String formatYear = "y";

    public DateCustomization(){
        format = "MMMM dd, y";
    }

    @Override
    public String getFormat() {
        return format;
    }
}
