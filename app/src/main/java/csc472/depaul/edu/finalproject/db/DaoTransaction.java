package csc472.depaul.edu.finalproject.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoTransaction {
    @Insert
    void insertMultipleTransaction(List<Transaction> transactionList);

    @Query("SELECT * FROM `Transaction`")
    List<Transaction> fetchAllTransactions();

    @Query("SELECT * FROM `Transaction` ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> findAll();

    @Query("SELECT transaction_category, SUM(transaction_amount) as category_amount FROM `Transaction` GROUP BY transaction_category")
    List<TransactionCategory> fetchTransactionsByCategory();

    @Query("DELETE FROM `Transaction`")
    void deleteAllTransactions();
}