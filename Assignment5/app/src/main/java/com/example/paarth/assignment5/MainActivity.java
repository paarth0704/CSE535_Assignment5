package com.example.paarth.assignment5;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    ProgressBar progressBar;
    List<MyTask> tasks;
    String url = "https://www.iiitd.ac.in/about";
    String content = "";
    String words;
    private static final String TAG = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        if (savedInstanceState != null) {
             words = savedInstanceState.getString(TAG, "");
             content = savedInstanceState.getString(TAG,"");
             updateDisplay(words);

        }

        tasks = new ArrayList<>();


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);




        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkconnectivity()) {
                    requestdata();
                }else{
                    //Log.d("MainActivity", "Network not available");
                    Toast.makeText(getApplicationContext(),"Network not available",Toast.LENGTH_LONG).show();

                }


            }
        });




    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d(TAG,"Inside onsaveinstance");
        outState.putString(TAG,words);
        //outState.putString(TAG,content);
        super.onSaveInstanceState(outState);

    }

    private void requestdata() {
        MyTask task = new MyTask();
        task.execute();
    }


    protected  void updateDisplay(String message){

        textView.append(message + "\n");

    }


    protected boolean checkconnectivity() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;

        }

    }


    private class MyTask extends AsyncTask<Void, Void, Void>{





        @Override
        protected void onPreExecute() {
            updateDisplay("Starting Task..");
            if(tasks.size()==0) {
                progressBar.setVisibility(View.VISIBLE);
            }
            tasks.add(this);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect(url).get();
                words = doc.text();
            } catch (IOException e) {
                e.printStackTrace();
            }

            content = HttpManager.getData(url);


            return null;
        }

        @Override
        protected void onPostExecute(Void s) {

            updateDisplay(words);
           // updateDisplay(content);
            Log.d("MainActivity",content);
            tasks.remove(this);
            if(tasks.size()==0) {
                progressBar.setVisibility(View.INVISIBLE);
            }


        }



    }



}
