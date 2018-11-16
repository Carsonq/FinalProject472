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

//    void saveA(Account account) {
//        executorService.execute(() -> postDao.save(post));
//    }
//
//    void deletePost(Post post) {
//        executorService.execute(() -> postDao.delete(post));
//    }
}