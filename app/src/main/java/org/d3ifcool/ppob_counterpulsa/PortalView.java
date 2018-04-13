package org.d3ifcool.ppob_counterpulsa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PortalView extends AppCompatActivity {

    public static final String TAG_PORTAL_LOGIN = "portalLogin";
    public static final String TAG_PORTAL_REGISTRATION = "portalRegistration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_view);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = new PortalLogin();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.portalFrame, fragment, TAG_PORTAL_LOGIN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (checkLastBackStack())
            moveTaskToBack(true);
    }

    private boolean checkLastBackStack(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_PORTAL_REGISTRATION);
        return fragment == null;
    }
}
