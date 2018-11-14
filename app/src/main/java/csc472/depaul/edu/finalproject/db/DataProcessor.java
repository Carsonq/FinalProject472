package csc472.depaul.edu.finalproject.db;

import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import csc472.depaul.edu.finalproject.models.ITransactionObserver;

public class DataProcessor {

    public static void addAccount(@NonNull final AccountDatabase db, Account account) {
        new AsyncDBOperation(db, account, "insert_account").execute();
    }

    public static void addTransactions(@NonNull final TransactionDatabase db, List<Transaction> transactions, ITransactionObserver ito) {
        new AsyncDBOperation(db, transactions, "insert_transactions", ito).execute();
    }

    public static void queryAccounts(@NonNull final AccountDatabase db, ITransactionObserver ito) {
        new AsyncDBOperation(db, null, "query_accounts", ito).execute();
    }

    public static void queryTransactions(@NonNull final TransactionDatabase db) {
        new AsyncDBOperation(db, null, "query_transactions").execute();
    }

    public static void queryTransactionsGroupCategory(@NonNull final TransactionDatabase db, ITransactionObserver ito) {
        new AsyncDBOperation(db, null, "query_transactions_group_category", ito).execute();
    }

    private static void insertTask(AccountDatabase db, Account acc) {
        db.daoAccess().insertOnlySingleAccount(acc);
    }

    private static void insertTask(TransactionDatabase db, List<Transaction> transactions) {
        db.daoTransaction().insertMultipleTransaction(transactions);
    }

    private static List<Account> queryTask(AccountDatabase db, int accountID) {
        List<Account> res = db.daoAccess().fetchOneAccountbyAccountId(accountID);
        return res;
    }

    private static List<Account> queryTask(AccountDatabase db) {
        List<Account> res = db.daoAccess().fetchAllAccounts();
        return res;
    }

    private static List<Transaction> queryTask(TransactionDatabase db) {
        List<Transaction> res = db.daoTransaction().fetchAllTransactions();
        return res;
    }

    private static List<TransactionCategory> queryTaskByCategory(TransactionDatabase db) {
        List<TransactionCategory> res = db.daoTransaction().fetchTransactionsByCategory();
        return res;
    }

//    private static void saveToSP(int x) {
//        sp =.getSharedPreferences(context.getResources().getString(R.string.sp_filename), Activity.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sp.edit();
//        if (editor != null) {
//            String sText = Integer.toString(x);
//            if (sText != null) {
//                editor.putString("last_channel", sText);
//                editor.commit();
//            }
//        }
//    }

    private static class AsyncDBOperation extends AsyncTask<Void, Void, Void> {

        private final RoomDatabase db;
        private final Object data;
        private final String type;
        private ArrayList<ITransactionObserver> iTransactionObservers = new ArrayList<ITransactionObserver>();

        AsyncDBOperation(RoomDatabase db, Object data, String t) {
            this.db = db;
            this.data = data;
            type = t;
        }

        AsyncDBOperation(RoomDatabase db, Object data, String t, ITransactionObserver ito) {
            this.db = db;
            this.data = data;
            type = t;
            iTransactionObservers.add(ito);
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (type.equals("insert_account")) {
                insertTask((AccountDatabase) db, (Account) data);
            } else if (type.equals("insert_transactions")) {
                insertTask((TransactionDatabase) db, (List<Transaction>) data);
                if (iTransactionObservers != null) {
                    for (ITransactionObserver ito : iTransactionObservers) {
                        ito.getData();
                    }
                }
            } else if (type.equals("query_accounts")) {
                List<Account> accs = queryTask((AccountDatabase) db);
                if (iTransactionObservers != null) {
                    for (ITransactionObserver ito : iTransactionObservers) {
                        ito.getTransactions(accs);
                    }
                }
            } else if (type.equals("query_transactions")) {
                queryTask((TransactionDatabase) db);
            } else if (type.equals("query_transactions_group_category")) {
                List<TransactionCategory> tra = queryTaskByCategory((TransactionDatabase) db);
                if (iTransactionObservers != null) {
                    for (ITransactionObserver ito : iTransactionObservers) {
                        ito.saveQueryResult(tra);
                    }
                }
            }
            return null;
        }
    }
}