package it.pagopa.interop.signalhub.updater.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    private static final String KEY_SEPARATOR= "-";
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    private Utils() {}

    public static String getCacheKey(String eServiceId, String consumerId) {
        return eServiceId.concat(KEY_SEPARATOR).concat(consumerId);
    }

    public static String getFormatHour(Instant instant){
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, UTC_ZONE);
        return dateTimeFormatter.format(zonedDateTime);
    }
}