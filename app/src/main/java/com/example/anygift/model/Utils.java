package com.example.anygift.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static boolean isDouble(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Long convertDateToLong(String day, String month, String year) {
        if (Integer.parseInt(day) < 10)
            day = "0" + day;
        if (Integer.parseInt(month) < 10)
            month = "0" + month;
        String d = year + ":" + month + ":" + day + " 00:00:00 GMT";
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss zzz");
        try {
            Date date = df.parse(d);
            long epoch = date.getTime();
            return epoch;
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
        return null;
    }

    public static String ConvertLongToDate(long l) {
        Date date = new Date(l);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/GMT"));
        return format.format(date).split(" ")[0];
    }
}
