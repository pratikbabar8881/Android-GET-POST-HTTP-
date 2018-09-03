package com.example.deepak.localto_doapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskDTO
{
    String subject;
    String date;
    int task_id;
    String done;


    //public java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    public TaskDTO(String subject, String date, int task_id, String done) {
        this.subject = subject;
        this.date = date;
        this.task_id = task_id;
        this.done = done;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {

        //Date d1=Calendar.getInstance().getTime();
       // SimpleDateFormat df=new SimpleDateFormat("dd/mm/yyyy");
        //String date2=df.format(date);

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public int getTask_id() {
        return task_id;



    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;

    }

    public String getDone() {
        return done;
    }


    public void setDone(String done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "subject='" + subject + '\'' +
                ", date='" + date + '\'' +
                ", task_id=" + task_id +
                ", done='" + done + '\'' +
                '}';
    }
}
