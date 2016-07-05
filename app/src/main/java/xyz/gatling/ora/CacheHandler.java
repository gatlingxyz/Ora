package xyz.gatling.ora;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import xyz.gatling.ora.customization.HeroCustomization;

/**
 * Created by gimmi on 7/4/2016.
 */

public class CacheHandler{

    private static final String BASE = "https://s3.amazonaws.com/xyz.gatling.ora.heroimages/";
    private static Bitmap defaultBitmap;
    private static int defaultSize = -1;

    public static Bitmap loadImage(Context context, HeroCustomization customization){
        if(defaultSize == -1){
            defaultSize = (int)Util.getDp(context, 400);
        }

        try {
            final String chosen = customization.organization.replace(" ", "+") + "/" + customization.whichImage + ".png";
            String url = BASE + chosen;
//            Log.v("TAVON", "Attempting to load " + url);
            return Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
//                    .listener(listener)
                    .into(defaultSize, defaultSize)
                    .get();
        }
        catch (Exception e){
            e.printStackTrace();
            if(defaultBitmap == null){
                defaultBitmap = Bitmap.createBitmap(defaultSize, defaultSize, Bitmap.Config.ARGB_8888);
            }
            return defaultBitmap;
        }
    }

    private static RequestListener<String, Bitmap> listener = new RequestListener<String, Bitmap>() {
        @Override
        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.v("TAVON", model + " is " + (isFromMemoryCache ? "" : "not ") + "from cache");
            return false;
        }
    };
}
