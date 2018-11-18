package csc472.depaul.edu.finalproject.models;

import java.util.List;

import csc472.depaul.edu.finalproject.db.ReceiptDate;

public interface ILoadDataObserver {
    public void loadData();
    public void loadData(List<ReceiptDate> receiptDates);
}
      