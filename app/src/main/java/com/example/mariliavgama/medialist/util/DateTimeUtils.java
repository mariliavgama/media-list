package com.example.mariliavgama.medialist.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateTimeUtils {
    public static String getDate(String date) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            Log.e(DateTimeUtils.class.getSimpleName(), "Error parsing date. " + e);
        }
        return "";
    }
}
