package com.example.deepak.localto_doapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

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
import java.util.Calendar;

public class FourthActivity extends AppCompatActivity
{
    private static final String TAG="todoapp";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        //Log.d(TAG, "onCreate: " + id);
        final EditText subject,date,auto_increment_id,done;
        Button update;


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

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(date, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        /////////////////////////////

        ImageButton ib1=findViewById(R.id.ib_datePicker);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


        /////////////////////////////
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub=subject.getText().toString();
         //       int id = Integer.parseInt(auto_increment_id.getText().toString());
                //String doing=done.getText().toString();
                String date1=date.getText().toString();


                TaskUpdate details=new TaskUpdate(sub,date1);
                JsonArray js=new JsonArray();
                JsonObject jProduct=new JsonObject();


                JSONArray jProducts = new JSONArray();
                JSONObject jProduct1 = new JSONObject();

                    try {
                        jProduct1.put("subject", details.subject);
                        jProduct1.put("date", details.date);
                        jProducts.put(jProduct1);

                         String jsonData = jProduct1.toString();


                new sendPut().execute(jsonData);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Intent in= new Intent(FourthActivity.this,FirstActivity.class);
                    startActivity(in);
            }
        });

    }

    class sendPut extends AsyncTask<String,Void,Void>
    {

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");

        @Override
        protected Void doInBackground(String... params) {



            try {
                String jsonData = params[0];
                String BASE_URl = "http://192.168.100.7:8000";
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
                //System.out.println("response code is:"+responseCode);
                //conn.connect();



                //int responseCode=conn.getResponseCode();
                Log.d(TAG, "doInBackground: ");


                System.out.println("Your enter url is"+url);
                //System.out.println("Your Enter response code is"+responseCode);


                    OutputStream os = conn.getOutputStream();
                    Log.d(TAG, "os "+os);
                    os.write(jsonData.getBytes());
                    Log.d(TAG, "doInBackground: ");



                //receives data
                InputStream is = conn.getInputStream();
                String result = "";
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }
                Log.d("json api", "DoUpdateProduct.doInBackground Json return: " + result);

                Log.d(TAG, "send data: "+result);
                is.close();
                //os.close();
                conn.disconnect();



                /*BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response=new StringBuffer();
                String Container="";
                while((Container=br.readLine())!=null)
                {
                    response.append(Container);

                }
                System.out.println(response);*/

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

}
