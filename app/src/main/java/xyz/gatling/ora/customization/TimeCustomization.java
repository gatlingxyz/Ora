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

    public static final Creator<BaseDateTimeCustomization> CREATOR = new Creator<BaseDateTimeCustomization>() {
        @Override
        public TimeCustomization createFromParcel(Parcel in) {
            return new TimeCustomization(in);
        }

        @Override
        public TimeCustomization[] newArray(int size) {
            return new TimeCustomization[size];
        }
    };

    TimeCustomization(Parcel in){
        super(in);
        format = in.readString();
        isAmPmCapitalized = in.readInt() == 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(format);
        dest.writeInt(isAmPmCapitalized ? 0 : 1);
    }
}
