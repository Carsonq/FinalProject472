package csc472.depaul.edu.finalproject;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DataProcessor {

    public static void addAccount(@NonNull final AccountDatabase db, Account account) {
        new AsyncDBOperation(db, account, "insert").execute();
    }

    public static void queryAccount(@NonNull final AccountDatabase db, Account account, ITokenObserver ito) {
        new AsyncDBOperation(db, account, "query", ito).execute();
    }

    public static void queryAccount(@NonNull final AccountDatabase db, ITokenObserver ito) {
        new AsyncDBOperation(db, null, "query", ito).execute();
    }

//    private static Account addAcc(final AccountDatabase db, Account acc) {
//        db.daoAccess().insertOnlySingleAccount(acc);
//        return acc;
//    }

    private static void insertTask(AccountDatabase db, Account acc) {
        db.daoAccess().insertOnlySingleAccount(acc);
    }

    private static List<Account> queryTask(AccountDatabase db, int accountID) {
        List<Account> res = db.daoAccess().fetchOneAccountbyAccountId(accountID);
        return res;
    }

    private static List<Account> queryTask(AccountDatabase db) {
        List<Account> res = db.daoAccess().fetchAll();
        return res;
    }

    private static class AsyncDBOperation extends AsyncTask<Void, Void, List<Account>> {

        private final AccountDatabase accountDatabase;
        private final Account account;
        private final String type;
        private ArrayList<ITokenObserver> iTokenObservers = new ArrayList<ITokenObserver>();

        AsyncDBOperation(AccountDatabase db, Account acc, String t) {
            accountDatabase = db;
            account = acc;
            type = t;
        }

        AsyncDBOperation(AccountDatabase db, Account acc, String t, ITokenObserver ito) {
            accountDatabase = db;
            account = acc;
            type = t;
            iTokenObservers.add(ito);
        }

        @Override
        protected List<Account> doInBackground(final Void... params) {
            if (type.equals("insert")) {
                insertTask(accountDatabase, account);
            } else if (type.equals("query")) {
                if (account != null) {
                    List<Account> x = queryTask(accountDatabase, account.getAccountId());
                    if (iTokenObservers != null) {
                        for(ITokenObserver ito: iTokenObservers) {
                            ito.getTransactions(queryTask(accountDatabase, account.getAccountId()));
                        }
                    }
                } else {
                    List<Account> x = queryTask(accountDatabase);
                    if (iTokenObservers != null) {
                        for(ITokenObserver ito: iTokenObservers) {
                            ito.getTransactions(queryTask(accountDatabase));
                        }
                    }
                }
            }
            return null;
        }

//        @Override
//        protected void onPostExecute(Void result) {
//
//        }
    }
}