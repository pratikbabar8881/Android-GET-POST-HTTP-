package com.example.deepak.localto_doapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommanUtil
{

    public static String getDate(String date)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
        Date newdate=null;
        try {
            newdate = sdf.parse(date);

        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        sdf=new SimpleDateFormat("dd/mm/yyyy");

        return sdf.format(newdate);
    }

}
