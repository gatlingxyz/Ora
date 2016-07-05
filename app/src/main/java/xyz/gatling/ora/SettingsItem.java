package xyz.gatling.ora;

import android.app.Dialog;
import android.graphics.Bitmap;

/**
 * Created by gimmi on 7/4/2016.
 */

public abstract class SettingsItem implements SettingsFragment.OnSettingSelectedListener {
    public String title;
    public String subtitle;
    public boolean previewIsText;
    public String previewText;
    public Bitmap previewBitmap;
    public Dialog dialog;

}
