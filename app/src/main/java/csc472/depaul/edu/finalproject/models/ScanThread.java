package csc472.depaul.edu.finalproject.models;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.db.AccountViewModel;
import csc472.depaul.edu.finalproject.db.DataProcessor;
import csc472.depaul.edu.finalproject.db.Receipt;
import csc472.depaul.edu.finalproject.db.ReceiptDatabase;
import csc472.depaul.edu.finalproject.db.ReceiptViewModel;
import csc472.depaul.edu.finalproject.utils.HttpUtils;

public class ScanThread implements Runnable {

    String imageFileName;
    String postUrl;
    String resUrl;
    Context context;

    public ScanThread(String resUrl, Context context) {
        this.resUrl = resUrl;
        this.context = context;
    }

    public ScanThread(String imageFileName, String posturl, String resUrl, Context context) {
        this.imageFileName = imageFileName;
        this.postUrl = posturl;
        this.resUrl = resUrl;
        this.context = context;
    }

    @Override
    public void run() {
        if (imageFileName == null) {
            resumeScanToken();
        } else {
            try {
//            String res = HttpUtils.postFile(imageFileName, postUrl);
                String res = "{\"message\":\"WARNING: Image uploaded successfully, but did not meet the recommended dimension of 720x1280 (WxH). Please follow the requirement to get the most accurate result\",\"status\":\"success\",\"status_code\":2,\"token\":\"lGpQaTRUfhioU2Xd\",\"success\":true,\"code\":300}";

                JSONObject resJsonObj = new JSONObject(res);
                boolean requestStatus = resJsonObj.getBoolean("success");
                String newFilePath = "/";
                if (requestStatus) {
                    String token = resJsonObj.getString("token");
                    File file = new File(imageFileName);
                    String[] p = imageFileName.split("/");
                    for (int i = 0; i < p.length - 1; i++) {
                        newFilePath = newFilePath + "/" + p[i];
                    }
                    newFilePath = newFilePath + "/" + token + ".jpg";
                    File newFile = new File(newFilePath);
                    file.renameTo(newFile);

                    getScanResult(resUrl, token);
                }

                if (!newFilePath.equals("/")) {
                    File file = new File(newFilePath);
//                file.delete();
                }

                if (imageFileName != null) {
                    File file = new File(imageFileName);
//                file.delete();
                    imageFileName = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveScanToken(String token) {
        SharedPreferences sp = context.getSharedPreferences(context.getResources().getString(R.string.sp_pending_token_filename), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (editor != null) {
            editor.putString(token, "pending");
            editor.commit();
        }
    }

    private void removeScanToken(String token) {
        SharedPreferences sp = context.getSharedPreferences(context.getResources().getString(R.string.sp_pending_token_filename), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (editor != null) {
            editor.remove(token);
            editor.commit();
        }
    }

    private void resumeScanToken() {
        SharedPreferences sp = context.getSharedPreferences(context.getResources().getString(R.string.sp_pending_token_filename), Activity.MODE_PRIVATE);
        Set<String> pendingToken = ((Map<String, String>) sp.getAll()).keySet();
        for (String pt : pendingToken) {
            getScanResult(resUrl, pt);
        }
    }

    private void getScanResult(String resUrl, String token) {
        resUrl = resUrl + '/' + token;
        try {
//            String receiptRes = HttpUtils.getRequest(resUrl);
            String receiptRes = "{\"message\":\"SUCCESS: Result available\",\"status\":\"pending\",\"status_code\":3,\"type\":0,\"result\":{\"establishment\":\"Cos\",\"date\":\"2014-01-25 10:36:00\",\"total\":\"28.11\",\"url\":\"\",\"phoneNumber\":\"323-603-0004\",\"paymentMethod\":\"VISA\",\"address\":\"\",\"cash\":\"0.00\",\"change\":\"0.00\",\"validatedTotal\":true,\"subTotal\":\"26.26\",\"validatedSubTotal\":false,\"tax\":\"1.85\",\"taxes\":[0.1,1.75],\"discount\":\"0.00\",\"discounts\":[],\"lineItems\":[{\"qty\":1,\"desc\":\"Gothiing C9 Aniivevea\",\"price\":\"0.00\",\"lineTotal\":\"14.99\"}]},\"success\":true,\"code\":202}";
            JSONObject resJsonObj = new JSONObject(receiptRes);
            while (resJsonObj.getString("status").equals("pending")) {
                saveScanToken(token);
                receiptRes = HttpUtils.getRequest(resUrl);
                resJsonObj = new JSONObject(receiptRes);
            }

            removeScanToken(token);

            if (resJsonObj.getString("status").equals("done")) {
                String receiptDate = resJsonObj.getJSONObject("result").getString("date");
                Double receiptTotal = resJsonObj.getJSONObject("result").getDouble("total");
                Double receiptSubtotal = resJsonObj.getJSONObject("result").getDouble("subTotal");
                Double receiptTax = resJsonObj.getJSONObject("result").getDouble("tax");

                Receipt r = new Receipt();
                r.setReceiptDate(receiptDate);
                r.setReceiptTotal(receiptTotal);
                r.setReceiptSubtotal(receiptSubtotal);
                r.setReceiptTax(receiptTax);
                r.setReceiptToken(token);
                DataProcessor.addReceipt(ReceiptDatabase.getReceiptDatabase(context), r);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
