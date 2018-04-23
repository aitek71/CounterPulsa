package org.d3ifcool.ppob_counterpulsa.model;

/**
 * Created by Andy on 4/23/2018.
 */

public class AnnouncemenetModel {
    private int id_announcement;
    private String title, description, date, time, image_resource;

    public AnnouncemenetModel(int id_announcement, String title, String description, String date, String time, String image_resource) {
        this.id_announcement = id_announcement;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.image_resource = image_resource;
    }

    public int getId_announcement() {
        return id_announcement;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImage_resource() {
        return image_resource;
    }
}
