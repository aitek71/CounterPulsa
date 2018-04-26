package org.d3ifcool.ppob_counterpulsa;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.d3ifcool.ppob_counterpulsa.controller.StorageController;
import org.d3ifcool.ppob_counterpulsa.model.ServiceModel;

import java.util.ArrayList;

/**
 * Created by Andy on 4/23/2018.
 */

public class ServiceAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ServiceModel> serviceModels;

    public ServiceAdapter(Activity activity) {
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.serviceModels = new ArrayList<>();

        StorageController storageController = new StorageController(activity);
        Cursor cursor = storageController.getService();
        while (cursor.moveToNext()){
            this.serviceModels.add(new ServiceModel(
                    cursor.getInt(cursor.getColumnIndex("id_service")),
                    cursor.getInt(cursor.getColumnIndex("service_price")),
                    cursor.getInt(cursor.getColumnIndex("service_status")),
                    cursor.getString(cursor.getColumnIndex("service_name")),
                    cursor.getString(cursor.getColumnIndex("service_category"))
            ));
        }
    }

    @Override
    public int getCount() {
        return this.serviceModels.size();
    }

    @Override
    public Object getItem(int i) {
        return (ServiceModel) this.serviceModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = this.layoutInflater.inflate(R.layout.item_service, null);

        TextView service_name, service_price, service_status, service_kategori;
        service_name = view.findViewById(R.id.service_name);
        service_price = view.findViewById(R.id.service_price);
        service_status = view.findViewById(R.id.service_status);
        service_kategori = view.findViewById(R.id.service_kategori);

        service_name.setText(this.serviceModels.get(i).getService_name());
        service_price.setText("Harga : " + this.serviceModels.get(i).getService_price());
        String status = null;
        if (this.serviceModels.get(i).getService_status() == 0 && this.serviceModels.get(i).getService_status() == 1){
            status = "Tidak aktif";
        }
        else{
            status = "Aktif";
        }
        service_status.setText(status);
        service_kategori.setText(this.serviceModels.get(i).getService_category());

        return view;
    }
}
