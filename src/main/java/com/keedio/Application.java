package com.keedio;

import com.keedio.domain.Authenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static String subscriptionKey = "bb93fc9e4d4048829566a49233355fc9";
    private static String host = "speech.platform.bing.com";
    private static String contentType = "audio/wav; codec=\"audio/pcm\"; samplerate=16000";
    private static String baseUrl = "https://speech.platform.bing.com/speech/recognition";
    private static String languageUri = "language=en-US";
    private static String apiVersion = "v1";
    private static String service = "cognitiveservices";


    public static void main(String[] args) throws Exception {
        Authenticate auth = new Authenticate(subscriptionKey);
        String token = auth.getToken();
        String url = Gen.buildUrl(baseUrl, Mode.conversation, service, apiVersion, languageUri);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);

        String wavpath="/Users/shubhamagrawal/Documents/testFile4.wav";
        File wavfile = new File(wavpath);

        if (wavfile.exists()) {
            System.out.println("**** audio.wav DETECTED: "+wavfile);
        }
        else{
            System.out.println("**** audio.wav MISSING: " +wavfile);
        }
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestProperty("Transfer-Encoding","chunked");
        con.setRequestProperty("Accept", "application/json;text/xml");
        con.setRequestProperty("Host", host);
        con.setRequestProperty("Content-Type", contentType);

        OutputStream output=null;
        try {
            output = con.getOutputStream();
            byte [] music=new byte[(int) wavfile.length()];//size & length of the file
            InputStream             is  = new FileInputStream       (wavfile);
            BufferedInputStream bis = new BufferedInputStream   (is, 16000);
            DataInputStream dis = new DataInputStream       (bis);      //  Create a DataInputStream to read the audio data from the saved file
            Gen.copyStream(dis,output);
        }
        catch(Exception e){
            System.out.println("error occurred!! \n" + e.getStackTrace());
        }

        con.connect();
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("***ASR RESULT: " + response.toString());
        } else {
            System.out.println("error occurred!! Response code is: " + responseCode);
        }
    }
}
