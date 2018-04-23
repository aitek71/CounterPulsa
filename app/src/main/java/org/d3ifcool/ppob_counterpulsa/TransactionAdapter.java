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
import org.d3ifcool.ppob_counterpulsa.model.TransactionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andy on 4/23/2018.
 */

public class TransactionAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<TransactionModel> transactionModels;

    public TransactionAdapter(Activity activity) {
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        StorageController storageController = new StorageController(activity);
        this.transactionModels = new ArrayList<>();
        Cursor getTransaction = storageController.getTransaction();
        while (getTransaction.moveToNext()) {
            transactionModels.add(new TransactionModel(
                    getTransaction.getInt(getTransaction.getColumnIndex("id_transaction")),
                    getTransaction.getInt(getTransaction.getColumnIndex("id_service")),
                    getTransaction.getInt(getTransaction.getColumnIndex("service_price")),
                    getTransaction.getInt(getTransaction.getColumnIndex("transaction_status")),
                    getTransaction.getString(getTransaction.getColumnIndex("phone_number")),
                    getTransaction.getString(getTransaction.getColumnIndex("date")),
                    getTransaction.getString(getTransaction.getColumnIndex("time")),
                    getTransaction.getString(getTransaction.getColumnIndex("service_name")),
                    getTransaction.getString(getTransaction.getColumnIndex("service_category"))
            ));
        }
    }

    @Override
    public int getCount() {
        return transactionModels.size();
    }

    @Override
    public Object getItem(int i) {
        return transactionModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = this.layoutInflater.inflate(R.layout.item_transaction, null);

        TextView transaction_title, transaction_description, transaction_status, transaction_kategori;
        transaction_title = view.findViewById(R.id.transaction_title);
        transaction_description = view.findViewById(R.id.transaction_description);
        transaction_status = view.findViewById(R.id.transaction_status);
        transaction_kategori = view.findViewById(R.id.transaction_kategori);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("in", "ID"));
        try {
            Date date = newDateFormat.parse(transactionModels.get(i).getDate());
            newDateFormat.applyPattern("EEEE d MMM yyyy");
            String myDate = newDateFormat.format(date);
            String myTime = transactionModels.get(i).getTime();

            String status;
            if (transactionModels.get(i).getTransaction_status() == 0) {
                status = "Menunggu";
                transaction_status.setTextColor(transaction_status.getResources().getColor(R.color.colorAccent));
            } else if (transactionModels.get(i).getTransaction_status() == 2) {
                status = "Berhasil";
                transaction_status.setTextColor(transaction_status.getResources().getColor(R.color.colorButtonLogin));
            } else {
                status = "Gagal";
                transaction_status.setTextColor(transaction_status.getResources().getColor(R.color.colorButtonRegistration));
            }
            transaction_title.setText("TRX : " + myDate + " | " + myTime);
            transaction_description.setText("Transaksi pada " + transactionModels.get(i).getPhone_number() + " " + transactionModels.get(i).getService_name());
            transaction_status.setText(status);
            transaction_kategori.setText(transactionModels.get(i).getService_category());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }
}
