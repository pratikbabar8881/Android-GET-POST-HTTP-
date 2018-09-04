package com.example.deepak.localto_doapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {
    TextView  date;
    List<TaskDTO> list;
    RetroRecycleAdapter adapter;
    ServerResponseAdapterThird responseAdapter1;
    RecyclerView recyclerView;
    private static final String TAG = "TUDOAPP";
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        date = findViewById(R.id.TVdateShow);
        Date d1= Calendar.getInstance().getTime();
        SimpleDateFormat s2=new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate=s2.format(d1);
        final TextView date=findViewById(R.id.TVdateShow);
        date.setText(formattedDate);

        SimpleDateFormat s3 = new SimpleDateFormat("EEEE");
        Date d2 = new Date();
        String dayOfTheWeek = s3.format(d2);
        TextView week = findViewById(R.id.Txtday);
        week.setText(dayOfTheWeek);

        list = new ArrayList<TaskDTO>();

        recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RetroRecycleAdapter(this,list);
        recyclerView.setAdapter(adapter);

        if (list.isEmpty()) {
            new HttpGet().execute();
        }




        //    DatabaseHandler db = new DatabaseHandler(this);
//        List<ToDoList> li = db.display();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ServerResponseAdapterThird recyclerAdapter = new ServerResponseAdapterThird(this,list);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public class HttpGet extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //String BASE_URl = "https://demo-todo-rest.herokuapp.com";
                URL url = new URL(FirstActivity.BASE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                //int responseCode=conn.getResponseCode();
                conn.setRequestMethod("GET");
                String token=getToken();
                Log.d(TAG, "TokenValue "+token);
                conn.setRequestProperty("Authorization",token);
                //System.out.println("Your response code is"+responseCode);
                System.out.println("your url is"+ url);

                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response=new StringBuffer();
                String Container="";
                while((Container=br.readLine())!=null)
                {
                    response.append(Container);

                }
                System.out.println(response);
                Wrapper task=new Gson().fromJson(response.toString(),Wrapper.class);
                System.out.println(task);
                list.addAll(task.getList());
                adapter.notifyDataSetChanged();
                System.out.println("Your Date is:"+task.getList());
                Log.d(TAG, "List "+task.getList());


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        public String getToken() {

            SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            String tokenvalue= sharedpreferences.getString("tokenkey","");
            return  tokenvalue;


        }
    }
}


