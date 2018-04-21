package org.d3ifcool.ppob_counterpulsa.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class StorageModel extends SQLiteOpenHelper {
    private class Storage{
        private String tableName;
        private String[] columNameList;

        Storage(String tableName, String[] columNameList) {
            this.tableName = tableName;
            this.columNameList = columNameList;
        }

        String getTableName() {
            return tableName;
        }

        String[] getColumNameList() {
            return columNameList;
        }
    }

    private static final String DATABASE_NAME = "d3ifcoolcounterpulsa";
    private ArrayList<Storage> tableList;

    public StorageModel(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.tableList = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        this.tableList.add(new Storage(
                "session_user",
                new String[]{
                        "id_user integer",
                        "identity_number text",
                        "full_name text",
                        "email text",
                        "phone_number text",
                        "address text",
                        "password text",
                        "id_balance integer",
                        "current_balance integer",
                        "out_balance integer"
                }
        ));

        this.tableList.add(new Storage(
                "purchase_balance",
                new String[]{
                        "id_purchase_balance integer",
                        "nominal_purchase integer",
                        "balance_price integer",
                        "date text",
                        "time text",
                        "proof_of_payment text",
                        "status integer"
                }
        ));

        this.tableList.add(new Storage(
                "service",
                new String[]{
                        "id_service integer",
                        "service_name text",
                        "service_category text",
                        "service_price integer",
                        "service_status integer"
                }
        ));

        this.tableList.add(new Storage(
                "transaction_user",
                new String[]{
                        "id_transaction integer",
                        "id_service integer",
                        "phone_number text",
                        "date text",
                        "time text",
                        "transaction_status integer"
                }
        ));

        this.createTableList(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        this.removeTableList(sqLiteDatabase);

        onCreate(sqLiteDatabase);
    }

    private void createTableList(SQLiteDatabase sqLiteDatabase){
        for (Storage currentTable: this.tableList){
            StringBuilder columnList = new StringBuilder();
            int i;
            for(i = 0; i < currentTable.getColumNameList().length - 1; i++){
                columnList.append(currentTable.getColumNameList()[i]).append(", ");
            }
            columnList.append(currentTable.getColumNameList()[i]);
            sqLiteDatabase.execSQL("create table " + currentTable.getTableName() + " (" + columnList + ")");
        }
    }

    private void removeTableList(SQLiteDatabase sqLiteDatabase){
        for (Storage currentTable: this.tableList){
            sqLiteDatabase.execSQL("drop table if exists " + currentTable.getTableName());
        }
    }

    public boolean insertToDB(String tableName, ArrayList<String> columnName, ArrayList<String> rowValue){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < columnName.size() && i < rowValue.size(); i++){
            contentValues.put(columnName.get(i), rowValue.get(i));
        }
        return sqLiteDatabase.insert(tableName, null, contentValues) > 0;
    }

    public Cursor getFromDB(String sql){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql, null);
    }

    public boolean deleteFromDB(String tableName, String whereClause, String[] whereClauseArgs){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(tableName, whereClause, whereClauseArgs) <= 0;
    }

    public boolean updateFromDB(String tableName, ArrayList<String> columnName, ArrayList<String> rowValue, String whereClause, String[] whereClauseArgs){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < columnName.size() && i < rowValue.size(); i++){
            contentValues.put(columnName.get(i), rowValue.get(i));
        }
        return sqLiteDatabase.delete(tableName, whereClause, whereClauseArgs) <= 0;
    }
}
