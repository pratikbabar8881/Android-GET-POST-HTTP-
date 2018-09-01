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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            new HttpGet().execute();
        }

        /*if(li!=null)
        {
            try {
                DELETE();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/




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
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
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


    //    DatabaseHandler db = new DatabaseHandler(this);
//        List<ToDoList> li = db.display();
        RecyclerView recyclerView = findViewById(R.id.re);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ServerResponseAdapter recyclerAdapter = new ServerResponseAdapter(this, li);
        recyclerView.setAdapter(recyclerAdapter);

    }
    /*public void GET()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String BASE_URL="http://192.168.100.21:8000";

                try {
                    URL url = new URL(BASE_URL);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    int responsecode=con.getResponseCode();
                    System.out.println("response code is "+responsecode);

                    BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuffer response= new StringBuffer();
                    String container="";
                    while((container=br.readLine())!=null)
                    {
                        response.append(container);
                    }
                    br.close();
                    System.out.println(response);
                    Wrapper task=new Gson().fromJson(response.toString(),Wrapper.class);
                    System.out.println(task.toString());
                    li.addAll(task.getList());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onCreate: ");
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                Log.d(TAG, "run: running");

            }
        }).start();
    }
*/
   /* public void DELETE()throws Exception
    {
        String BASE_URL="192.168.100.11/8000/{2}";
        URL url=new URL(BASE_URL);
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type","Application/Json; charset=UTF-8");
        conn.setRequestMethod("DELETE");

        int ResponseCode=conn.getResponseCode();
        System.out.println("Your ResponseCode is "+ResponseCode);
        System.out.println("Your Url is "+url);

        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer response=new StringBuffer();

        String Container;

        while((Container=br.readLine())!=null)
        {
            response.append(Container);
        }

        System.out.println(response);


    }
*/

        /*protected String doInBackground(String... strings) {

            GET();
            return li.toString();
        }
*/


        public class HttpGet extends AsyncTask<Void,Void,Void>
        {
            public static final String MyPREFERENCES = "MyPrefs" ;
            @Override
            protected Void doInBackground(Void... voids) {


                String BASE_URL="http://192.168.100.21:8000/login/";

                try {
                    URL url = new URL(BASE_URL);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    //Log.d(TAG, "doInBackground: " + getToken());
                    String token = getToken();
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    String tokenvalue= sharedpreferences.getString("tokenkey","");
                    //Log.d(TAG, "doInBackground: "+token);
                    //int responsecode=con.getResponseCode();
                    //System.out.println("response code is "+responsecode);
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    //String token = preferences.getString("token","");
                    Log.d(TAG, "ttt: " + tokenvalue);
                    con.setRequestProperty("Authorization",tokenvalue);

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
                   // JSONObject jsonObject = new JSONObject(response.toString());
                    //String token1 = jsonObject.getJSONObject("token").getString("token");
                    //System.out.println(token1);
                    //Log.d(TAG, "token: "+token1);

            //        Log.d(TAG, "onCreate: ");
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

              //  Log.d(TAG, "run: running");

                return null;
            }

            public String getToken() {

                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                String tokenvalue= sharedpreferences.getString("tokenkey","");
                return  tokenvalue;


            }
        }
}


