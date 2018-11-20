package csc472.depaul.edu.finalproject.models;

import android.os.AsyncTask;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;

import java.util.HashMap;

import csc472.depaul.edu.finalproject.db.Account;
import csc472.depaul.edu.finalproject.db.AccountDatabase;
import csc472.depaul.edu.finalproject.db.DataProcessor;
import retrofit2.Response;


public class AccessToken extends AsyncTask<Object, Void, Void> {
    @Override
    protected Void doInBackground(Object... params) {
        try {
            HashMap<String, String> linkData = (HashMap<String, String>)params[3];

            PlaidClient plaidClient = PlaidClient.newBuilder()
                    .clientIdAndSecret(params[1].toString(), params[2].toString())
                    .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                    .build();

            Response<ItemPublicTokenExchangeResponse> response = plaidClient.service()
                    .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(linkData.get("public_token").toString())).execute();

            if (response.isSuccessful()) {
                String accessToken = response.body().getAccessToken();
                String itemId = response.body().getItemId();
                Account acc = new Account();
                acc.setAccountType(linkData.get("account_type"));
                acc.setAccountSubtype(linkData.get("account_subtype"));
                acc.setInstitutionID(linkData.get("institution_id"));
                acc.setInstitutionName(linkData.get("institution_name"));
                acc.setAccessToken(accessToken);
                acc.setItemId(itemId);
                DataProcessor.addAccount((AccountDatabase) params[4], acc);
            }
        } catch (Exception ex) {

        } finally {

        }
        return null;
    }

//    protected void onPostExecute(String token) {
//        Account acc = new Account();
//        acc.setPublicToken(token);
//        DataProcessor.addAccount(this.db, acc);
//    }
}