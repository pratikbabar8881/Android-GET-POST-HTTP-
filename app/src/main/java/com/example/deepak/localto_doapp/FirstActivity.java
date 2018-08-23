package com.example.deepak.localto_doapp;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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


public class FirstActivity extends AppCompatActivity {
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
        Log.d(TAG, "My messsage" + d.toString());

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

        if (li.isEmpty()) {
            GET();
        }
        

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent ins = new Intent(FirstActivity.this, ThirdActivity.class);
                Log.d(TAG, "onNavigationItemSelected: true");
                startActivity(ins);

                return false;

            }
        });


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
    public void GET()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String BASE_URL="http://192.168.100.6:8000/";

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

}


