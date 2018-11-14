package csc472.depaul.edu.finalproject.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.TransactionsGetResponse;

import java.util.ArrayList;
import java.util.List;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.db.Account;
import csc472.depaul.edu.finalproject.db.DataProcessor;
import csc472.depaul.edu.finalproject.db.Transaction;
import csc472.depaul.edu.finalproject.db.TransactionCategory;
import csc472.depaul.edu.finalproject.db.TransactionDatabase;
import retrofit2.Response;

public class Transactions extends ApiBase implements ITransactionObserver {
    private DateRange dateRange;
    private static List<TransactionsGetResponse.Transaction> transactions;
    private static Context context;
    private ArrayList<ILoadDataObserver> iLoadDataObservers = new ArrayList<ILoadDataObserver>();
    private SharedPreferences sp;

    public Transactions(String clientId, String secret, DateRange dr, Context context, ILoadDataObserver ilo) {
        super(clientId, secret);
        dateRange = dr;
        this.context = context;
        iLoadDataObservers.add(ilo);
        sp = context.getSharedPreferences(context.getResources().getString(R.string.sp_filename), Activity.MODE_PRIVATE);
    }

    @Override
    public void getTransactions(List<Account> accounts) {
        transactions = new ArrayList<>();

        for (Account acc: accounts) {
            String accessToke = acc.getAccessToken();
            String itemId = acc.getItemId();

            try {
                PlaidClient plaidClient = PlaidClient.newBuilder()
                        .clientIdAndSecret(this.clientId, this.secret)
                        .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                        .build();

                Response<TransactionsGetResponse> response = plaidClient.service()
                        .transactionsGet(new TransactionsGetRequest(accessToke, dateRange.getDStartDate(), dateRange.getDEndDate())).execute();

                if (response.isSuccessful()) {
                    transactions.addAll(response.body().getTransactions());
                }
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {}
        }
        if (!transactions.equals(new ArrayList<>())) {
            saveTransaction();
            System.out.println("Got Transactions");
        }
    }

    private void saveTransaction() {
        List<Transaction> transactionList = new ArrayList<>();
        for(TransactionsGetResponse.Transaction trs: transactions) {
            Transaction t = new Transaction();
            List<String> ctg = trs.getCategory();
            t.setTransactionAmount(trs.getAmount());
            t.setTransactionCategory(ctg.get(0));
            t.setTransactionDate(trs.getDate());
            t.setTransactionName(trs.getName());

            transactionList.add(t);
        }

        DataProcessor.addTransactions(TransactionDatabase.getTransactionDatabase(context.getApplicationContext()), transactionList, this);
    }

    @Override
    public void getData() {
        DataProcessor.queryTransactionsGroupCategory(TransactionDatabase.getTransactionDatabase(context.getApplicationContext()), this);
    }

    @Override
    public void saveQueryResult(List<TransactionCategory> tra) {
        SharedPreferences.Editor editor = sp.edit();
        if (editor != null) {
            editor.clear();
            for (TransactionCategory tc: tra) {
                editor.putString(tc.getTransactionCategory(), Double.toString(tc.getCategoryAmount()));
                editor.commit();
            }
        }

        for (ILoadDataObserver ilo : iLoadDataObservers) {
            ilo.loadData();
        }
    }
}
