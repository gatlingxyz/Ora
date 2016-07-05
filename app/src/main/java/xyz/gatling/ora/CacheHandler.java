package xyz.gatling.ora;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import xyz.gatling.ora.customization.HeroCustomization;

/**
 * Created by gimmi on 7/4/2016.
 */


//https://s3.amazonaws.com/xyz.gatling.ora.heroimages/Ora/Icon.png

public class CacheHandler{

    private static String base = "https://s3.amazonaws.com/xyz.gatling.ora.heroimages/";

    public static Bitmap loadImage(Context context, HeroCustomization customization){
        int size = (int)Util.getDp(context, 400);
        try {
            final String chosen = customization.organization.replace(" ", "+") + "/" + customization.whichImage + ".png";
            String url = base + chosen;
//            Log.v("TAVON", "Attempting to load " + url);
            return Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
//                    .listener(listener)
                    .into(size, size)
                    .get();
        }
        catch (Exception e){
            e.printStackTrace();
            return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
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
