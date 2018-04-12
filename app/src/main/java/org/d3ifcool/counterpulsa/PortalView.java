package org.d3ifcool.counterpulsa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PortalView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_view);

        this.runLoginFragment();
    }

    private void runLoginFragment(){
        Fragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.portalFrame, loginFragment, "loginFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private boolean checkAlreadyLoginFragment(){
        Fragment loginFragment = getSupportFragmentManager().findFragmentByTag("loginFragment");
        return loginFragment != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!checkAlreadyLoginFragment()){
            moveTaskToBack(true);
        }
    }
}