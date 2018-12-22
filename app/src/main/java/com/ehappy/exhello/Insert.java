package com.ehappy.exhello;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

/**
 * Created by use on 2018/12/9.
 */

public class Insert extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    String email;

    String ServerURL = "http://120.105.129.146/test/update_note.php" ;

    EditText etTitle,etContent ;


    String TempTitle="", TempContent="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.acticity_insert_note);

        Intent intent = getIntent();

        email = intent.getStringExtra("EMAIL");

        // Get Reference to variables

        etTitle = (EditText) findViewById(R.id.title);

        etContent = (EditText) findViewById(R.id.content);

    }


    public void checkStore(View arg0) {
        GetData();

        //使用電子郵件和密碼初始化AsyncLogin

        InsertData(email, TempTitle,TempContent);

        Intent intent = new Intent(Insert.this,SuccessActivity.class);

        intent.putExtra("EMAIL",email);

        startActivity(intent);

        Insert.this.finish();

    }
    public void checkBack(View arg0){

        Intent intent = new Intent(Insert.this,SuccessActivity.class);

        intent.putExtra("EMAIL",email);

        startActivity(intent);

        Insert.this.finish();

    }

    public void GetData(){

        TempTitle = etTitle.getText().toString();

        TempContent = etContent.getText().toString();


    }

    public void InsertData(final String email, final String title,final String content){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String EmailHolder =email;

                String TitleHolder = title ;

                String ContentHo=content;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));

                nameValuePairs.add(new BasicNameValuePair("title", TitleHolder));

                nameValuePairs.add(new BasicNameValuePair("content", ContentHo));

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

                Toast.makeText(Insert.this, "新增成功", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(email,title,content);
    }
}