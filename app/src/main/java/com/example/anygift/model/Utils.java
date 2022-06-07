package com.example.anygift.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    
    final static double earthRadius = 6371000; //meters

    public static Double CalculateDistance(String LatLon1, String LatLon2) {
        Double lat1 = Double.parseDouble(LatLon1.split(",")[0]);
        Double lon1 = Double.parseDouble(LatLon1.split(",")[1]);
        Double lat2 = Double.parseDouble(LatLon2.split(",")[0]);
        Double lon2 = Double.parseDouble(LatLon2.split(",")[1]);
        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance =  Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos( Math.toRadians(lat1)) * Math.cos( Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = earthRadius * c;
        return distance;
    }
    public static int MinDistance(String[] locations, String latLonUser ){
        int index=0;
        double min=CalculateDistance(locations[0],latLonUser);
        for (int i=1;i<locations.length;i++) {
            double temp=CalculateDistance(locations[i],latLonUser);
            if(temp<min){
                min=temp;
                index=i;
            }
        }
        return index;
    }

}
