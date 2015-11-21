package com.example.kevin.gradecalculator;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by 100484144 on 10/26/2015.
 */
public class GetLicenseAsync extends AsyncTask<String, String, String> {
    private LicenseListener listener = null;
    private Exception exception = null;
    private String license = "";

    public GetLicenseAsync(LicenseListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        int count;
        try {
            URL url = new URL(params[0]);
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                // str is one line of text; readLine() strips the newline character(s)
                license = license + str + "\n";
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }

        return license;
    }

    @Override
    protected void onPostExecute(String result) {
        if (exception != null) {
            exception.printStackTrace();
            return;
        }

        Log.d("InternetResourcesSample", "setting definition: " + license);
        listener.showLicense(license);
    }
}
