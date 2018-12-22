package com.ehappy.exhello;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

public class MainActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    public String email="";

    public static final int CONNECTION_TIMEOUT=10000;

    public static final int READ_TIMEOUT=15000;

    public EditText etEmail;

    private EditText etPassword;

    private Button btnR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Get Reference to variables

        etEmail = (EditText) findViewById(R.id.email);

        etPassword = (EditText) findViewById(R.id.password);

        btnR=(Button)findViewById(R.id.buttonRegister);

        btnR.setOnClickListener(myListener);
    }


    public void checkLogin(View arg0) {

        // Get text from email and passord field

        email = etEmail.getText().toString();

        final String password = etPassword.getText().toString();

        //使用電子郵件和密碼初始化AsyncLogin

        new AsyncLogin().execute(email,password);

    }
    private Button.OnClickListener myListener=new Button.OnClickListener(){
        public void onClick(View v){

            Intent intent = new Intent();

            intent.setClass(MainActivity.this, Registered.class);

            startActivity(intent);

            MainActivity.this.finish();
        }
    };


//停止使用者操作
    private class AsyncLogin extends AsyncTask<String, String, String>
            {
                ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

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

                        url = new URL("http://120.105.129.146/test/login.inc.php");

                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block

                        e.printStackTrace();

                        return "exception";
                    }
                    try {
                        // Setup HttpURLConnection class to send and receive data from php and mysql

                        conn = (HttpURLConnection)url.openConnection();

                        conn.setReadTimeout(READ_TIMEOUT);

                        conn.setConnectTimeout(CONNECTION_TIMEOUT);

                        conn.setRequestMethod("POST");

                        // setDoInput and setDoOutput method depict handling of both send and receive

                        conn.setDoInput(true);

                        conn.setDoOutput(true);

                        // Append parameters to URL

                        Uri.Builder builder = new Uri.Builder()

                                .appendQueryParameter("email", params[0])

                                .appendQueryParameter("password", params[1]);

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

                            return(result.toString());

                        }
                        else{

                            return("unsuccessful");

                        }
                    } catch (IOException e) {

                        e.printStackTrace();

                        return "exception";

                    }
                    finally {

                        conn.disconnect();
                    }
                }
                @Override
                protected void onPostExecute(String result) {


                    pdLoading.dismiss();

                    if(result.equalsIgnoreCase("true"))
                    {
                        Intent intent = new Intent(MainActivity.this,SuccessActivity.class);

                        intent.putExtra("EMAIL",email);

                        startActivity(intent);

                        MainActivity.this.finish();

                        Toast.makeText(MainActivity.this, "成功登入", Toast.LENGTH_LONG).show();

                    }else if (result.equalsIgnoreCase("false")){

                        Toast.makeText(MainActivity.this, "無效的email或密碼", Toast.LENGTH_LONG).show();

                    } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                        Toast.makeText(MainActivity.this, "出問題了，無法連接資料庫", Toast.LENGTH_LONG).show();

                    }
        }


    }
}