package com.example.deepak.localto_doapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG ="todoapp" ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    EditText subject;
    TextView  date1;
    EditText datepicker;
    EditText dob;
    EditText cal1;
    Calendar currentdate;
    int Year_x, day_x, month_x;


    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        subject = findViewById(R.id.subject);
        date1 = findViewById(R.id.tvday1);
        Date d1=Calendar.getInstance().getTime();
        SimpleDateFormat s2=new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate=s2.format(d1);
        final TextView date=findViewById(R.id.tvdate);
        date.setText(formattedDate);

        datepicker=findViewById(R.id.et_date);
        datepicker.setClickable(false);
        datepicker.setShowSoftInputOnFocus(false);

        datepicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(SecondActivity.this);
                return true;
            }
        });

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp=new DatePickerDialog(SecondActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        datepicker.setText( day + "/" + (month+1) + "/" + year);
                    }
                },year,month,day);
                dp.show();

            }
        });


        Date d2=Calendar.getInstance().getTime();
        SimpleDateFormat s3=new SimpleDateFormat("EEEE");
        String s4=s3.format(d2);
        TextView day=findViewById(R.id.tvday1);
        day.setText(s4);

        ImageButton ib1=findViewById(R.id.ibcal);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dp=new DatePickerDialog(SecondActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        datepicker.setText( i2 + "/" + (i1+1) + "/" + i);
                    }
                },year,month,day);
                dp.show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_check);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {

                                        try {
                                            POST();
                                            Toast.makeText(SecondActivity.this,"data stored",Toast.LENGTH_SHORT).show();
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                   }
                               });


                //Intent in=new Intent(SecondActivity.this,FirstActivity.class);
                //startActivity(in);



        ////////////////////////////////////////////////////////////////////////////////////////////
        /*Update here */


    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void POST()
    {

        new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {

                    String BASE_URL = "http://192.168.100.7:8000";
                    URL url = new URL(BASE_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");

                    String token = getToken();
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    String tokenvalue= sharedpreferences.getString("tokenkey","");
                    Log.d(TAG, "run: ");

                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setRequestProperty("Authorization",tokenvalue);
                    conn.getURL();
                    System.out.println(conn.getURL());
                    conn.setDoOutput(true);

                    String data2="{\"subject\":\"" + subject.getText().toString()+"\" ,\"date\":\""+datepicker.getText().toString()+"\"}";
                    //String data1 = "{\"subject\":\"" + subject.getText().toString() + "\",\"date\":\"" + datepicker.getText().toString() + "}";
                    byte[] bobj = data2.getBytes(StandardCharsets.UTF_8);
                    OutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.write(bobj);
                    Log.d("TAg", "run: ");

                    int responseCode = conn.getResponseCode();
                    System.out.println("your Responsecode is:" + responseCode);
                    System.out.println("Your enter url is:" + url);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String container;
                    StringBuffer response = new StringBuffer();
                    if (responseCode == 200) {
                        while ((container = br.readLine()) != null) {
                            response.append(container);
                            Log.d("TAG", "run: ");
                        }

                        System.out.println(response);
                    } else {
                        System.out.println("connection failed");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                }).start();

              Intent in=new Intent(SecondActivity.this,FirstActivity.class);
              startActivity(in);




}
    public String getToken() {

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String tokenvalue= sharedpreferences.getString("tokenkey","");
        return  tokenvalue;


    }

}


               /* try {
                    if (subject.getText().toString() != null || date.getText().toString() != null) {
                        Intent in = new Intent(SecondActivity.this, FirstActivity.class);


                                           Log.d("TAG", "onClick: " + subject.getText().toString() + "" + date2.getText().toString());
                        final DatabaseHandler databaseHandler = new DatabaseHandler(SecondActivity.this);
                        //databaseHandler.addtodo(new ToDoRetro(subject.getText().toString(), date2.getText().toString()));
                        startActivity(in);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }*/








