package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentBalance extends Fragment {
    private Cursor getUserSession;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    private void reloadData(final StringBuilder stringBuilder, final StorageController storageController, final ListView listView){
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new GetBalanceOrder(stringBuilder, storageController, listView).execute();
                timer.cancel();
            }
        }, 2000, 4000);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection ConstantConditions
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.item_balance));

        final StorageController storageController = new StorageController(getActivity());
         getUserSession = storageController.getUserSession();

        if (getUserSession != null){
            if(getUserSession.moveToFirst()){
                TextView balanceCurrent;
                balanceCurrent = view.findViewById(R.id.balanceCurrent);

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("Rp. ");
                decimalFormatSymbols.setGroupingSeparator('.');
                decimalFormatSymbols.setMonetaryDecimalSeparator('.');
                numberFormat.setMaximumFractionDigits(0);
                ((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);

                balanceCurrent.setText(numberFormat.format(getUserSession.getInt(getUserSession.getColumnIndex("current_balance"))));

                final StringBuilder stringPurchaseSaldo = new StringBuilder();
                stringPurchaseSaldo.append("action").append("=").append("getPurchaseSaldoAPI").append("&");
                stringPurchaseSaldo.append("idSaldo").append("=").append(getUserSession.getInt(getUserSession.getColumnIndex("id_balance")));

                final ListView listView = view.findViewById(R.id.loadPurchaseSaldo);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Mohon tunggu sebentar");
                progressDialog.setCancelable(false);
                progressDialog.setMax(100);
                progressDialog.show();

                final Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        new GetBalanceOrder(stringPurchaseSaldo, storageController, listView).execute();
                        timer.cancel();
                        progressDialog.dismiss();
                    }
                }, 2000, 4000);

                Button newBalanace = view.findViewById(R.id.newBalance);
                newBalanace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        assert inflater != null;
                        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.new_balance_fragment, null);

                        final EditText new_balance_nominal = layout.findViewById(R.id.new_balance_nominal);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(layout);
                        builder.setTitle("Beli Dompet");
                        builder.setPositiveButton(getActivity().getResources().getString(R.string.quick_access_new_balance), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {final StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("action").append("=").append("insertPesanSaldoAPI").append("&");
                                stringBuilder.append("purchase").append("=").append(new_balance_nominal.getText()).append("&");
                                stringBuilder.append("idSaldo").append("=").append(getUserSession.getInt(getUserSession.getColumnIndex("id_balance")));
                                new NewBalanceOrder(stringBuilder).execute();
                                reloadData(stringPurchaseSaldo, storageController, listView);
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class NewBalanceOrder extends AsyncTask<Void, Void, Boolean>{
        private StringBuilder packageBalanceOrderToPOST;
        private ProgressDialog progressDialog;

        NewBalanceOrder(StringBuilder packageBalanceOrderToPOST) {
            this.packageBalanceOrderToPOST = packageBalanceOrderToPOST;
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
                URL targetUrl = new URL("http://counterpulsa.xyz/try/base/php/saldo.php");
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(this.packageBalanceOrderToPOST.toString());
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
                Toast.makeText(getActivity(), "Berhasil melakukan pembelian", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetBalanceOrder extends AsyncTask<Void, Void, Boolean>{
        private StringBuilder packageBalanceOrderToPOST;
        private StorageController storageController;
        private ListView listView;

        GetBalanceOrder(StringBuilder packageBalanceOrderToPOST, StorageController storageController, ListView listView) {
            this.packageBalanceOrderToPOST = packageBalanceOrderToPOST;
            this.storageController = storageController;
            this.listView = listView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL targetUrl = new URL("http://counterpulsa.xyz/try/base/php/saldo.php");
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(this.packageBalanceOrderToPOST.toString());
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
                        storageController.removePurchaseBalance();

                        JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                        for (int i = 0; i < jsonArray.length(); i++){
                            ArrayList<String> columnName = new ArrayList<>();
                            columnName.add("id_purchase_balance");
                            columnName.add("nominal_purchase");
                            columnName.add("balance_price");
                            columnName.add("date");
                            columnName.add("time");
                            columnName.add("proof_of_payment");
                            columnName.add("status");

                            ArrayList<String> rowValues = new ArrayList<>();
                            rowValues.add(Integer.toString(jsonArray.getJSONObject(i).getInt("id_pesan_saldo")));
                            rowValues.add(Integer.toString(jsonArray.getJSONObject(i).getInt("nominal_saldo")));
                            rowValues.add(Integer.toString(jsonArray.getJSONObject(i).getInt("harga_saldo")));
                            rowValues.add(jsonArray.getJSONObject(i).getString("tanggal"));
                            rowValues.add(jsonArray.getJSONObject(i).getString("waktu"));
                            rowValues.add(jsonArray.getJSONObject(i).getString("bukti_pembayaran"));
                            rowValues.add(Integer.toString(jsonArray.getJSONObject(i).getInt("status_pembelian")));

                            storageController.newPurchaseBalance(columnName, rowValues);
                        }
                        return true;
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
                PurchaseBalanceAdapter adapter = new PurchaseBalanceAdapter(getActivity());
                listView.setAdapter(adapter);
            }
        }
    }
}
