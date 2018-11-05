package csc472.depaul.edu.finalproject;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class DataProcessor {

    public static void addAccount(@NonNull final AccountDatabase db, Account account) {
        AsyncDBOperation task = new AsyncDBOperation(db, account, "insert");
        task.execute();
    }

    public static void queryAccount(@NonNull final AccountDatabase db, Account account) {
        AsyncDBOperation task = new AsyncDBOperation(db, account, "qeury");
        task.execute();
    }

//    private static Account addAcc(final AccountDatabase db, Account acc) {
//        db.daoAccess().insertOnlySingleAccount(acc);
//        return acc;
//    }

    private static void insertTask(AccountDatabase db, Account acc) {
        db.daoAccess().insertOnlySingleAccount(acc);
    }

    private static Account queryTask(AccountDatabase db, int accountID) {
        Account res = db.daoAccess().fetchOneAccountbyAccountId(accountID);
        return res;
    }

    private static class AsyncDBOperation extends AsyncTask<Void, Void, Void> {

        private final AccountDatabase accountDatabase;
        private final Account account;
        private final String type;

        AsyncDBOperation(AccountDatabase db, Account acc, String t) {
            accountDatabase = db;
            account = acc;
            type = t;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (type.equals("insert")){
                insertTask(accountDatabase, account);
            }
            else {
                queryTask(accountDatabase, account.getAccountId());
            }
            return null;
        }

//        @Override
//        protected void onPostExecute(Void result) {
//
//        }
    }
}