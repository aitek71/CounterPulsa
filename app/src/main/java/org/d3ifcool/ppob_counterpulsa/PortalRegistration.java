package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class PortalRegistration extends Fragment implements View.OnClickListener{
    private EditText
            registration_identitiy_number,
            registration_full_name,
            registration_email,
            registration_phone_number,
            registration_address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.portal_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.registration_identitiy_number = view.findViewById(R.id.registration_identity_number);
        this.registration_full_name = view.findViewById(R.id.registration_full_name);
        this.registration_email = view.findViewById(R.id.registration_email);
        this.registration_phone_number = view.findViewById(R.id.registration_phone_number);
        this.registration_address = view.findViewById(R.id.registration_address);

        Button registration_button = view.findViewById(R.id.registration_button);

        registration_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registration_button:
                if (checkValidation() == null) {
                    StringBuilder packageRegistrationToPOST;
                    packageRegistrationToPOST = new StringBuilder();
                    packageRegistrationToPOST.append("action").append("=").append("registrationAPI").append("&");
                    packageRegistrationToPOST.append("identityNumber").append("=").append(registration_identitiy_number.getText().toString()).append("&");
                    packageRegistrationToPOST.append("fullName").append("=").append(registration_full_name.getText().toString()).append("&");
                    packageRegistrationToPOST.append("email").append("=").append(registration_email.getText().toString()).append("&");
                    packageRegistrationToPOST.append("phoneNumber").append("=").append(registration_phone_number.getText().toString()).append("&");
                    packageRegistrationToPOST.append("address").append("=").append(registration_address.getText().toString());
                    new RegistrationToServer(packageRegistrationToPOST).execute();
                }
                else
                    Toast.makeText(getActivity(), checkValidation(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RegistrationToServer extends AsyncTask<Void, Void, Boolean>{
        private StringBuilder packageRegistrationToPOST;
        private ProgressDialog progressDialog;

        RegistrationToServer(StringBuilder packageRegistrationToPOST) {
            this.packageRegistrationToPOST = packageRegistrationToPOST;
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
                dataOutputStream.writeBytes(this.packageRegistrationToPOST.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line).append('\n');

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                if(jsonObject.getString("status").equals("200"))
                    return true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
            if (!aVoid)
                Toast.makeText(getActivity(), "Gagal melakukan registrasi", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(getActivity(), "Pendaftaran berhasil dikirm, cek alamat elektronik anda.", Toast.LENGTH_SHORT).show();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(PortalView.TAG_PORTAL_REGISTRATION);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
            progressDialog.dismiss();
        }
    }

    private String checkValidation(){
        String callBackString;
        if (TextUtils.isEmpty(registration_identitiy_number.getText().toString()))
            callBackString = "Harap isi nomor identitas";
        else if(TextUtils.isEmpty(registration_full_name.getText().toString()))
            callBackString = "Harap isi nama lengkap";
        else if(TextUtils.isEmpty(registration_email.getText().toString()))
            callBackString = "Harap isi alamat elektronik";
        else if (TextUtils.isEmpty(registration_phone_number.getText().toString()))
            callBackString = "Harap isi nomor telepon";
        else if (TextUtils.isEmpty(registration_address.getText().toString()))
            callBackString = "Harap isi alamat";
        else if (registration_identitiy_number.getText().toString().length() != 16)
            callBackString = "Nomor identitas tidak benar";
        else if (!checkValidEmail())
            callBackString = "Alamat elektronik tidak benar";
        else if (registration_phone_number.getText().toString().length() < 11 || registration_phone_number.getText().toString().length() > 12)
            callBackString = "Nomor telepon tidak benar";
        else
            callBackString = null;
        return callBackString;
    }

    private boolean checkValidEmail(){
        boolean status = false;
        for (int i = 0; i < registration_email.getText().toString().length(); i++){
            if (registration_email.getText().toString().charAt(i) == '@')
                status = true;
        }
        return status;
    }
}
