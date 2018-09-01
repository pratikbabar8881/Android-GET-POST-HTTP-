package com.example.deepak.localto_doapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class ServerResponseAdapterThird extends RecyclerView.Adapter<ServerResponseAdapterThird.UserViewHolder> {
    private List<TaskDTO> listuser;
    private Context context;

    public ServerResponseAdapterThird(Context context, List<TaskDTO> listuser) {
        this.listuser = listuser;
        this.context = context;
    }


    @Override
    public ServerResponseAdapterThird.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServerResponseAdapterThird.UserViewHolder holder, final int position) {
        if(context instanceof ThirdActivity) {
            if (listuser.get(position).getDone().equals("true")) {
                holder.subjectDisplay.setText(Html.fromHtml("" + listuser.get(position).getSubject()));
                holder.dateDisplay.setText(Html.fromHtml("" + listuser.get(position).getDate()));
                holder.cb.setChecked(true);
                holder.cb.setEnabled(false);
                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            builder.setTitle("Do you want to close the session").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //DatabaseHandler db = new DatabaseHandler(context);
                                    //db.delete(listuser.get(position).getSubject());
                                    //db.add_check(listuser.get(position).getSubject(), listuser.get(position).getDate());
                                    //listuser.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, listuser.size());

                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    holder.cb.setChecked(true);

                                }
                            }).show();
                        }
                    }
                });
            }
        }


    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView subjectDisplay,dateDisplay;
        CheckBox cb;



        UserViewHolder(View v) {
            super(v);
            subjectDisplay = v.findViewById(R.id.name1);
            dateDisplay = v.findViewById(R.id.date);
            cb = v.findViewById(R.id.checkbox_meat);


        }

    }
        public int getItemCount() {
            return listuser.size();
        }
    }


