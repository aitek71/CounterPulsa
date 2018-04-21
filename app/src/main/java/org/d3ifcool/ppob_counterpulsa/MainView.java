package org.d3ifcool.ppob_counterpulsa;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import org.d3ifcool.ppob_counterpulsa.controller.StorageController;
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

public class MainView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        Toolbar toolbar = findViewById(R.id.mainBar);
        setSupportActionBar(toolbar);

        StorageController storageController = new StorageController(this);
        Cursor getUserIdentity = storageController.getUserSession();
        int idUser = 0;
        if (getUserIdentity.moveToFirst()){
            idUser = getUserIdentity.getInt(getUserIdentity.getColumnIndex("id_user"));
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("action").append("=").append("reloadUserAPI").append("&");
        stringBuilder.append("id").append("=").append(String.valueOf(idUser));

        new UpdateUser(stringBuilder, storageController).execute();

        Fragment mainFragment = new FragmentMain();
        getSupportFragmentManager().beginTransaction().replace(R.id.loadFrame, mainFragment, "MainFragment").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainView.class);
                startActivity(intent);
                finish();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateUser extends AsyncTask<Void, Void, Boolean> {
        private StringBuilder packageUserToPOST;
        private StorageController storageController;

        UpdateUser(StringBuilder packageUserToPOST, StorageController storageController) {
            this.packageUserToPOST = packageUserToPOST;
            this.storageController = storageController;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                URL targetUrl = new URL("http://counterpulsa.xyz/try/base/php/user.php");
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(this.packageUserToPOST.toString());
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
                        System.out.println(stringBuilder.toString());

                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                        ArrayList<String> columnName = new ArrayList<>();
                        columnName.add("id_user"); columnName.add("identity_number");
                        columnName.add("full_name"); columnName.add("email");
                        columnName.add("phone_number"); columnName.add("address");
                        columnName.add("password"); columnName.add("id_balance");
                        columnName.add("current_balance");columnName.add("out_balance");

                        ArrayList<String> rowValues = new ArrayList<>();
                        rowValues.add(Integer.toString(jsonObject.getInt("id_user")));
                        rowValues.add(jsonObject.getString("no_ktp"));
                        rowValues.add(jsonObject.getString("nama_lengkap"));
                        rowValues.add(jsonObject.getString("email"));
                        rowValues.add(jsonObject.getString("no_hp"));
                        rowValues.add(jsonObject.getString("alamat"));
                        rowValues.add(jsonObject.getString("password"));
                        rowValues.add(Integer.toString(jsonObject.getInt("id_saldo")));
                        rowValues.add(Integer.toString(jsonObject.getInt("current_saldo")));
                        rowValues.add(Integer.toString(jsonObject.getInt("saldo_out")));

                        this.storageController.removeUserSession();
                        return this.storageController.setUserSession(columnName, rowValues);

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
    }
}
