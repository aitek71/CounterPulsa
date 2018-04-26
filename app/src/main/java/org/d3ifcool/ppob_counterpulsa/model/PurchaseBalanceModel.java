package org.d3ifcool.ppob_counterpulsa.model;

/**
 * Created by Andy on 4/20/2018.
 */

public class PurchaseBalanceModel {
    private int id_purchase_balance, nominal_purchase, balance_price, status;
    private String date, time, proof_of_payment;

    public PurchaseBalanceModel(int id_purchase_balance, int nominal_purchase, int balance_price, int status, String date, String time, String proof_of_payment) {
        this.id_purchase_balance = id_purchase_balance;
        this.nominal_purchase = nominal_purchase;
        this.balance_price = balance_price;
        this.status = status;
        this.date = date;
        this.time = time;
        this.proof_of_payment = proof_of_payment;
    }

    public int getId_purchase_balance() {
        return id_purchase_balance;
    }

    public int getNominal_purchase() {
        return nominal_purchase;
    }

    public int getBalance_price() {
        return balance_price;
    }

    public int getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getProof_of_payment() {
        return proof_of_payment;
    }
}
