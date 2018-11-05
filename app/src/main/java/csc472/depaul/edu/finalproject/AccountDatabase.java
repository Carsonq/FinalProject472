package csc472.depaul.edu.finalproject;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Account.class}, version = 1, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {
    private static AccountDatabase INSTANCE;

    public abstract DaoAccess daoAccess();

    public static AccountDatabase getAccountDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AccountDatabase.class, "account_db")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE.close();
        INSTANCE = null;
    }
}
