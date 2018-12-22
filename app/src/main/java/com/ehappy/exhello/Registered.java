package com.ehappy.exhello;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Registered extends AppCompatActivity {

    String ServerURL = "http://120.105.129.146/test/get_data.php" ;

    EditText etUser, etEmail,etPassword,etCheckPass ;

    Button btnRegister,btnRegisterLogin;

    String TempName="", TempEmail="",TempPass="",TempPasscon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.registered);

        etEmail = (EditText) findViewById(R.id.etMail);

        etUser = (EditText)findViewById(R.id.etUsername);

        etPassword = (EditText) findViewById(R.id.etPassword);

        etCheckPass = (EditText) findViewById(R.id.etConfirmPassword);

        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnRegisterLogin = (Button)findViewById(R.id.btnRegisterLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        GetData();

        if(TempName.isEmpty()|| TempEmail.isEmpty()||TempPass.isEmpty()||TempPasscon.isEmpty()) {

            Toast.makeText(Registered.this, "請填上完整資料", Toast.LENGTH_SHORT).show();

        }
        else {

            if(TempPass.equals(TempPasscon)) {

                 InsertData(TempName, TempEmail, TempPass);

                 Intent intent = new Intent();

                 intent.setClass(Registered.this, MainActivity.class);

                 startActivity(intent);

                 Registered.this.finish();

            }
            else {

                Toast.makeText(Registered.this, "驗證密碼錯誤", Toast.LENGTH_SHORT).show();

            }
        }
            }
        });


        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setClass(Registered.this, MainActivity.class);

                startActivity(intent);

                Registered.this.finish();


            }
        });

    }




    public void GetData(){

        TempName = etUser.getText().toString();

        TempEmail = etEmail.getText().toString();

        TempPass = etPassword.getText().toString();

        TempPasscon = etCheckPass.getText().toString();

    }

    public void InsertData(final String name, final String email, final String password){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String EmailHolder =email;

                String NameHolder = name ;

                String PasswordHo=password;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));

                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));

                nameValuePairs.add(new BasicNameValuePair("password", PasswordHo));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";

            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(Registered.this, "註冊成功", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name,email,password);
    }

}