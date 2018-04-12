package org.d3ifcool.counterpulsa;

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

import org.d3ifcool.counterpulsa.controller.APIController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistrationFragment extends Fragment {
    private EditText
        registration_identitiy_number,
        registration_full_name,
        registration_email,
        registration_phone_number,
        registration_address;
    private APIController apiController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.apiController = new APIController(getActivity());
        this.registration_identitiy_number = view.findViewById(R.id.registration_identity_number);
        this.registration_full_name = view.findViewById(R.id.registration_full_name);
        this.registration_email = view.findViewById(R.id.registration_email);
        this.registration_phone_number = view.findViewById(R.id.registration_phone_number);
        this.registration_address = view.findViewById(R.id.registration_address);

        Button registration_button = view.findViewById(R.id.registration_button);

        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation() == null){
                    ArrayList<String> key = new ArrayList<>();
                    key.add("identityNumber");
                    key.add("fullName");
                    key.add("email");
                    key.add("phoneNumber");
                    key.add("address");

                    ArrayList<String> value = new ArrayList<>();
                    value.add(registration_identitiy_number.getText().toString());
                    value.add(registration_full_name.getText().toString());
                    value.add(registration_email.getText().toString());
                    value.add(registration_phone_number.getText().toString());
                    value.add(registration_address.getText().toString());

                    String response = apiController.registration(key, value);
                    System.out.println(response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        if (response == null || !jsonObject.getString("status").equals("200")){
//                            Toast.makeText(getActivity(), "Gagal melakukan pendaftaran", Toast.LENGTH_SHORT);
//                        }
//                        else{
//                            Toast.makeText(getActivity(), "Berhasi, cek email anda", Toast.LENGTH_SHORT);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
                else
                    Toast.makeText(getActivity(), checkValidation(), Toast.LENGTH_SHORT).show();
            }
        });
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
