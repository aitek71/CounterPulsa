package org.d3ifcool.ppob_counterpulsa;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.d3ifcool.ppob_counterpulsa.controller.StorageController;
import org.d3ifcool.ppob_counterpulsa.model.AnnouncemenetModel;

import java.util.ArrayList;

/**
 * Created by Andy on 4/23/2018.
 */

public class AnnouncementAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<AnnouncemenetModel> announcemenetModels;

    public AnnouncementAdapter(Activity activity) {
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        StorageController storageController = new StorageController(activity);
        announcemenetModels = new ArrayList<>();
        Cursor getAnnouncement = storageController.getAnnouncement();
        while (getAnnouncement.moveToNext()){
            announcemenetModels.add(new AnnouncemenetModel(
                    getAnnouncement.getInt(getAnnouncement.getColumnIndex("id_announcement")),
                    getAnnouncement.getString(getAnnouncement.getColumnIndex("title")),
                    getAnnouncement.getString(getAnnouncement.getColumnIndex("description")),
                    getAnnouncement.getString(getAnnouncement.getColumnIndex("date")),
                    getAnnouncement.getString(getAnnouncement.getColumnIndex("time")),
                    getAnnouncement.getString(getAnnouncement.getColumnIndex("image_resource"))
            ));
        }
    }

    @Override
    public int getCount() {
        return announcemenetModels.size();
    }

    @Override
    public Object getItem(int i) {
        return announcemenetModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        return view;
    }
}
