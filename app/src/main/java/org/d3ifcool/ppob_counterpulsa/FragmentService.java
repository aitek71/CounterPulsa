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

public class FragmentService extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView loadServiceItem = view.findViewById(R.id.loadServiceItem);
        StorageController storageController = new StorageController(getActivity());

        Cursor getService = storageController.getService();
        if(getService != null){
            ServiceAdapter serviceAdapter = new ServiceAdapter(getActivity());
            loadServiceItem.setAdapter(serviceAdapter);
        }
        else{
            final StringBuilder stringService = new StringBuilder();
            stringService.append("action").append("=").append("getAllServiceAPI");
            new loadService(stringService, storageController, loadServiceItem).execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class loadService extends AsyncTask<Void, Void, Boolean> {
        private StringBuilder packageServiceOrderToPOST;
        private StorageController storageController;
        private ListView loadServiceItem;
        private ProgressDialog progressDialog;

        loadService(StringBuilder packageServiceOrderToPOST, StorageController storageController, ListView loadServiceItem) {
            this.packageServiceOrderToPOST = packageServiceOrderToPOST;
            this.storageController = storageController;
            this.loadServiceItem = loadServiceItem;
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
                URL targetUrl = new URL("http://counterpulsa.xyz/try/base/php/produk.php");
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(this.packageServiceOrderToPOST.toString());
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
                            columnName.add("id_service");
                            columnName.add("service_name");
                            columnName.add("service_category");
                            columnName.add("service_price");
                            columnName.add("service_status");

                            ArrayList<String> rowsValue = new ArrayList<>();
                            rowsValue.add(String.valueOf(jsonArray.getJSONObject(i).getInt("id_service")));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("nama_service"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("kategori_service"));
                            rowsValue.add(String.valueOf(jsonArray.getJSONObject(i).getInt("harga_service")));
                            rowsValue.add(String.valueOf(jsonArray.getJSONObject(i).getInt("status_service")));

                            this.storageController.insertService(columnName, rowsValue);
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
                ServiceAdapter adapter = new ServiceAdapter(getActivity());
                loadServiceItem.setAdapter(adapter);
            }
            progressDialog.dismiss();
        }
    }
}
