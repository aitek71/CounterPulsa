package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.d3ifcool.ppob_counterpulsa.controller.StorageController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class FragmentTransaction extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transaction_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //noinspection ConstantConditions
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.item_transaction));

        final Spinner spinner = view.findViewById(R.id.loadService);

        final StorageController storageController = new StorageController(getActivity());
        Cursor getService = storageController.getService();
        if (getService != null){
            ArrayList<String> spinnerList = new ArrayList<>();
            while (getService.moveToNext()){
                spinnerList.add(getService.getString(getService.getColumnIndex("service_name")));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else{
            final StringBuilder stringService = new StringBuilder();
            stringService.append("action").append("=").append("getAllServiceAPI");

            new loadService(stringService, storageController, spinner).execute();
        }
        final EditText transaction_phone_number = view.findViewById(R.id.transaction_phone_number);
        Button newTransaction = view.findViewById(R.id.newTransaction);
        newTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor getUser = storageController.getUserSession();
                if(getUser.moveToFirst()) {
                    final StringBuilder stringServiceNew = new StringBuilder();
                    stringServiceNew.append("action").append("=").append("newTransactionAPI").append("&");
                    stringServiceNew.append("nameService").append("=").append(spinner.getSelectedItem().toString()).append("&");
                    stringServiceNew.append("phoneNumber").append("=").append(transaction_phone_number.getText().toString()).append("&");
                    stringServiceNew.append("idUser").append("=").append(String.valueOf(getUser.getInt(getUser.getColumnIndex("id_user"))));

                    new newTransaction(stringServiceNew).execute();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class newTransaction extends AsyncTask<Void, Void, Boolean>{
        private StringBuilder packageServiceOrderToPOST;
        private ProgressDialog progressDialog;

        newTransaction(StringBuilder packageServiceOrderToPOST) {
            this.packageServiceOrderToPOST = packageServiceOrderToPOST;
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
                dataOutputStream.writeBytes(this.packageServiceOrderToPOST.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line).append('\n');

                if (!TextUtils.isEmpty(stringBuilder.toString())){
                    try {
                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                        return jsonObject.getString("status").equals("200");
                    } catch (JSONException e) {
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
                Toast.makeText(getActivity(), "Berhasil melakukan transaksi", Toast.LENGTH_SHORT).show();
                Fragment fragment = new FragmentMain();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loadFrame, fragment).commit();
            }
            progressDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class loadService extends AsyncTask<Void, Void, Boolean>{
        private StringBuilder packageServiceOrderToPOST;
        private StorageController storageController;
        private Spinner spinner;
        private ArrayList<String> spinnerList;

        loadService(StringBuilder packageServiceOrderToPOST, StorageController storageController, Spinner spinner) {
            this.packageServiceOrderToPOST = packageServiceOrderToPOST;
            this.storageController = storageController;
            this.spinner = spinner;
            spinnerList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                            this.spinnerList.add(jsonArray.getJSONObject(i).getString("nama_service"));

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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, this.spinnerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }
    }
}
