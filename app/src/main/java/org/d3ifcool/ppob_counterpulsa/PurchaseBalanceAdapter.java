package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.d3ifcool.ppob_counterpulsa.controller.StorageController;
import org.d3ifcool.ppob_counterpulsa.model.PurchaseBalanceModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PurchaseBalanceAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<PurchaseBalanceModel> item_list;

    PurchaseBalanceAdapter(Activity activity) {
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        StorageController storageController = new StorageController(activity);
        Cursor cursor = storageController.getPurchaseBalance();
        item_list = new ArrayList<>();

        if (cursor != null){
            while (cursor.moveToNext()){
                item_list.add(new PurchaseBalanceModel(
                        cursor.getInt(cursor.getColumnIndex("id_purchase_balance")),
                        cursor.getInt(cursor.getColumnIndex("nominal_purchase")),
                        cursor.getInt(cursor.getColumnIndex("balance_price")),
                        cursor.getInt(cursor.getColumnIndex("status")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("proof_of_payment"))));
            }
        }
    }

    @Override
    public int getCount() {
        return item_list.size();
    }

    @Override
    public Object getItem(int i) {
        return item_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.item_purchase_balance, null);
        TextView purchaseDateTime, purchaseDescription, purchaseStatus;
        purchaseDateTime = view.findViewById(R.id.purchaseDateTime);
        purchaseDescription = view.findViewById(R.id.purchaseDescription);
        purchaseStatus = view.findViewById(R.id.purchaseStatus);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("in", "ID"));
        try {
            Date date = newDateFormat.parse(item_list.get(i).getDate());
            newDateFormat.applyPattern("EEEE d MMM yyyy");
            String myDate = newDateFormat.format(date);
            String myTime = item_list.get(i).getTime();

            String status;
            if(item_list.get(i).getStatus() == 0){
                status = "Menunggu";
                purchaseStatus.setTextColor(purchaseStatus.getResources().getColor(R.color.colorAccent));
            }
            else if(item_list.get(i).getStatus() == 2){
                status = "Berhasil";
                purchaseStatus.setTextColor(purchaseStatus.getResources().getColor(R.color.colorButtonLogin));
            }
            else{
                status = "Gagal";
                purchaseStatus.setTextColor(purchaseStatus.getResources().getColor(R.color.colorButtonRegistration));
            }

            purchaseDateTime.setText("PSN : " + myDate + " | " + myTime);
            purchaseDescription.setText("Pengisian dompet sebesar Rp." + item_list.get(i).getNominal_purchase() + " dengan harga Rp." + item_list.get(i).getBalance_price());
            purchaseStatus.setText(status);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}
