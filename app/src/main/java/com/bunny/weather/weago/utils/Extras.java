package com.bunny.weather.weago.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Extras {

    public static SpannableString formatDateString(String dateString) {
        try {
            // Create a DateTimeFormatter that can handle both "HH:mm" and "H:mm"
            DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .optionalStart()
                    .appendPattern(" HH:mm")
                    .optionalEnd()
                    .optionalStart()
                    .appendPattern(" H:mm")
                    .optionalEnd()
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .toFormatter();

            // Parse the original date string into a LocalDateTime object
            LocalDateTime dateTime = LocalDateTime.parse(dateString, inputFormatter);

            // Format the date into the desired string format
            String day = String.valueOf(dateTime.getDayOfMonth());
            String monthYear = dateTime.format(DateTimeFormatter.ofPattern("MMMM, yyyy"));
            String daySuffix = getDaySuffix(dateTime.getDayOfMonth());

            String fullDate = day + daySuffix + " " + monthYear;

            // Create a SpannableString to apply superscript
            SpannableString spannableString = new SpannableString(fullDate);
            int suffixStart = day.length();
            int suffixEnd = suffixStart + daySuffix.length();

            // Apply SuperscriptSpan and RelativeSizeSpan to the suffix
            spannableString.setSpan(new SuperscriptSpan(), suffixStart, suffixEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(0.75f), suffixStart, suffixEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        } catch (DateTimeParseException e) {
            Log.e("formatDateStringParseException", e.getParsedString());
            return null; // or handle the error as needed
        }
    }

    private static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getTimeFromString(String dateTimeString) {
        try {
            // Parse the original date-time string into a LocalDateTime object
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            // Format the LocalDateTime object to get the time
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            Log.e("getTimeFromStringParseException",e.toString());
            return null; // or handle the error as needed
        }
    }

    public static String formatDateStringType2(String originalDate) {
        // Define the input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());

        try {
            // Parse the original date string into a Date object
            Date date = inputFormat.parse(originalDate);

            // Format the Date object into the desired string format
            return outputFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            Log.e("formatDateStringType2ParseException",e.toString());
            return originalDate; // Return the original date string in case of error
        }
    }

}
