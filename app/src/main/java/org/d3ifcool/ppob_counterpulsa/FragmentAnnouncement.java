package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

public class FragmentAnnouncement extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.announcement_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView loadAnnouncementItem = view.findViewById(R.id.loadAnnouncement);

    }

    @SuppressLint("StaticFieldLeak")
    private class loadAnnouncement extends AsyncTask<Void, Void, Boolean> {
        private StringBuilder packageAnnouncementOrderToPOST;
        private StorageController storageController;
        private ListView loadAnnouncementItem;
        private ProgressDialog progressDialog;

        loadAnnouncement(StringBuilder packageAnnouncementOrderToPOST, StorageController storageController, ListView loadAnnouncementItem) {
            this.packageAnnouncementOrderToPOST = packageAnnouncementOrderToPOST;
            this.storageController = storageController;
            this.loadAnnouncementItem = loadAnnouncementItem;
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
                URL targetUrl = new URL("http://counterpulsa.xyz/try/base/php/pengumuman.php");
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(this.packageAnnouncementOrderToPOST.toString());
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
                            columnName.add("id_announcement");
                            columnName.add("title");
                            columnName.add("description");
                            columnName.add("date");
                            columnName.add("time");
                            columnName.add("image_resource");

                            ArrayList<String> rowsValue = new ArrayList<>();
                            rowsValue.add(String.valueOf(jsonArray.getJSONObject(i).getInt("id_announcement")));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("title"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("description"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("date"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("time"));
                            rowsValue.add(jsonArray.getJSONObject(i).getString("image_resource"));

                            this.storageController.insertAnnouncement(columnName, rowsValue);
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
                loadAnnouncementItem.setAdapter(adapter);
            }
            progressDialog.dismiss();
        }
    }
}
