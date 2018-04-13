package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class PortalLogin extends Fragment implements View.OnClickListener {
    private EditText login_phone_number, login_password;
    private boolean status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.portal_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.login_password = view.findViewById(R.id.login_password);
        this.login_phone_number = view.findViewById(R.id.login_phone_number);
        Button login_button = view.findViewById(R.id.login_button);
        Button registration_button = view.findViewById(R.id.registration_button);

        this.status = true;

        login_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (motionEvent.getX() >= (login_password.getRight() - login_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        if (status == true) {
                            login_password.setTransformationMethod(null);
                            status = false;
                        }
                        else{
                            login_password.setTransformationMethod(new PasswordTransformationMethod());
                            status = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        login_button.setOnClickListener(this);
        registration_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                if (TextUtils.isEmpty(checkValidation())){
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("action").append("=").append("loginAPI").append("&");
                    stringBuilder.append("phoneNumber").append("=").append(login_phone_number.getText().toString()).append("&");
                    stringBuilder.append("passWord").append("=").append(login_password.getText().toString());

                    new LoginToServer(stringBuilder).execute();
                }
                else
                    Toast.makeText(getActivity(), checkValidation(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.registration_button:
                Fragment fragment = new PortalRegistration();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.portalFrame, fragment, PortalView.TAG_PORTAL_REGISTRATION);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginToServer extends AsyncTask<Void, Void, Boolean> {
        private StringBuilder packageLoginToPOST;
        private ProgressDialog progressDialog;

        LoginToServer(StringBuilder packageLoginToPOST) {
            this.packageLoginToPOST = packageLoginToPOST;
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

                URL targetUrl = new URL("http://counterpulsa.xyz/try/base/php/portal.php");
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(this.packageLoginToPOST.toString());
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

                        return new StorageController(getActivity()).setUserSession(columnName, rowValues);

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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (!aVoid) {
                Toast.makeText(getActivity(), "Gagal anda belum terdaftar", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "Berhasil masuk.", Toast.LENGTH_SHORT).show();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(PortalView.TAG_PORTAL_LOGIN);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
            progressDialog.dismiss();
        }
    }

    private String checkValidation(){
        StringBuilder callBackString = new StringBuilder();
        if (TextUtils.isEmpty(login_phone_number.getText().toString()))
            callBackString.append("Harap isi nomor telepon");
        else if (TextUtils.isEmpty(login_password.getText().toString()))
            callBackString.append("Harap isi kata sandi");
        else if(login_phone_number.getText().toString().length() <= 11 || login_phone_number.getText().toString().length() > 12)
            callBackString.append("Nomor telepon tidak valid");
        return callBackString.toString();
    }
}