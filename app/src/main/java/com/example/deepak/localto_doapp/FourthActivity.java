package com.example.deepak.localto_doapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FourthActivity extends AppCompatActivity
{

     EditText subject,date,auto_increment_id,done;
    FloatingActionButton update;

    TextView dateView,dayView;
    private static final String TAG="todoapp";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);



        dateView=findViewById(R.id.tvdate);
        dayView=findViewById(R.id.tvday1);

        Date mydate=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
        String formatedDate2=sdf.format(mydate);
        dateView.setText(formatedDate2);


        subject=findViewById(R.id.etUpdateSubject);
        date=findViewById(R.id.UpdateDate);
        date.setClickable(true);
       // auto_increment_id=findViewById(R.id.etUpdateId);
         //done=findViewById(R.id.UpdateDone);
        update=findViewById(R.id.btnUpdate);

        Intent in=getIntent();
        String subjectUpdate = in.getExtras().getString("subject");
        String dateUpdte=in.getExtras().getString("date");
        subject.setText(subjectUpdate);
        subject.setSelection(subject.getText().length());
        date.setText(dateUpdte);

        Date mydate2=Calendar.getInstance().getTime();
        SimpleDateFormat sdf1=new SimpleDateFormat("EEEE");
        String formatedDay=sdf1.format(mydate2);
        dayView.setText(formatedDay);


        date.setShowSoftInputOnFocus(false);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        /////////////////////////////

        ImageButton ib1=findViewById(R.id.ib_datePicker);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();
            }
        });


        /////////////////////////////
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub = subject.getText().toString();
                //       int id = Integer.parseInt(auto_increment_id.getText().toString());
                //String doing=done.getText().toString();
                String date1 = date.getText().toString();

                if (sub.equals("")) {

                    Toast.makeText(FourthActivity.this, "Enter invalid Subject and Date", Toast.LENGTH_LONG).show();
                } else {
                    TaskUpdate details = new TaskUpdate(sub, date1);
                    JsonArray js = new JsonArray();
                    JsonObject jProduct = new JsonObject();


                    JSONArray jProducts = new JSONArray();
                    JSONObject jProduct1 = new JSONObject();

                    try {
                        jProduct1.put("subject", details.subject);
                        jProduct1.put("date", details.date);
                        jProducts.put(jProduct1);

                        String jsonData = jProduct1.toString();
                        new sendPut().execute(jsonData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent in = new Intent(FourthActivity.this, FirstActivity.class);
                    startActivity(in);

                }
            }
        });

    }

    public static void hideSoftKeyboard1(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void getDate()
    {

        final Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dp=new DatePickerDialog(FourthActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date.setText( i2 + "/" + (i1+1) + "/" + i);
            }
        },year,month,day);
        dp.show();
    }

    class sendPut extends AsyncTask<String,Void,Void>
    {

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");

        @Override
        protected Void doInBackground(String... params) {



            try {
                String jsonData = params[0];
                String BASE_URl = "http://192.168.100.7:7000";
                Log.d(TAG, "doInBackground: ");
                URL url = new URL(String.format("%s/%s/",BASE_URl,id));
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                //conn.connect();

                /////////////////////////

                SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
               String token= getToken();

                Log.d(TAG, "doInBackground: "+token);

                ////////////////////////
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               conn.setRequestProperty("Authorization",token);
                conn.setDoInput(true);
                conn.setDoOutput(true);


                //int responseCode=conn.getResponseCode();
                Log.d(TAG, "doInBackground: ");


                System.out.println("Your enter url is"+url);
                //System.out.println("Your Enter response code is"+responseCode);


                    OutputStream os = conn.getOutputStream();
                    Log.d(TAG, "os "+os);
                    os.write(jsonData.getBytes());
                    Log.d(TAG, "doInBackground: ");



                //receives data
               /* InputStream is = conn.getInputStream();
                String result = "";
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }
                Log.d("json api", "DoUpdateProduct.doInBackground Json return: " + result);

                Log.d(TAG, "send data: "+result);
                is.close();
                //os.close();
                conn.disconnect();*/

               //recieves data
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response=new StringBuffer();
                String container="";
                while ((container=br.readLine())!=null)
                {
                    response.append(container);
                }
                System.out.println("Your response is "+response);
                conn.disconnect();
                br.close();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        public String getToken() {

            SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            String tokenvalue= sharedpreferences.getString("tokenkey","");
            Log.d(TAG, "getToken: "+tokenvalue);
            return  tokenvalue;


        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


}
