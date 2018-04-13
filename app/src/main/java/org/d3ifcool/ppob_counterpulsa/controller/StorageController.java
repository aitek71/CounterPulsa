package org.d3ifcool.ppob_counterpulsa.controller;

import android.app.Activity;

import org.d3ifcool.ppob_counterpulsa.model.StorageModel;

import java.util.ArrayList;

public class StorageController {
    private StorageModel storageModel;
    private Activity activity;

    public StorageController(Activity activity) {
        this.activity = activity;
    }

    public void startDB(){
        this.storageModel = new StorageModel(this.activity);
    }

    public void closeDB(){
        this.storageModel.close();
    }

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
}
