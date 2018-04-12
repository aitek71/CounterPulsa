package org.d3ifcool.counterpulsa.controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class APIController {
    public class HttpController extends AsyncTask<String, Void, String>{
        private final String TAG = HttpController.class.getSimpleName();
        private String url, parameters;

        public HttpController(String url, String parameters){
            this.url = url;
            this.parameters = parameters;
        }

        @Override
        protected String doInBackground(String... voids) {
            String response = "";
            try {
                URL targetURL = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) targetURL.openConnection();
                connection.setRequestMethod("POST");

                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

                dataOutputStream.writeBytes(parameters);
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                response = getDataAPI(inputStream);
            }
            catch (MalformedURLException e) {
                Log.e(TAG, "Kesalahan penulisan alamat tujuan : " + e.getMessage());
            }
            catch (ProtocolException e){
                Log.e(TAG, "Kesalahan penulisan protokol tujuan : " + e.getMessage());
            }
            catch (IOException e){
                Log.e(TAG, "Kesalahan penulisan input output : " + e.getMessage());
            }
            return response;
        }

        private String getDataAPI(InputStream inputStream){
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            try{
                while ((line = reader.readLine()) != null){
                    stringBuilder.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }
    }

    private Activity activity;
    private StorageController storageController;
    public APIController(Activity activity) {
        this.activity = activity;
        this.storageController = new StorageController(this.activity);
    }

    private boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public String registration(ArrayList<String> key, ArrayList<String> value){
        if (checkConnection()){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("action=registrationAPI&");
            int i = 0;
            for(i = 0; i < key.size()-1 && i < value.size()-1; i++)
                stringBuilder.append(key.get(i)).append("=").append(value.get(i)).append("&");
            stringBuilder.append(key.get(i)).append("=").append(value.get(i));

            HttpController apiRegistration = new HttpController("http://counterpulsa.xyz/try/base/php/portal.php", stringBuilder.toString());
            apiRegistration.execute("");
        }
        return "1";
    }
}
