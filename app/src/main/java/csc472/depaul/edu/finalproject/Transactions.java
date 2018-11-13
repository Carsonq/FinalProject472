package csc472.depaul.edu.finalproject;

import com.plaid.client.PlaidApiService;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.TransactionsGetResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Transactions extends ApiBase implements ITokenObserver {
    private DateRange dateRange;
    private static List<TransactionsGetResponse.Transaction> transactions;

    Transactions(String clientId, String secret, DateRange dr) {
        super(clientId, secret);
        dateRange = dr;
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
            System.out.println("Got Transactions");
        }
    }
}
