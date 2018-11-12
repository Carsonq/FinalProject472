package csc472.depaul.edu.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import csc472.depaul.edu.finalproject.R;

public class NewAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        HashMap<String, String> linkInitializeOptions = new HashMap<String, String>();
        linkInitializeOptions.put("key", getResources().getString(R.string.public_key));
        linkInitializeOptions.put("product", "transactions,auth,income");
        linkInitializeOptions.put("apiVersion", "v2"); // set this to "v1" if using the legacy Plaid API
        linkInitializeOptions.put("env", "sandbox");
        linkInitializeOptions.put("clientName", "Test App");
        linkInitializeOptions.put("selectAccount", "true");
        linkInitializeOptions.put("webhook", "http://requestb.in");
        linkInitializeOptions.put("baseUrl", "https://cdn.plaid.com/link/v2/stable/link.html");

        final Uri linkInitializationUrl = generateLinkInitializationUrl(linkInitializeOptions);

        final WebView plaidLinkWebview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = plaidLinkWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebView.setWebContentsDebuggingEnabled(true);

        plaidLinkWebview.loadUrl(linkInitializationUrl.toString());

        plaidLinkWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Parse the URL to determine if it's a special Plaid Link redirect or a request
                // for a standard URL (typically a forgotten password or account not setup link).
                // Handle Plaid Link redirects and open traditional pages directly in the  user's
                // preferred browser.
                Uri parsedUri = Uri.parse(url);
                if (parsedUri.getScheme().equals("plaidlink")) {
                    String action = parsedUri.getHost();
                    final HashMap<String, String> linkData = parseLinkUriData(parsedUri);

                    if (action.equals("connected")) {
                        // User successfully linked
                        Log.d("Public token: ", linkData.get("public_token"));
                        Log.d("Account ID: ", linkData.get("account_id"));
                        Log.d("Account name: ", linkData.get("account_name"));
                        //Log.d("Institution type: ", linkData.get("institution_type"));
                        Log.d("Institution name: ", linkData.get("institution_name"));

                        new AccessToken().execute(getResources().getString(R.string.plaid_mode),
                                getResources().getString(R.string.client_id),
                                getResources().getString(R.string.secret),
                                linkData.get("public_token"),
                                AccountDatabase.getAccountDatabase(getApplicationContext()));

//                        plaidLinkWebview.setWebViewClient(new WebViewClient() {
//                            @Override
//                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                Uri parsedUri = Uri.parse(url);
//                                final HashMap<String, String> linkData = parseLinkUriData(parsedUri);
//                                return true;
//                            }
//                        });

//                        Account acc = new Account();
//                        acc.setPublicToken(linkData.get("public_token"));
//                        DataProcessor.addAccount(AccountDatabase.getAccountDatabase(getApplicationContext()), acc);

//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Account acc = new Account();
//                                acc.setPublicToken(linkData.get("public_token"));
//                                addAccount(AccountDatabase.getAccountDatabase(getApplicationContext()), acc);
//                            }
//                        }) .start();


                        // Reload Link in the Webview
                        // You will likely want to transition the view at this point.

//                        HashMap<String, String> linkOptions = new HashMap<String,String>();
//                        linkOptions.put("public_token", linkData.get("public_token"));
//                        linkOptions.put("baseUrl", "https://cdn.plaid.com/link/v2/stable/link.html/get_access_token");
//                        Uri linkUrl = generateLinkInitializationUrl(linkOptions);
//
//                        plaidLinkWebview.loadUrl(linkUrl.toString());

                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("investment_data", investment);
                        getActivity().startActivity(intent);
                    } else if (action.equals("exit")) {
                        // User exited
                        // linkData may contain information about the user's status in the Link flow,
                        // the institution selected, information about any error encountered,
                        // and relevant API request IDs.
                        Log.d("User status in flow: ", linkData.get("status"));
                        // The requet ID keys may or may not exist depending on when the user exited
                        // the Link flow.
                        Log.d("Link request ID: ", linkData.get("link_request_id"));
                        Log.d("API request ID: ", linkData.get("plaid_api_request_id"));

                        // Reload Link in the Webview
                        // You will likely want to transition the view at this point.
                        plaidLinkWebview.loadUrl(linkInitializationUrl.toString());
                    } else if (action.equals("event")) {
                        // The event action is fired as the user moves through the Link flow
                        Log.d("Event name: ", linkData.get("event_name"));
//                        if (linkData.get("event_name").equals("EXIT")) {
//                            Intent intent = new Intent(getActivity(), MainActivity.class);
//                            getActivity().startActivity(intent);
//                        }
                    } else {
                        Log.d("Link action detected: ", action);
                    }
                    // Override URL loading
                    return true;
                } else if (parsedUri.getScheme().equals("https") ||
                        parsedUri.getScheme().equals("http")) {
                    // Open in browser - this is most  typically for 'account locked' or
                    // 'forgotten password' redirects
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    // Override URL loading
                    return true;
                } else {
                    // Unknown case - do not override URL loading
                    return false;
                }
            }
        });
    }

    private Uri generateLinkInitializationUrl(HashMap<String, String> linkOptions) {
        Uri.Builder builder = Uri.parse(linkOptions.get("baseUrl"))
                .buildUpon()
                .appendQueryParameter("isWebview", "true")
                .appendQueryParameter("isMobile", "true");
        for (String key : linkOptions.keySet()) {
            if (!key.equals("baseUrl")) {
                builder.appendQueryParameter(key, linkOptions.get(key));
            }
        }
        return builder.build();
    }

    public HashMap<String, String> parseLinkUriData(Uri linkUri) {
        HashMap<String, String> linkData = new HashMap<String, String>();
        for (String key : linkUri.getQueryParameterNames()) {
            linkData.put(key, linkUri.getQueryParameter(key));
        }
        return linkData;
    }

    private final NewAuthActivity getActivity() {
        return this;
    }
}

//class CallAPI extends AsyncTask<String, String, String> {
//
//    public CallAPI(){
//        //set context variables if required
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void doInBackground(String... params) {
//        String urlString = params[0]; // URL to call
//        String data = params[1]; //data to post
//        OutputStream out = null;
//
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            out = new BufferedOutputStream(urlConnection.getOutputStream());
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//            writer.write(data);
//            writer.flush();
//            writer.close();
//            out.close();
//
//            urlConnection.connect();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}

