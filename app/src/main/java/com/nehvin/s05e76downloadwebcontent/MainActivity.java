package com.nehvin.s05e76downloadwebcontent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            URL url;
            HttpsURLConnection urlConnection = null;
            InputStream in = null;
            InputStreamReader reader=null;
            try {
                Log.i("URL to be accessed", params[0]);
                url = new URL(params[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                in = urlConnection.getInputStream();
                reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (IOException e) {
                e.printStackTrace();
                return "failed";
            }
            finally {
                try {
                    if(in != null)
                        in.close();
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result = null;

        try {
            result = task.execute("https://www.ecowebhosting.co.uk").get();
//            result = task.execute("216.58.199.131").get();
//            result = task.execute("https://216.58.199.131/").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TextView txt = (TextView)findViewById(R.id.txtView);
        txt.setText(result);
        Log.i("Contents of URL", result);
    }
}