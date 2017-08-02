package com.keedio.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;

/**
 * Created by shubhamagrawal on 02/08/17.
 */

// TODO: refreshToken logic is not taken care of in this version
public class Authenticate {

    public static final String FetchTokenUri = "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
    private String subscriptionKey;
    private String token;

    public Authenticate(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
        try {
            this.token = this.getToken(FetchTokenUri, subscriptionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken(){
        return token;
    }

    // issueToken
    private String getToken(String url, String subscriptionKey) throws Exception {
        // needed to change Host
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
        con.setRequestProperty("Content-Length", "0");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}