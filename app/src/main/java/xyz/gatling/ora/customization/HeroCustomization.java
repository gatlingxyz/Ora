package xyz.gatling.ora.customization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MotionEventCompat;

import xyz.gatling.ora.R;

/**
 * Created by gimmi on 7/3/2016.
 */

public class HeroCustomization {
    int resId = R.drawable.iota_bg_one;
    String filepath;
    int opacity = 100;
    int overflowOpacity;

    public Bitmap createBitmap(Context context){
        Bitmap bitmap = ((BitmapDrawable)context.getResources().getDrawable(resId)).getBitmap();
        return bitmap.isMutable() ? bitmap : bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public Canvas createCanvas(Bitmap bitmap){
        return new Canvas(adjustOpacity(bitmap, opacity));
    }

    private Bitmap adjustOpacity(Bitmap bitmap, int opacity) {
        new Canvas(bitmap).drawColor((opacity & MotionEventCompat.ACTION_MASK) << 24, PorterDuff.Mode.DST_IN);
        return bitmap;
    }
}
