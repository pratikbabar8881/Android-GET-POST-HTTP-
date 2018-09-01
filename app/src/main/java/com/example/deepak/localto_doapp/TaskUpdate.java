package com.example.deepak.localto_doapp;

public class TaskUpdate {
    String subject;
    String date;


    public TaskUpdate(String subject, String date) {
        this.subject = subject;
        this.date = date;
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

    @Override
    public String toString() {
        return "TaskUpdate{" +
                "subject='" + subject + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
