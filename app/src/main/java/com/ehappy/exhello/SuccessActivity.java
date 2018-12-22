package com.ehappy.exhello;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ehappy.exhello.R.id.recycler_view;

public class SuccessActivity extends AppCompatActivity implements MyAdapter.OnItemClickHandler  {

    private MyAdapter mAdapter;

    List<subjects> data=new ArrayList<>();

    RecyclerView recyclerView;

    String email;

    private ArrayList<String> mData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_success);

        Intent intent = getIntent();

        email = intent.getStringExtra("EMAIL");

        recyclerView = (RecyclerView) findViewById(recycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        new AsyncLogin().execute(email);

    }

    public void checkNewNote(View arg0) {

        Intent intent = new Intent(SuccessActivity.this,Insert.class);

        intent.putExtra("EMAIL",email);

        startActivity(intent);

        SuccessActivity.this.finish();

    }

    public void checkRe(View arg0) {

        finish();

        Intent intent = new Intent(SuccessActivity.this, SuccessActivity.class);

        intent.putExtra("EMAIL",email);

        startActivity(intent);
    }

    public void checkLogout(View arg0) {

        Intent intent = new Intent(SuccessActivity.this,MainActivity.class);

        startActivity(intent);

        SuccessActivity.this.finish();

    }

    public void onItemClick1(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);

        Toast.makeText(SuccessActivity.this, "item click index = "+childAdapterPosition, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SuccessActivity.this, Update.class);

        intent.putExtra("EMAIL",email);

        startActivity(intent);
    }

    public void onItemClick(String id,String title,String content) {

        Intent intent = new Intent(SuccessActivity.this, Update.class);

        intent.putExtra("EMAIL",email);

        intent.putExtra("ID",id);

        intent.putExtra("TITLE",title);

        intent.putExtra("CONTENT",content);

        startActivity(intent);
    }


    public void onItemRemove(int position, subjects text) {
    }


    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(SuccessActivity.this);

        HttpURLConnection conn;

        URL url = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            //this method will be running on UI thread

            pdLoading.setMessage("\t連接中...");

            //設為true為可以點選back button，false則是不行

            pdLoading.setCancelable(false);

            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides

                url = new URL("http://120.105.129.146/test/note_data.php");

            } catch (MalformedURLException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql

                conn = (HttpURLConnection)url.openConnection();

                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive

                conn.setDoInput(true);

                conn.setDoOutput(true);

                // Append parameters to URL

                Uri.Builder builder = new Uri.Builder()

                        .appendQueryParameter("email", params[0]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(

                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);

                writer.flush();

                writer.close();

                os.close();

                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block

                e1.printStackTrace();

                return "exception";

            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made

                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server

                    InputStream input = conn.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    StringBuilder result = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {

                        result.append(line);

                    }
                    // Pass data to onPostExecute method

                    return (result.toString());

                }
                else{

                    return ("unsuccessful");

                }
            }
            catch (IOException e) {

                e.printStackTrace();

                return e.toString();

            }
            finally {

                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {

            pdLoading.dismiss();


            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){

                    JSONObject json_data = jArray.getJSONObject(i);

                    subjects obj = new subjects();

                    obj.subId= json_data.getString("id");

                    obj.subTitle= json_data.getString("title");

                    obj.subContent= json_data.getString("content");



                    data.add(obj);
                }

                // Setup and Handover data to recyclerview

                recyclerView = (RecyclerView)findViewById(recycler_view);

                mAdapter = new MyAdapter(data, SuccessActivity.this);

                recyclerView.setAdapter(mAdapter);

                recyclerView.setLayoutManager(new LinearLayoutManager(SuccessActivity.this));

            } catch (JSONException e) {
               // Toast.makeText(SuccessActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}