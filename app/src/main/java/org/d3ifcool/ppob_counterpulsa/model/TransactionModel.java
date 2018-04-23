package org.d3ifcool.ppob_counterpulsa.model;

/**
 * Created by Andy on 4/23/2018.
 */

public class TransactionModel {
    private int id_transaction, id_service, service_price, transaction_status;
    private String phone_number, date, time, service_name, service_category;

    public TransactionModel(int id_transaction, int id_service, int service_price, int transaction_status, String phone_number, String date, String time, String service_name, String service_category) {
        this.id_transaction = id_transaction;
        this.id_service = id_service;
        this.service_price = service_price;
        this.transaction_status = transaction_status;
        this.phone_number = phone_number;
        this.date = date;
        this.time = time;
        this.service_name = service_name;
        this.service_category = service_category;
    }

    public int getId_transaction() {
        return id_transaction;
    }

    public int getId_service() {
        return id_service;
    }

    public int getService_price() {
        return service_price;
    }

    public int getTransaction_status() {
        return transaction_status;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_category() {
        return service_category;
    }
}
