package com.example.deepak.localto_doapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class ServerResponseAdapter extends RecyclerView.Adapter<ServerResponseAdapter.UserViewHolder> {

    //public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String TAG="todo";
    protected List<TaskDTO> users;
    protected Context context;



    ServerResponseAdapter(Context context, List<TaskDTO> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        if (context instanceof FirstActivity) {
            if (users.get(position).getDone().equals("false")) {
                holder.nameDisplay.setText(Html.fromHtml(" " + users.get(position).getSubject()));

               // SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                //String formattedDate1 = df.format(users.get(position).getDate());

                holder.classDisplay.setText(Html.fromHtml("" + CommanUtil.getDate(users.get(position).getDate())));

                /*holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Log.d("TAG", "onCheckedChanged: " + b);
                        if (b) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            builder.setTitle("Do you want to close").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    DatabaseHandler db = new DatabaseHandler(context);
                                    db.delete(users.get(position).getSubject());
                                    db.add_check(users.get(position).getSubject(), users.get(position).getDate());
                                    users.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, users.size());

                                }
                            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.cb.setChecked(false);
                                }
                            }).show();


                        }

                    }

                });*/


                holder.ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new DeleteRequest(context, users.get(position).getTask_id()).execute();
                        users.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                holder.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* AlertDialog alert = new AlertDialog.Builder(context).create();
                        alert.setMessage("Do you want to update");
                        alert.setCancelable(true);
                        alert.setButton(DialogInterface.BUTTON_POSITIVE,"Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent in=new Intent(context,FourthActivity.class);
                                context.startActivity(in);
                            }
                        });*/

                        /*alert.setButton(DialogInterface.BUTTON_NEGATIVE, "no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent in=new Intent(context,FirstActivity.class);
                                context.startActivity(in);

                            }
                        });*/

                        Intent in=new Intent(context,FourthActivity.class);
                        Log.d(TAG, "onClick: "+users.get(position).getTask_id());
                        String id_ = Integer.toString(users.get(position).getTask_id());
                        in.putExtra("id", id_);
                        String sunject_name=users.get(position).getSubject();
                        String date_value=CommanUtil.getDate(users.get(position).getDate());
                        in.putExtra("subject",sunject_name);
                        in.putExtra("date",date_value);
                        context.startActivity(in);

                    }
                });

            }
            /*else
            {
                users.remove(position);
            }*/
        }
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameDisplay, classDisplay;
        CheckBox cb;
        ImageButton ib,update;


        UserViewHolder(View v) {
            super(v);
            nameDisplay = v.findViewById(R.id.name1);
            classDisplay = v.findViewById(R.id.date);

           // cb = v.findViewById(R.id.checkbox_meat);
            ib=v.findViewById(R.id.ib_delete);
            update=v.findViewById(R.id.ib_update);


        }
    }

    public class DeleteRequest extends AsyncTask<Integer,Void,Void>
    {
        int deleteId;
        Context context;

        SharedPreferences sharedPreferences;
        DeleteRequest(Context context,int id)
        {
            this.context = context;
            this.deleteId = id;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            String BASE_URL="http://192.168.100.7:8000";

            try {
                URL url = new URL(String.format("%s/%d/",BASE_URL,deleteId));
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();


                conn.setRequestMethod("DELETE");
                String token=getToken();
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Authorization",token);


                //conn.connect();
                //int responseCode=conn.getResponseCode();

                System.out.println("Your Enter url is"+url);
                //System.out.println("Your Response code is"+responseCode);

                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response=new StringBuffer();
                String Container="";
                while((Container=br.readLine())!=null)
                {
                    response.append(Container);
                }
                System.out.println(response);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;

        }
        public  String getToken() {

            SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            String tokenvalue= sharedpreferences.getString("tokenkey","");
            return  tokenvalue;


        }

//        public String getToken() {
//
//            String tokenvalue=sharedPreferences.getString("tokenkey","");
//            return  tokenvalue;
//        }


    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
