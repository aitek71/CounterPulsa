package org.d3ifcool.ppob_counterpulsa.model;

/**
 * Created by Andy on 4/20/2018.
 */

public class MainSliderModel {
    private String title_item_slider, description_item_slider;

    public MainSliderModel(String title_item_slider, String description_item_slider) {
        this.title_item_slider = title_item_slider;
        this.description_item_slider = description_item_slider;
    }

    public String getTitle_item_slider() {
        return title_item_slider;
    }

    public String getDescription_item_slider() {
        return description_item_slider;
    }
}
