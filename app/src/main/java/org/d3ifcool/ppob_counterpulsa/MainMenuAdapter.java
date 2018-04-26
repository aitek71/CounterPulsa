package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.d3ifcool.ppob_counterpulsa.model.MainMenuModel;

import java.util.ArrayList;

public class MainMenuAdapter extends BaseAdapter {
    private ArrayList<MainMenuModel> item_list;
    private LayoutInflater layoutInflater;

    public MainMenuAdapter(Activity activity, ArrayList<MainMenuModel> item_list) {
        this.item_list = item_list;
        this.layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item_list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.item_main_menu, null);

        ImageView icon_item_main_menu = view.findViewById(R.id.icon_item_main_menu);
        TextView title_item_main_menu = view.findViewById(R.id.title_item_main_menu);

        icon_item_main_menu.setImageResource(item_list.get(i).getIcon_item_main_menu());
        icon_item_main_menu.setBackgroundResource(item_list.get(i).getBackground_item_main_menu());
        title_item_main_menu.setText(item_list.get(i).getTitle_item_main_menu());

        return view;
    }
}
