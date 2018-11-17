package csc472.depaul.edu.finalproject.db;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountViewModel extends AndroidViewModel {

    private DaoAccess accountDao;
    private ExecutorService executorService;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountDao = AccountDatabase.getAccountDatabase(application).daoAccess();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Account>> getAllAccounts() {
        return accountDao.findAll();
    }

    public void saveAccount(Account account) {
        executorService.execute(() -> accountDao.insertOnlySingleAccount(account));
    }

    public void deleteAccount(Account account) {
        executorService.execute(() -> accountDao.deleteAccount(account));
    }
}