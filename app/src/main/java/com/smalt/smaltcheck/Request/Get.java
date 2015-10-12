package com.smalt.smaltcheck.Request;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yassinebouderbala on 08/10/2015.
 */
public class Get {
    public static int send(String url){
        try {
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)link.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); //set timeout to 10 seconds
            connection.connect();
            return connection.getResponseCode();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 404;
        }
    }
}
