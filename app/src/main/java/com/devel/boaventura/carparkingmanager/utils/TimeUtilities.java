package com.devel.boaventura.carparkingmanager.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by scavenger on 4/20/18.
 */

public class TimeUtilities {
    public static final String FORMAT_HOUR_MINUTES_SECONDS = "HH:mm:ss";
    public static final String FORMAT_HOUR_MINUTES = "mm";
    public static final String FORMAT_DAY_MONTH_YEAR = "dd/MM/yyyy";

    public static final long SECONDS_IN_MILLI = 1000;
    public static final long MINUTES_IN_MILLI = SECONDS_IN_MILLI * 60;
    public static final long HOURS_IN_MILLI = MINUTES_IN_MILLI * 60;
    public static final long DAYS_IN_MILLI = HOURS_IN_MILLI * 24;

    public static final long DAY_IN_MINUTES = 24 * 60;
    public static final String FORMAT_DATE_AND_TIME = FORMAT_DAY_MONTH_YEAR
            + " " + TimeUtilities.FORMAT_HOUR_MINUTES_SECONDS;

    public static final DateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }


    public static void printDifference(long startDate, long endDate) {

        //milliseconds
        long different = endDate - startDate;

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / DAYS_IN_MILLI;
        different = different % DAYS_IN_MILLI;

        long elapsedHours = different / HOURS_IN_MILLI;
        different = different % HOURS_IN_MILLI;

        long elapsedMinutes = different / MINUTES_IN_MILLI;
        different = different % MINUTES_IN_MILLI;

        long elapsedSeconds = different / SECONDS_IN_MILLI;

        Log.i("DBG", elapsedDays + " Days "
                + elapsedHours + " Horas " +
                +elapsedMinutes + " Minutos " +
                +elapsedSeconds + " segundos ");
    }

    public static long getDaysElapsed(long startTime, long endTime) {
        long diference = endTime - startTime;
        return diference / DAYS_IN_MILLI;
    }

    public static long getHoursElapsed(long startTime, long endTime) {
        long diference = endTime - startTime;
        diference = diference % DAYS_IN_MILLI;
        return diference / HOURS_IN_MILLI;
    }

    public static long getMinutesElapsed(long startTime, long endTime) {
        long diference = endTime - startTime;
        diference = diference % DAYS_IN_MILLI;
        diference = diference % HOURS_IN_MILLI;
        return diference / MINUTES_IN_MILLI;
    }


    public static class ElapsedTime {
        private long m_days, m_hours, m_minutes, m_seconds, m_diferrence;

        public ElapsedTime(long startTime, long endTime) {
            splitTimeUnits(startTime, endTime);
        }

        public long getDays() {
            return m_days;
        }

        public long getHours() {
            return m_hours;
        }

        public long getMinutes() {
            return m_minutes;
        }

        public long getSeconds() {
            return m_seconds;
        }


        public long getDaysInHours(){
            return (m_days * 24);
        }

        public long getDaysInMinutes(){
            return (m_days * 24 * 60);
        }


        public long getTimeDiference(){
            return m_diferrence;
        }

        private void splitTimeUnits(long startDate, long endDate) {

            long different = endDate - startDate;
            m_diferrence = different;

            m_days = different / DAYS_IN_MILLI;
            different = different % DAYS_IN_MILLI;

            m_hours = different / HOURS_IN_MILLI;
            different = different % HOURS_IN_MILLI;

            m_minutes = different / MINUTES_IN_MILLI;
            different = different % MINUTES_IN_MILLI;

            m_seconds = different / SECONDS_IN_MILLI;
        }
    }

}
