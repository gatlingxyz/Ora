package xyz.gatling.ora.customization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MotionEventCompat;

import xyz.gatling.ora.CacheHandler;
import xyz.gatling.ora.R;

/**
 * Created by gimmi on 7/3/2016.
 */

public class HeroCustomization {
    public String organization = "Ora";
    public String whichImage = "Icon";
    public int opacity = 255;
    public int overflowOpacity = 255;

    public Bitmap createBitmap(Context context){
        Bitmap bitmap = CacheHandler.loadImage(context, this);
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
