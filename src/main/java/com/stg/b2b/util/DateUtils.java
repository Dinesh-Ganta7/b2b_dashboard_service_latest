package com.stg.b2b.util;



import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;



public class DateUtils {


    private DateUtils(){

    }

    public static Date formatDate(String dateString ,String pattern){
        java.sql.Date sqlDate=null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            java.util.Date utilDate = sdf.parse(dateString);
            sqlDate= new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sqlDate;
    }

    public static int currentMonthNumber(){
        return  LocalDate.now().getMonthValue();
    }

    public static int getWeekNumber(LocalDate date){
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), (date.getMonthValue() - 1)  , date.getDayOfMonth());
        return  cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeekDayNumber(LocalDate date){
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), (date.getMonthValue() - 1)  , date.getDayOfMonth());
        return cal.get(Calendar.DAY_OF_WEEK);
    }


}
