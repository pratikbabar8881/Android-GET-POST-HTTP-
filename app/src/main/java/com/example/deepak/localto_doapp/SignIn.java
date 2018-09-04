package com.example.deepak.localto_doapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignIn extends AppCompatActivity
{
    EditText username,password;
    TextView textusername,textpassword;
    Button signin;
    public static String TAG="todoapp";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);



        username=findViewById(R.id.et_username);
        password=findViewById(R.id.et_password);
        textusername=findViewById(R.id.tv_username);
        textpassword=findViewById(R.id.etPassword);
        signin=findViewById(R.id.bt_sign);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Post().execute();
                Toast.makeText(SignIn.this,"Successfully SignUp",Toast.LENGTH_LONG).show();
                Intent in=new Intent(SignIn.this,LoginActivity.class);
                startActivity(in);
            }
        });

    }

    public class Post extends AsyncTask<String,Void,Void>
    {
        @Override
        protected Void doInBackground(String... strings) {

            String BASE_URL="https://demo-todo-rest.herokuapp.com/signup/";
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                //int responseCode=conn.getResponseCode();

                System.out.println("Your URl:"+url);
               // System.out.println("Your Response Code is:"+responseCode);

                String data2="{\"username\":\"" +username.getText().toString()+"\",\"password\":\""+password.getText().toString()+"\"}";
                Log.d(TAG, "senddata: "+data2);

                OutputStream os=new DataOutputStream(conn.getOutputStream());
                os.write(data2.getBytes());

                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response=new StringBuffer();
                String Container="";

                while((Container=br.readLine())!=null)
                {
                    response.append(Container);
                }

                System.out.println("Your Response code is:"+response);
                SessionResponse sessionResponse=new Gson().fromJson(response.toString(),SessionResponse.class);

                System.out.println(sessionResponse.toString());
                System .out.println(sessionResponse.getToken());
                System .out.println(sessionResponse.getPassword());
                System .out.println(sessionResponse.getUsername());
                Log.d(TAG, "password "+sessionResponse.getPassword());

                SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("tokenkey",sessionResponse.getToken());
                editor.apply();
                Log.d(TAG, "doInBackground: "+sharedPreferences.getString("tokenkey",""));

                }catch (Exception e)
                {
                e.printStackTrace();
                }

                return null;
        }
    }
}
