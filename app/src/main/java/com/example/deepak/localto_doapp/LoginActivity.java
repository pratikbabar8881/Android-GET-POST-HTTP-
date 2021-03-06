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
    Button login,signin;
    public static String TAG="todoapp";

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String  usernamekey="namekey";
    public static final String  passwordkey="passwordkey";
    public static  final String tokenkey="tokenkey";
    public static final String loginkey="loginkey";

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(loginkey.equals("true")) {
            startActivity(new Intent(getApplicationContext(),FirstActivity.class));
        }
        username=findViewById(R.id.etUsername);
        password=findViewById(R.id.etPassword);
        login=findViewById(R.id.btlogin);
        signin=findViewById(R.id.bt_signin);

        TaskDTO obj= new TaskDTO("","",getTaskId(),"getDone");
        Log.d(TAG, "onCreate: "+getTaskId());
       final String complete= obj.getDone();
        System.out.println(complete);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String name = username.getText().toString();
                    String passwordValue=password.getText().toString();

                    if (name.equals("") && passwordValue.equals("")){

                        Toast.makeText(LoginActivity.this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
                    }else {
                        new sendLogin().execute();
                    }
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(usernamekey, name);
                    editor.putString(passwordkey,passwordValue);
                    editor.putString(loginkey,"true");
                    editor.commit();
                    Log.d(TAG, "onClick: " + sharedpreferences.getString(usernamekey, ""));




                //new sendGET().execute();

                }

        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //SignIn Code here

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(LoginActivity.this,SignIn.class);
                startActivity(in);
            }
        });







        ///////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public class sendLogin extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            String BASE_URl="http://192.168.100.7:7000/login/";
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

                String data2="{\"username\":\"" + username.getText().toString()+"\" ,\"password\":\""+password.getText().toString()+"\"}";
               // String data2="{\"username\":\"" + "admin"+"\" ,\"password\":\""+"password"+"\"}";
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

                if (sessionResponse.getToken()!=null){
                    Intent navigateToNextActivity=new Intent();
                    navigateToNextActivity.setClass(LoginActivity.this,FirstActivity.class);
                    navigateToNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(navigateToNextActivity);
                }
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
