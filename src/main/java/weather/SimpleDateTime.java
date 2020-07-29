package weather;

/**
 * This class is implemented to convert Unix timestamps to more familiar forms of date and time representations.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SimpleDateTime {


    /**
     * Method to convert Unix timestamp. Also converts time from UTC to location's time zone.
     * Parameters obtained from JSON response from OpenWeatherMap One Call API
     * @param unixDateTime The date and time in Unix timestamp format.
     * @param offset The location's timezone offset from UTC time. Used to obtain the current time of location being looked up.
     * @return The date and time formatted as "MM/dd/yyyy 'at' hh:mm a"
     */
    public static String convertToDateTime(long unixDateTime, long offset) {

        Date date = new Date((unixDateTime+offset)*1000L);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); //Set default to UTC and got the seconds offset from OpenWeatherMap in order to display the current time according to the time zone of specific location
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm a");
        return sdf.format(date);
    }

    /**
     * Method to convert Unix timestamp. Ignores time.
     * Parameter obtained from JSON response from OpenWeatherMap One Call API
     * @param unixDateTime The date and time in Unix timestamp format.
     * @return The date and time formatted as "(day of the week), MM/dd/yyyy"
     */
    public static String convertToDate(long unixDateTime) {

        Date date = new Date(unixDateTime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("E, MM/dd/yyyy");
        return sdf.format(date);
    }

}
