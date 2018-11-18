package csc472.depaul.edu.finalproject.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"receipt_token"}, unique = true)})
public class Receipt {
    @PrimaryKey(autoGenerate = true)
    private int receiptId;
    @ColumnInfo(name = "receipt_total")
    private double receiptTotal;
    @ColumnInfo(name = "receipt_subtotal")
    private double receiptSubtotal;
    @ColumnInfo(name = "receipt_tax")
    private double receiptTax;
    @ColumnInfo(name = "receipt_date")
    private String receiptDate;
    @ColumnInfo(name = "receipt_token")
    private String receiptToken;

    public Receipt() {
    }

    public int getReceiptId() {
        return receiptId;
    }

    public double getReceiptTotal() {
        return receiptTotal;
    }

    public double getReceiptSubtotal() {
        return receiptSubtotal;
    }

    public double getReceiptTax() {
        return receiptTax;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public String getReceiptToken() {
        return receiptToken;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public void setReceiptTotal(double receiptTotal) {
        this.receiptTotal = receiptTotal;
    }

    public void setReceiptSubtotal(double receiptSubtotal) {
        this.receiptSubtotal = receiptSubtotal;
    }

    public void setReceiptTax(double receiptTax) {
        this.receiptTax = receiptTax;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setReceiptToken(String receiptToken) {
        this.receiptToken = receiptToken;
    }
}
