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
    /**
     * @param0 the mode
     * @param1 the client_id
     * @param2 the secret
     * @param3 the public_token
     * @param4 the db
     */
    @Override
    protected Void doInBackground(Object... params) {

//        String url = "https://" + params[0].toString() + ".plaid.com/item/public_token/exchange";
//        JSONObject data = new JSONObject();
//        try {
//            data.put("client_id", params[1].toString());
//            data.put("secret", params[2].toString());
//            data.put("public_token", params[3].toString());
//
//            String accessTokenInfo = HttpUtils.submitPostData(url, data.toString());
//            JSONObject res = new JSONObject(accessTokenInfo);
//            String ttt = res.get("access_token").toString();
//            String xxx = res.get("item_id").toString();
//
//
//            if (!accessToken.equals("-1") || !accessToken.startsWith("err")) {
//
//                Account acc = new Account();
//                acc.setPublicToken(accessToken);
//                DataProcessor.addAccount((AccountDatabase) params[4], acc);
//            }
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }

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