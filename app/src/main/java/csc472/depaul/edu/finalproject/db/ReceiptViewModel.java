package csc472.depaul.edu.finalproject.db;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiptViewModel extends AndroidViewModel {

    private DaoReceipt receiptDao;
    private ExecutorService executorService;

    public ReceiptViewModel(@NonNull Application application) {
        super(application);
        receiptDao = ReceiptDatabase.getReceiptDatabase(application).daoReceipt();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Receipt>> getReceipts(String minDate, String maxDate) {
        return receiptDao.fetchReceipts(minDate, maxDate);
    }

    public void saveReceipt(Receipt receipt) {
        executorService.execute(() -> receiptDao.insertOne(receipt));
    }

}