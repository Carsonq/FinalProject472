package csc472.depaul.edu.finalproject;

import android.os.AsyncTask;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;

import org.json.JSONObject;

import java.util.HashMap;

import csc472.depaul.edu.finalproject.utils.HttpUtils;
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
            PlaidClient plaidClient = PlaidClient.newBuilder()
                    .clientIdAndSecret(params[1].toString(), params[2].toString())
                    .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                    .build();

            Response<ItemPublicTokenExchangeResponse> response = plaidClient.service()
                    .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(params[3].toString())).execute();

            if (response.isSuccessful()) {
                String accessToken = response.body().getAccessToken();
                String itemId = response.body().getItemId();
                Account acc = new Account();
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