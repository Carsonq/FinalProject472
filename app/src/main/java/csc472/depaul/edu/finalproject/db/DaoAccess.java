package csc472.depaul.edu.finalproject.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOnlySingleAccount(Account account);

    @Insert
    void insertMultipleAccounts(List<Account> accountList);

    @Query("SELECT * FROM Account WHERE accountId =:accountId")
    List<Account> fetchOneAccountbyAccountId(int accountId);

    @Query("SELECT * FROM Account")
    List<Account> fetchAllAccounts();

    @Query("SELECT * FROM Account")
    LiveData<List<Account>> findAll();

    @Update
    void updateAccount(Account accounts);

    @Delete
    void deleteAccount(Account accounts);

}