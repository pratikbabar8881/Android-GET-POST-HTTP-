package com.example.deepak.localto_doapp;

public class TaskDTO
{
    String subject;
    String date;
    int auto_increment_id;
    String done;


    public TaskDTO(String subject, String date, int auto_increment_id, String done) {
        this.subject = subject;
        this.date = date;
        this.auto_increment_id = auto_increment_id;
        this.done = done;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAuto_increment_id() {
        return auto_increment_id;
    }

    public void setAuto_increment_id(int auto_increment_id) {
        this.auto_increment_id = auto_increment_id;
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
                ", auto_increment_id=" + auto_increment_id +
                ", done='" + done + '\'' +
                '}';
    }
}
