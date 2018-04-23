package org.d3ifcool.ppob_counterpulsa.controller;

import android.app.Activity;
import android.database.Cursor;

import org.d3ifcool.ppob_counterpulsa.model.StorageModel;

import java.util.ArrayList;

public class StorageController {
    private StorageModel storageModel;
    private Activity activity;

    public StorageController(Activity activity) {
        this.activity = activity;
    }

    private void startDB(){
        this.storageModel = new StorageModel(this.activity);
    }

    private void closeDB(){
        this.storageModel.close();
    }

    // START USER SESSION

    public void removeUserSession(){
        this.startDB();
        this.storageModel.deleteFromDB("session_user", null, null);
        this.closeDB();
    }

    public boolean setUserSession(ArrayList<String> columnName, ArrayList<String> rowValue){
        this.startDB();
        boolean status = this.storageModel.insertToDB("session_user", columnName, rowValue);
        this.closeDB();
        return status;
    }

    public boolean checkUserSession(){
        this.startDB();
        Cursor getUserSession = this.storageModel.getFromDB("select * from session_user");
        if (getUserSession.getCount() != 0)
            return true;
        this.closeDB();
        return false;
    }

    public Cursor getUserSession(){
        this.startDB();
        Cursor getUserSession = this.storageModel.getFromDB("select * from session_user");
        if (getUserSession.getCount() != 0)
            return getUserSession;
        this.closeDB();
        return null;
    }

    public boolean updateUserSession(ArrayList<String> columnName, ArrayList<String> rowValue, String idUser){
        this.startDB();
        boolean status = this.storageModel.updateFromDB("session_user", columnName, rowValue, "id_user=?", new String[]{idUser});
        this.closeDB();
        return status;
    }

    // CLOSE USER SESSION
    // START BALANCE SESSION

    public boolean newPurchaseBalance(ArrayList<String> columnName, ArrayList<String> rowsValue){
        this.startDB();
        boolean status = this.storageModel.insertToDB("purchase_balance", columnName, rowsValue);
        this.closeDB();
        return status;
    }

    public Cursor getPurchaseBalance(){
        this.startDB();
        Cursor cursor = this.storageModel.getFromDB("select * from purchase_balance order by id_purchase_balance desc");
        if (cursor.getCount() != 0)
            return cursor;
        this.closeDB();
        return null;
    }

    public void removePurchaseBalance(){
        this.startDB();
        this.storageModel.deleteFromDB("purchase_balance", null, null);
        this.closeDB();
    }

    // STOP BALANCE SESSION
    // START SERVICE SESSION

    public Cursor getService(){
        this.startDB();
        Cursor service = this.storageModel.getFromDB("select * from service");
        if (service.getCount() != 0)
            return service;
        this.closeDB();
        return null;
    }

    public boolean insertService(ArrayList<String> columnName, ArrayList<String> rowsValue){
        this.startDB();
        boolean status = this.storageModel.insertToDB("service", columnName, rowsValue);
        this.closeDB();
        return status;
    }

    // STOP SERVICE SESSION
    // START TRANSACTION SESSION

    public Cursor getTransaction(){
        this.startDB();
        Cursor service = this.storageModel.getFromDB("select * from transaction_user");
        if (service.getCount() != 0)
            return service;
        this.closeDB();
        return null;
    }

    public boolean insertTransaction(ArrayList<String> columnName, ArrayList<String> rowsValue){
        this.startDB();
        boolean status = this.storageModel.insertToDB("transaction_user", columnName, rowsValue);
        this.closeDB();
        return status;
    }

    // STOP TRANSACTION SESSION
    // START ANNOUNCEMENT SESSION

    public boolean insertAnnouncement(ArrayList<String> columnName, ArrayList<String> rowsValue){
        this.startDB();
        boolean status = this.storageModel.insertToDB("announcement", columnName, rowsValue);
        this.closeDB();
        return status;
    }

    public Cursor getAnnouncement(){
        this.startDB();
        Cursor service = this.storageModel.getFromDB("select * from announcement");
        if (service.getCount() != 0)
            return service;
        this.closeDB();
        return null;
    }

    // STOP ANNOUNCEMENT SESSION
}
