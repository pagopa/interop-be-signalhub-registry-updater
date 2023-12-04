package it.pagopa.interop.signalhub.updater.utility;

public class CacheUtils {
    private static final String KEY_SEPARATOR= "-";


    private CacheUtils() {}

    public static String getCacheKey(String eServiceId, String consumerId) {
        return eServiceId.concat(KEY_SEPARATOR).concat(consumerId);
    }
}