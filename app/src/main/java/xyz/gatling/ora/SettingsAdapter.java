package xyz.gatling.ora;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gimmi on 7/3/2016.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>{

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsViewHolder(new SettingsItem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        SettingsListItem item = settingsListItems.get(position);
        holder.settingsItem.setTitleAndSubtitle(item.title, item.subtitle);
        if(item.previewIsText){
            holder.settingsItem.setRightPreview(item.previewText);
        }
        else{
            holder.settingsItem.setRightPreview(((BitmapDrawable)holder.settingsItem.getContext().getResources().getDrawable(item.previewResId)).getBitmap());
        }
    }

    @Override
    public int getItemCount() {
        return settingsListItems.size();
    }

    public class SettingsViewHolder extends RecyclerView.ViewHolder{

        SettingsItem settingsItem;

        public SettingsViewHolder(SettingsItem itemView) {
            super(itemView);
            this.settingsItem = itemView;
        }
    }

    private static class SettingsListItem{
        public String title;
        public String subtitle;
        public boolean previewIsText;
        public String previewText;
        public int previewResId;

        public SettingsListItem(String title, String subtitle, String previewText) {
            this.title = title;
            this.subtitle = subtitle;
            this.previewIsText = true;
            this.previewText = previewText;
        }

        public SettingsListItem(String title, String subtitle, int previewResId) {
            this.title = title;
            this.subtitle = subtitle;
            this.previewIsText = false;
            this.previewResId = previewResId;
        }
    }

    public static List<SettingsListItem> settingsListItems = new ArrayList<>();
    static{
        settingsListItems.add(new SettingsListItem("Organization", "Select your organization", "AKA"));
        settingsListItems.add(new SettingsListItem("Main Image", "Select your main image", R.drawable.iota_bg_one));
        settingsListItems.add(new SettingsListItem("Main Image Transparency", "Select the transparency", "100%"));
        settingsListItems.add(new SettingsListItem("Menu Transparency", "Change the transparency of the overflow menu", "100%"));
        settingsListItems.add(new SettingsListItem("Time Settings", "Change setting related to the time", ""));
        settingsListItems.add(new SettingsListItem("Date Settings", "Change settings related to the date", ""));
    }

}
