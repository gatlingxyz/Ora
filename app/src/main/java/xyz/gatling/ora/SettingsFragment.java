package xyz.gatling.ora;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xyz.gatling.ora.customization.Customization;
import xyz.gatling.ora.customization.CustomizationHandler;

/**
 * Created by gimmi on 7/2/2016.
 */

public class SettingsFragment extends Fragment {

    RecyclerView recyclerView;
    int appWidgetId;
    OnSettingsUpdatedListener onSettingsUpdatedListener;
    SettingsAdapter adapter;
    Customization customization;

    public interface OnSettingSelectedListener{
        public void onSettingSelected(String valueSelected);
    }

    public interface OnSettingsUpdatedListener{
        public void onSettingsUpdated(Customization customization);
    }


    public static SettingsFragment newInstance(OnSettingsUpdatedListener onSettingsUpdatedListener, int appWidgetId){
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(bundle);
        fragment.setOnSettingsUpdatedListener(onSettingsUpdatedListener);
        return fragment;
    }

    public void setOnSettingsUpdatedListener(OnSettingsUpdatedListener onSettingsUpdatedListener) {
        this.onSettingsUpdatedListener = onSettingsUpdatedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appWidgetId = getArguments().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        customization = getCustomization();
        if(OraWorkerService.s3listings == null){
            getActivity().finish();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final List<SettingsItem> settingsItems = new ArrayList<>();
        organizationSetting = new OrganizationSetting();
        heroImageCustomization = new HeroImageCustomization();
        settingsItems.add(organizationSetting);
        settingsItems.add(heroImageCustomization);
//        settingsItems.add(new SettingsItem("Main Image Transparency", "Select the transparency", "100%", getListDialog(orgs)));
//        settingsItems.add(new SettingsItem("Menu Transparency", "Change the transparency of the overflow menu", "100%", getListDialog(orgs)));
//        settingsItems.add(new SettingsItem("Time Settings", "Change setting related to the time", "", getListDialog(orgs)));
//        settingsItems.add(new SettingsItem("Date Settings", "Change settings related to the date", "", getListDialog(orgs)));

        adapter = new SettingsAdapter(settingsItems);
        recyclerView.setAdapter(adapter);



        //Org
        //Background
        //Trans
        //Overflow
        //Time
        // - Size
        // - Color
        // - Shadow
        // - Shadow Rad
        // - Strokke
        // + Stroke color
        // ? Format
        // ? Ampm cap
        // ? position, xy
        //Date
        // ? Day of week
        // ? Month/Day
        // ? year
        // ? position, xy
//        settings.setRightPreview(((BitmapDrawable)getResources().getDrawable(R.drawable.iota_bg_two)).getBitmap());
//        settings.setRightPreview("100%");

    }


    private OrganizationSetting organizationSetting;
    private HeroImageCustomization heroImageCustomization;

    private class OrganizationSetting extends SettingsItem{
        public OrganizationSetting(){
            title = "Organization";
            subtitle = "First, select your organization";
            previewIsText = true;
            dialog = getListDialog(
                    new ArrayList<>(OraWorkerService.organizationToGreekMapping.keySet()),
                    this
            );
        }

        @Override
        public void onSettingSelected(String valueSelected) {
            customization.hero.organization = valueSelected;
            previewIsText = true;
            previewText = OraWorkerService.organizationToGreekMapping.get(valueSelected);
            heroImageCustomization.dialog = getListDialog(OraWorkerService.organizationToHeroMapping.get(valueSelected), heroImageCustomization);
            heroImageCustomization.dialog.show();
            onSettingsUpdatedListener.onSettingsUpdated(customization);
        }
    }

    private class HeroImageCustomization extends SettingsItem{
        public HeroImageCustomization() {
            title = "Main image";
            subtitle = "Next, select the main image to display";
            previewIsText = false;
        }

        @Override
        public void onSettingSelected(String valueSelected) {
            customization.hero.whichImage = valueSelected;
            previewIsText = false;
            new AsyncTask<Void, Void, Bitmap>(){
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return customization.hero.createBitmap(getActivity());
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    previewBitmap = bitmap;
                    adapter.notifyDataSetChanged();
                }
            }.execute();
            onSettingsUpdatedListener.onSettingsUpdated(customization);
        }
    }

    private Customization getCustomization(){
        return CustomizationHandler.getInstance(getActivity()).getCustomizationForWidget(appWidgetId);
    }

    private class BasicStringAdapter extends BaseAdapter {

        List<String> items = new ArrayList<>();

        public BasicStringAdapter(List<String> items) {
            this.items = items;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BasicViewHolder viewHolder;
            if(convertView == null) {
                viewHolder = new BasicViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, null);
                viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder =(BasicViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(getItem(position));
            return convertView;
        }

        private class BasicViewHolder {
            TextView textView;
        }
    }

    private Dialog getListDialog(final List<String> items, final OnSettingSelectedListener onSettingSelectedListener){
        return new AlertDialog.Builder(getActivity())
                .setAdapter(new BasicStringAdapter(items), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onSettingSelectedListener.onSettingSelected(items.get(which));
                    }
                })
                .create();
    }

    
}
