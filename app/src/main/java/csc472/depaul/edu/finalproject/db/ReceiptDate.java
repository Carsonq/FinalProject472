package csc472.depaul.edu.finalproject.db;

import android.arch.persistence.room.ColumnInfo;

public class ReceiptDate {
    @ColumnInfo(name = "receipt_date")
    private String receiptDate;
    @ColumnInfo(name = "day_total")
    private double dayTotal;

    public String getReceiptDate() {
        return receiptDate;
    }

    public double getDayTotal() {
        return dayTotal;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setDayTotal(double dayTotal) {
        this.dayTotal = dayTotal;
    }
}