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
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity
{
    EditText username,password;
    Button login;
    public static String TAG="todoapp";

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String  usernamekey="namekey";
    public static final String  passwordkey="passwordkey";
    public static  final String tokenkey="tokenkey";


    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username=findViewById(R.id.etUsername);
        password=findViewById(R.id.etPassword);
        login=findViewById(R.id.btlogin);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String name=username.getText().toString();
               //String pass=password.getText().toString();
               // String token=


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(usernamekey,name);
                //editor.putString(passwordkey,pass);
                //editor.putString();
                editor.commit();
                Toast.makeText(LoginActivity.this,"data stored",Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: " + sharedpreferences.getString(usernamekey, ""));
                //Log.d(TAG, "onClick: "+editor.toString());

//                SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
//                sharedPreferences.edit().putString("token").commit();


                Intent in=new Intent(LoginActivity.this,FirstActivity.class);
                startActivity(in);
               new sendLogin().execute();
               //new sendGET().execute();

            }
        });

    }

    /*public class sendGET extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String BASE_URL = "http://192.168.100.21:8000";
                URL url = new URL(BASE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("","application/json; Content-type ");
                conn.getURL();
                //int responsecode=conn.getResponseCode();

                System.out.println("your enter url is"+url);
                //System.out.println("your enter response code is"+responsecode);


                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response= new StringBuffer();
                String Container="";
                while((Container=br.readLine())!=null)
                {
                    response.append(Container);
                }
                System.out.println("data is:"+response);
                Log.d(TAG, "doInBackground: "+response);
            }
            catch (Exception e)
            {
                e.printStackTrace();

            }

            return null;
        }
    }*/

    public class sendLogin extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            String BASE_URl="https://demo-todo-rest.herokuapp.com/login/";
            try
            {
                URL url=new URL(BASE_URl);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                conn.getURL();
                //int responsecode= conn.getResponseCode();


                System.out.println("Your URL is"+url);
                //System.out.println("Your Response code is"+responsecode);

                //String data2="{\"username\":\"" + username.getText().toString()+"\" ,\"password\":\""+password.getText().toString()+"\"}";
                String data2="{\"username\":\"" + "admin"+"\" ,\"password\":\""+"password"+"\"}";
                //byte[] bobj=data2.getBytes(StandardCharsets.UTF_8);
                OutputStream os=new DataOutputStream(conn.getOutputStream());
                os.write(data2.getBytes());
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response=new StringBuffer();
                String Container="";
                while((Container=br.readLine())!=null)
                {
                    response.append(Container);


                }
                System.out.println("data is"+response);
                SessionResponse sessionResponse=new Gson().fromJson(response.toString(),SessionResponse.class);

                System.out.println(sessionResponse.toString());
                System .out.println(sessionResponse.getToken());
                System .out.println(sessionResponse.getPassword());
                System .out.println(sessionResponse.getUsername());

                SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("tokenkey",sessionResponse.getToken());
                editor.apply();
                Log.d(TAG, "doInBackground: "+sharedPreferences.getString("tokenkey",""));
                //Log.d(TAG, "session response "+sessionReponse.getToken());
                //JsonParser parse=new JsonParser();
                //JSONObject obj=(JSONObject)parse.parse(response);



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        return  null;

        }
    }
}