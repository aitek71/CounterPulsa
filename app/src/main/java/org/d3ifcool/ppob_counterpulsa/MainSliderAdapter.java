package org.d3ifcool.ppob_counterpulsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.d3ifcool.ppob_counterpulsa.model.MainSliderModel;

import java.util.ArrayList;

public class MainSliderAdapter extends PagerAdapter{

    private LayoutInflater layoutInflater;
    private ArrayList<MainSliderModel> item_list;

    MainSliderAdapter(Activity activity, ArrayList<MainSliderModel> item_list) {
        this.layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item_list = item_list;
    }

    @Override
    public int getCount() {
        return item_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        @SuppressLint("InflateParams") View view = this.layoutInflater.inflate(R.layout.item_main_slider, null);
        TextView title_item_slider, description_item_slider;

        title_item_slider = view.findViewById(R.id.title_item_slider);
        description_item_slider = view.findViewById(R.id.description_item_slider);
        title_item_slider.setText(item_list.get(position).getTitle_item_slider());
        description_item_slider.setText(item_list.get(position).getDescription_item_slider());

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View)object;
        viewPager.removeView(view);
    }
}
