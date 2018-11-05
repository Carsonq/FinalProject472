package csc472.depaul.edu.finalproject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleAccount(Account accounts);

    @Insert
    void insertMultipleAccounts(List<Account> accountList);

    @Query("SELECT * FROM Account WHERE accountId =:accountId")
    Account fetchOneAccountbyAccountId(int accountId);

    @Update
    void updateAccount(Account accounts);

    @Delete
    void deleteAccount(Account accounts);
}