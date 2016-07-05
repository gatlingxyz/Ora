package xyz.gatling.ora;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by gimmi on 7/4/2016.
 */

public class Util {
    public static float getDp(Context context, int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
