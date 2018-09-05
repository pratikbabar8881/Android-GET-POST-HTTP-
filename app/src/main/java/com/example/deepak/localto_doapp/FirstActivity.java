package com.example.deepak.localto_doapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.example.deepak.localto_doapp.LoginActivity.MyPREFERENCES;
import static com.example.deepak.localto_doapp.LoginActivity.usernamekey;

public class FirstActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String usernamekey="namekey";
    public static final String passwordkey="passwordkey";
    public static String BASE_URL="http://192.168.100.7:7000";

    private static final String TAG = "TUDOAPP";
    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<TaskDTO> li;
    //RetroRecycleAdapter retroRecycleAdapter;
    RetroRecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        fab = findViewById(R.id.floating);
        Date d = Calendar.getInstance().getTime();
        //Log.d(TAG, "My messsage" + d.toString());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(d);
        TextView day = findViewById(R.id.tvdateShow);
        day.setText(formattedDate);


        SimpleDateFormat s2 = new SimpleDateFormat("EEEE");
        Date d2 = new Date();
        String dayOfTheWeek = s2.format(d2);
        TextView week = findViewById(R.id.txtday);
        week.setText(dayOfTheWeek);

        NavigationView nv = findViewById(R.id.navi_view);
        ImageButton ib = findViewById(R.id.ibnav);
        final DrawerLayout dl = findViewById(R.id.dllayouts);

        li = new ArrayList<TaskDTO>();

        recyclerView = findViewById(R.id.re);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RetroRecycleAdapter(this,li);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedpreferences;
        if (li.isEmpty()) {
            new HttpGet().execute();
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                //Intent ins = new Intent(getApplicationContext(), FirstActivity.class);
          //      Log.d(TAG, "onNavigationItemSelected: true");

                SharedPreferences preferences =getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

                String tokenvalue=preferences.getString("tokenkey","");
                SharedPreferences.Editor editor = preferences.edit();
                Log.d(TAG, "onNavigationItemSelected: "+tokenvalue);

                editor.clear();
                editor.commit();
                //finish();
                Intent navigateToNextActivity=new Intent();
                navigateToNextActivity.setClass(FirstActivity.this,LoginActivity.class);
                navigateToNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(navigateToNextActivity);

                return true;

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(i);
            }


        });

        RecyclerView recyclerView = findViewById(R.id.re);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ServerResponseAdapter recyclerAdapter = new ServerResponseAdapter(this, li);
        recyclerView.setAdapter(recyclerAdapter);

    }


    public class HttpGet extends AsyncTask<Void,Void,Void>
        {
            public static final String MyPREFERENCES = "MyPrefs" ;
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    URL url = new URL(BASE_URL);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    String token = getToken();
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    String tokenvalue= sharedpreferences.getString("tokenkey","");
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    //String token = preferences.getString("token","");
                    Log.d(TAG, "ttt: " + tokenvalue);
                    con.setRequestProperty("Authorization",tokenvalue);
                    int responsecode=con.getResponseCode();
                    System.out.println("response code is"+responsecode);
                    Log.d(TAG, "responsecode "+responsecode);
                    con.connect();

                   // con.setRequestProperty("Authorization",token);

                    BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuffer response= new StringBuffer();
                    String container="";
                    while((container=br.readLine())!=null)
                    {
                        response.append(container);
                    }
                    br.close();
                    System.out.println(response);
                    Log.d(TAG, "response: "+response);
                    Wrapper task=new Gson().fromJson(response.toString(),Wrapper.class);
                    System.out.println(task.toString());
                    Log.d(TAG, "doInBackground: " + task.getList().get(0).getSubject());
                    li.addAll(task.getList());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "result "+task);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                return null;
            }
            public String getToken() {

                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                String tokenvalue= sharedpreferences.getString("tokenkey","");
                Log.d(TAG, "getToken: "+tokenvalue);

                return  tokenvalue;


            }
        }

}


