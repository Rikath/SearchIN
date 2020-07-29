package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Creates a simple date [YYYY-MM-dd-hh_mm_ss] to be used for file naming
 */
public final class SimpleDateFormatter {

    private static Date now;

    public SimpleDateFormatter () {
        now = new Date();
    }

    public static String getSimpleDate () {
        now = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh_mm_ss");
        return formatter.format(now);
    }
}
