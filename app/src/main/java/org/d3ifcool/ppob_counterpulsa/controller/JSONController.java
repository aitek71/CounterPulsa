package org.d3ifcool.ppob_counterpulsa.controller;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class JSONController {

    private String jsonFileName;
    private Activity activity;

    public JSONController(Activity activity, String jsonFileName) {
        this.jsonFileName = jsonFileName;
        this.activity = activity;
    }

    public void createJSONFile(String data){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.activity.openFileOutput(jsonFileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readJSONFile(){
        if (jsonFileName != null){
            try {
                InputStream inputStream = this.activity.openFileInput(jsonFileName);
                if(inputStream != null){
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((receiveString = bufferedReader.readLine()) != null){
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    return stringBuilder.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
