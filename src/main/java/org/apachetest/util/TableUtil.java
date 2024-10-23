package org.apachetest.util;

import java.io.Serializable;

import java.util.Locale;
import org.threeten.bp.*;
import org.threeten.bp.format.DateTimeFormatter;

public class TableUtil implements Serializable {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.of("UTC"));

    public String getCurrentTS(){
        return dateTimeFormatter.format(Instant.now());
    }
}
