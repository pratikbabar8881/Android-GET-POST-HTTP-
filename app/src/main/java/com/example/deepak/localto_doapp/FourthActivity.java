package com.example.deepak.localto_doapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FourthActivity extends AppCompatActivity
{
    private static final String TAG="todoapp";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        final EditText subject,date,auto_increment_id,done;
        Button update;


        subject=findViewById(R.id.etUpdateSubject);
        date=findViewById(R.id.UpdateDate);
        auto_increment_id=findViewById(R.id.etUpdateId);
        done=findViewById(R.id.UpdateDone);
        update=findViewById(R.id.btnUpdate);

        /////////////////////////////


        /////////////////////////////
        /*update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub=subject.getText().toString();
                int id = Integer.parseInt(auto_increment_id.getText().toString());
                String doing=done.getText().toString();
                String date1=date.getText().toString();


                TaskDTO details=new TaskDTO(sub,date1,id,doing);
                *//*JsonArray js=new JsonArray();
                JsonObject jProduct=new JsonObject();*//*


                JSONArray jProducts = new JSONArray();
                JSONObject jProduct = new JSONObject();

                    try {
                        jProduct.put("subject", details.subject);
                        jProduct.put("date", details.date);
                        jProduct.put("auto_increment_id", details.auto_increment_id);
                        jProduct.put("done", details.done);
                        jProducts.put(jProduct);

                         String jsonData = jProduct.toString();


                new sendPut().execute(jsonData);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        });
*/
    }

    class sendPut extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... params) {

            try {
                String jsonData = params[0];
                String BASE_URl = "http://192.168.100.21:8000/237/";
                Log.d(TAG, "doInBackground: ");
                URL url = new URL(BASE_URl);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                //conn.connect();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //int responseCode=conn.getResponseCode();
                Log.d(TAG, "doInBackground: ");


                System.out.println("Your enter url is"+url);
                //System.out.println("Your Enter response code is"+responseCode);

                //sending data

                OutputStream os=conn.getOutputStream();
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

                Log.d(TAG, "send data: ");
                is.close();
                os.close();
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

    }

}
