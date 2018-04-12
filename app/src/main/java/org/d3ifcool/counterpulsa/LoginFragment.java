package org.d3ifcool.counterpulsa;

import android.annotation.SuppressLint;
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

public class LoginFragment extends Fragment {
    private EditText login_phone_number, login_password;
    private boolean status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.login_phone_number = view.findViewById(R.id.login_phone_number);
        this.login_password = view.findViewById(R.id.login_password);

        Button login_button = view.findViewById(R.id.login_button);
        Button registration_button = view.findViewById(R.id.registration_button);

        status = true;

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

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation() == null){

                }
                else
                    Toast.makeText(getActivity(), checkValidation(), Toast.LENGTH_SHORT).show();
            }
        });

        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runRegistrationFragment();
            }
        });
    }

    private String checkValidation(){
        String callBackString;
        if (TextUtils.isEmpty(login_phone_number.getText().toString()))
            callBackString = "Harap isi nomor telepon";
        else if (TextUtils.isEmpty(login_password.getText().toString()))
            callBackString = "Harap isi kata sandi";
        else if(login_phone_number.getText().toString().length() >= 11 || login_phone_number.getText().toString().length() <= 12)
            callBackString = "Nomor telepon tidak valid";
        else
            callBackString = null;
        return callBackString;
    }

    private void runRegistrationFragment(){
        Fragment registrationFragment = new RegistrationFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.portalFrame, registrationFragment, "registrationFragment");
        fragmentTransaction.addToBackStack("loginFragment");
        fragmentTransaction.commitAllowingStateLoss();
    }
}
