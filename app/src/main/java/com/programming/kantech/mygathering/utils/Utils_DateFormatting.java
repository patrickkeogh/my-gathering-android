package com.programming.kantech.mygathering.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by patrick keogh on 2017-05-25.
 */

public class Utils_DateFormatting {

    public static String getFormattedGatheringDate(Date date_in) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date_in);


        String[] suffixes =
                //    0     1     2     3     4     5     6     7     8     9
                {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    10    11    12    13    14    15    16    17    18    19
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                        //    20    21    22    23    24    25    26    27    28    29
                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    30    31
                        "th", "st"};

        SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d");
        int day = Integer.parseInt(formatDayOfMonth.format(cal.getTime()));
        String strDay = day + suffixes[day];


        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEE");
        String strDayOfWeek = dayOfWeek.format(cal.getTime());

        SimpleDateFormat monthOfYear = new SimpleDateFormat("MMM");
        String strMonthOfYear = monthOfYear.format(cal.getTime());

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String strYear = year.format(cal.getTime());

        SimpleDateFormat time = new SimpleDateFormat("hh:mm");
        String strTime = time.format(cal.getTime());



        return strTime + " " + strDayOfWeek + " " + strMonthOfYear + " " + strDay + ", " + strYear;
    }

    public static String getFormattedLongDateStringFromLongDate(long dateIn) {

        String strTime;

        // create a calendar
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(dateIn);

        String[] suffixes =
                //    0     1     2     3     4     5     6     7     8     9
                {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    10    11    12    13    14    15    16    17    18    19
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                        //    20    21    22    23    24    25    26    27    28    29
                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    30    31
                        "th", "st"};

        SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d");
        int day = Integer.parseInt(formatDayOfMonth.format(cal.getTime()));
        String strDay = day + suffixes[day];


        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
        String strDayOfWeek = dayOfWeek.format(cal.getTime());

        SimpleDateFormat monthOfYear = new SimpleDateFormat("MMMM");
        String strMonthOfYear = monthOfYear.format(cal.getTime());

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String strYear = year.format(cal.getTime());


        return strDayOfWeek + " " + strMonthOfYear + " " + strDay + ", " + strYear;

    }



    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_DateFormatting() {
        throw new AssertionError();
    }
}
