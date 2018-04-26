package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.d3ifcool.ppob_counterpulsa.controller.StorageController;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Andy on 4/23/2018.
 */

public class FragmentRecapTransaction extends Fragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView loadRecapTransaction = view.findViewById(R.id.loadRecapTransaction);
        StorageController storageController = new StorageController(getActivity());

        Cursor getTransaction = storageController.getTransaction();
        if(getTransaction != null){
            TransactionAdapter adapter = new TransactionAdapter(getActivity());
            loadRecapTransaction.setAdapter(adapter);
        }
        else{
            Cursor getUser = storageController.getUserSession();
            if(getUser.moveToFirst()) {
                final StringBuilder stringService = new StringBuilder();
                stringService.append("action").append("=").append("getTransactionAPI").append("&");
                stringService.append("idUser").append("=").append(getUser.getInt(getUser.getColumnIndex("id_user"))).append("&");
                stringService.append("limit").append("=").append("10");
                new loadTransaction(stringService, storageController, loadRecapTransaction).execute();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recap_transaction_fragment, container, false);
    }

    @SuppressLint("StaticFieldLeak")
    private class loadTransaction extends AsyncTask<Void, Void, Boolean> {
        private StringBuilder packageTransactionOrderToPOST;
        private StorageController storageController;
        private ListView loadRecapTransaction;
        private ProgressDialog progressDialog;

        loadTransaction(StringBuilder packageTransactionOrderToPOST, StorageController storageController, ListView loadRecapTransaction) {
            this.packageTransactionOrderToPOST = packageTransactionOrderToPOST;
            this.storageController = storageController;
            this.loadRecapTransaction = loadRecapTransaction;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Mohon tunggu sebentar");
            progressDialog.setCancelable(false);
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL targetUrl = new URL("http://counterpulsa.xyz/try/base/php/transaksi.php");
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(this.packageTransactionOrderToPOST.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line).append('\n');

                if (!TextUtils.isEmpty(stringBuilder.toString())) {
                    try {
                        JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                        for (int i = 0; i < jsonArray.length(); i++){
                            ArrayList<String> columnName = new ArrayList<>();
                            columnName.add("id_transaction");
                            columnName.add("id_service");
                            columnName.add("phone_number");
                            columnName.add("date");
                            columnName.add("time");
                            columnName.add("transaction_status");
                            columnName.add("service_name");
                            columnName.add("service_category");
                            columnName.add("service_price");

                            ArrayList<String> rowsValue = new ArrayList<>();
                            rowsValue.add(String.valueOf(jsonArray.getJSONObject(i).getInt("id_transaksi")));
                            rowsValue.add(String.valueOf(jsonArray.getJSONObject(i).getInt("id_service")));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("no_hp"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("tanggal"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("waktu"));
                            rowsValue.add(String.valueOf(jsonArray.getJSONObject(i).getInt("status_transaksi")));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("nama_service"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("kategori_service"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("harga_service"));

                            this.storageController.insertTransaction(columnName, rowsValue);
                        }
                        return true;
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(!aBoolean)
                Toast.makeText(getActivity(), "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
            else {
                TransactionAdapter adapter = new TransactionAdapter(getActivity());
                loadRecapTransaction.setAdapter(adapter);
            }
            progressDialog.dismiss();
        }
    }
}
