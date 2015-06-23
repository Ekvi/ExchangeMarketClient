package com.ekvilan.exchangemarket.utils;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConnectionProvider {
    private String LOG_TAG = "myLog";
    private String jsonFromServer;

    public String POST(String urlString, String json){
        String result = "";

        DataOutputStream dataOutputStream;

        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            dataOutputStream = new DataOutputStream(connection.getOutputStream ());

            byte[] data = json.getBytes("UTF-8");

            dataOutputStream.write(data);
            dataOutputStream.flush ();
            dataOutputStream.close ();

            result = connection.getResponseMessage();
            jsonFromServer = convertInputStreamToString(connection.getInputStream());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Can't send json file!");
            e.printStackTrace();
        }
        return result;
    }

    private String convertInputStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public boolean isConnected(ConnectivityManager connectivityManager){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public String getJson() {
        return jsonFromServer;
    }
}
