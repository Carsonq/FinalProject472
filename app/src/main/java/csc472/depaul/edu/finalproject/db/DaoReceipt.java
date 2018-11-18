package csc472.depaul.edu.finalproject.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DaoReceipt {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOne(Receipt receipt);

    @Query("SELECT * FROM Receipt WHERE date(receipt_date) BETWEEN date(:minDate) AND date(:maxDate) ORDER BY receipt_date")
    LiveData<List<Receipt>> fetchReceipts(String minDate, String maxDate);

    @Query("SELECT strftime(\"%Y-%m-%d\", date(receipt_date)) as receipt_date, sum(receipt_total) as day_total FROM Receipt WHERE date(receipt_date) BETWEEN date(:minDate) AND date(:maxDate) GROUP BY date(receipt_date)")
    List<ReceiptDate> fetchReceiptsByDate(String minDate, String maxDate);
}