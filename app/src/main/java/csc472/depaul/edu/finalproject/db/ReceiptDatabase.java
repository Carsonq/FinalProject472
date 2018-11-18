package csc472.depaul.edu.finalproject.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Receipt.class}, version = 1, exportSchema = false)
public abstract class ReceiptDatabase extends RoomDatabase {
    private static ReceiptDatabase INSTANCE;

    public abstract DaoReceipt daoReceipt();

    public static ReceiptDatabase getReceiptDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), ReceiptDatabase.class, "receipt_db")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE.close();
        INSTANCE = null;
    }
}
