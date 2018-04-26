package org.d3ifcool.ppob_counterpulsa.model;

/**
 * Created by Andy on 4/19/2018.
 */

public class MainMenuModel {
    private String title_item_main_menu;
    private int icon_item_main_menu;
    private int background_item_main_menu;

    public MainMenuModel(String title_item_main_menu, int icon_item_main_menu, int background_item_main_menu) {
        this.title_item_main_menu = title_item_main_menu;
        this.icon_item_main_menu = icon_item_main_menu;
        this.background_item_main_menu = background_item_main_menu;
    }

    public String getTitle_item_main_menu() {
        return title_item_main_menu;
    }

    public int getIcon_item_main_menu() {
        return icon_item_main_menu;
    }

    public int getBackground_item_main_menu() {
        return background_item_main_menu;
    }
}
