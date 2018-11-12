package csc472.depaul.edu.finalproject.utils;

import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.io.IOException;
import java.net.URLEncoder;
import java.io.ByteArrayOutputStream;

public class HttpUtils {
    public static String submitPostData(String strUrlPath, String params) {
        try {
            URL url = new URL(strUrlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("User-Agent", "Plaid Java");
            httpURLConnection.setRequestProperty("Plaid-Version", "2018-05-22");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");

//            DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
//            os.writeBytes(params.toString());
//            os.flush();
//            os.close();

            OutputStreamWriter os = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
            os.write(params);
            os.flush();
            os.close();

            int response = httpURLConnection.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                httpURLConnection.disconnect();
                return dealResponseResult(inputStream);
            }
        } catch (IOException e) {
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }

    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static String dealResponseResult(InputStream inputStream) {

        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            streamReader.close();
            return responseStrBuilder.toString();
        } catch (Exception ex) {
        }

        return "-1";
//        String resultData = null;
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byte[] data = new byte[1024];
//        int len = 0;
//        try {
//            while ((len = inputStream.read(data)) != -1) {
//                byteArrayOutputStream.write(data, 0, len);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        resultData = new String(byteArrayOutputStream.toByteArray());
//        return resultData;
    }
}