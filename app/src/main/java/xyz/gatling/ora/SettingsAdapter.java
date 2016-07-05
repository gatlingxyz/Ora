package xyz.gatling.ora;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gimmi on 7/3/2016.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>{

    public List<SettingsItem> SettingsItems = new ArrayList<>();

    public SettingsAdapter(List<SettingsItem> SettingsItems) {
        this.SettingsItems = SettingsItems;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsViewHolder(new SettingsItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        final SettingsItem item = SettingsItems.get(position);
        holder.settingsItemView.setTitleAndSubtitle(item.title, item.subtitle);
        if(item.previewIsText){
            holder.settingsItemView.setRightPreview(item.previewText);
        }
        else{
            holder.settingsItemView.setRightPreview(item.previewBitmap);
        }
        holder.settingsItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.dialog != null) {
                    item.dialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return SettingsItems.size();
    }

    public class SettingsViewHolder extends RecyclerView.ViewHolder{

        SettingsItemView settingsItemView;

        public SettingsViewHolder(SettingsItemView itemView) {
            super(itemView);
            this.settingsItemView = itemView;
        }
    }

   

}
