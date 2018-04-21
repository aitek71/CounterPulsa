package org.d3ifcool.ppob_counterpulsa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.d3ifcool.ppob_counterpulsa.model.MainMenuModel;
import org.d3ifcool.ppob_counterpulsa.model.MainSliderModel;

import java.util.ArrayList;

public class FragmentMain extends Fragment implements AdapterView.OnItemClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager mainSlider = view.findViewById(R.id.mainSlider);
        ArrayList<MainSliderModel> item_list_slider = new ArrayList<>();

        item_list_slider.add(new MainSliderModel("3 Langkah Mudah Memulai", "Terdapat 3 langkah untuk memulai bersama counter pulsa."));
        item_list_slider.add(new MainSliderModel("Daftar Agen Pulsa", "Pendaftaran agen pulsa murah counter pulsa gratis, mudah."));
        item_list_slider.add(new MainSliderModel("Deposit Saldo", "Deposit saldo 24 jam online Minimal deposit hanya 10rb."));
        item_list_slider.add(new MainSliderModel("Transaksi Pulsa", "Setelah saldo terisi, langsung bisa transaksi."));

        MainSliderAdapter mainSliderAdapter = new MainSliderAdapter(getActivity(), item_list_slider);
        mainSlider.setAdapter(mainSliderAdapter);

        GridView mainMenu = view.findViewById(R.id.mainMenu);
        ArrayList<MainMenuModel> item_list_menu = new ArrayList<>();

        item_list_menu.add(new MainMenuModel(getResources().getString(R.string.item_balance), R.mipmap.menu_purchase, R.drawable.item_menu_purchase));
        item_list_menu.add(new MainMenuModel(getResources().getString(R.string.item_product), R.mipmap.menu_service, R.drawable.item_menu_service));
        item_list_menu.add(new MainMenuModel(getResources().getString(R.string.item_transaction), R.mipmap.menu_transaction, R.drawable.item_menu_transaction));
        item_list_menu.add(new MainMenuModel(getResources().getString(R.string.item_recap), R.mipmap.menu_recap, R.drawable.item_menu_recap));
        item_list_menu.add(new MainMenuModel(getResources().getString(R.string.item_home), R.mipmap.menu_announcement, R.drawable.item_menu_announcement));
        item_list_menu.add(new MainMenuModel(getResources().getString(R.string.item_user), R.mipmap.menu_user, R.drawable.item_menu_user));

        MainMenuAdapter adapter = new MainMenuAdapter(getActivity(), item_list_menu);
        mainMenu.setAdapter(adapter);
        mainMenu.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Fragment fragment = null;
        switch (i){
            case 0:
                fragment = new FragmentBalance();
                break;
            case 1:
//                fragment = new FragmentService();
                break;
            case 2:
//                fragment = new FragmentTransactions();
                break;
            case 3:
//                fragment = new FragmentRecap();
                break;
            case 4:
//                fragment = new FragmentAnnouncement();
                break;
            default:
//                fragment = new FragmentUser();
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loadFrame, fragment).commit();
    }
}
