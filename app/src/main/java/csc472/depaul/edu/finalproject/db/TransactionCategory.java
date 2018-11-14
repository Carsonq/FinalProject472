package csc472.depaul.edu.finalproject.db;


import android.arch.persistence.room.ColumnInfo;

public class TransactionCategory {
    @ColumnInfo(name = "transaction_category")
    private String transactionCategory;
    @ColumnInfo(name = "category_amount")
    private double categoryAmount;

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public double getCategoryAmount() {
        return categoryAmount;
    }

    public void setCategoryAmount(double categoryAmount) {
        this.categoryAmount = categoryAmount;
    }
}