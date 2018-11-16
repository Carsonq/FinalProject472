package csc472.depaul.edu.finalproject.db;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionViewModel extends AndroidViewModel {

    private DaoTransaction transactionDao;
    private ExecutorService executorService;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        transactionDao = TransactionDatabase.getTransactionDatabase(application).daoTransaction();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionDao.findAll();
    }
}