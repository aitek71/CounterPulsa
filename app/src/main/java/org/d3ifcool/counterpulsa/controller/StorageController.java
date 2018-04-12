package org.d3ifcool.counterpulsa.controller;

import android.app.Activity;

import org.d3ifcool.counterpulsa.model.StorageModel;

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
}
