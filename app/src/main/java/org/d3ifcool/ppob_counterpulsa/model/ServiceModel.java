package org.d3ifcool.ppob_counterpulsa.model;

/**
 * Created by Andy on 4/23/2018.
 */

public class ServiceModel {
    private int id_service, service_price, service_status;
    private String service_name, service_category;

    public ServiceModel(int id_service, int service_price, int service_status, String service_name, String service_category) {
        this.id_service = id_service;
        this.service_price = service_price;
        this.service_status = service_status;
        this.service_name = service_name;
        this.service_category = service_category;
    }

    public int getId_service() {
        return id_service;
    }

    public int getService_price() {
        return service_price;
    }

    public int getService_status() {
        return service_status;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_category() {
        return service_category;
    }
}
