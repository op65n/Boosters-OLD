package com.splicegames.sgboosters.util.time;

@SuppressWarnings("unused")
public final class TimeDisplay {

    private static final long ONE_MILLISECOND = 1L;
    private static final long MILLISECONDS_IN_A_SECOND = 1000L;
    private static final long ONE_SECOND = 1000L;
    private static final long SECONDS_IN_A_MINUTE = 60L;
    private static final long MINUTES_IN_AN_HOUR = 60L;
    private static final long HOURS_IN_A_DAY = 24L;
    private static final long DAYS_IN_A_YEAR = 365L;

    public static String getFormattedTime(long duration) {
        final StringBuilder result = new StringBuilder();

        duration *= 1000L;
        final long milliseconds = (duration /= ONE_MILLISECOND) % MILLISECONDS_IN_A_SECOND;
        final long seconds = (duration /= ONE_SECOND) % SECONDS_IN_A_MINUTE;
        final long minutes = (duration /= SECONDS_IN_A_MINUTE) % MINUTES_IN_AN_HOUR;
        final long hours = (duration /= MINUTES_IN_AN_HOUR) % HOURS_IN_A_DAY;
        final long days = (duration /= HOURS_IN_A_DAY) % DAYS_IN_A_YEAR;
        final long years = (duration / DAYS_IN_A_YEAR);

        if (years != 0) result.append(String.format("%s %s ", years, years == 1 ? "year" : "years"));
        if (days != 0) result.append(String.format("%s %s ", days, days == 1 ? "day" : "days"));
        if (hours != 0) result.append(String.format("%s %s ", hours, hours == 1 ? "hour" : "hours"));
        if (minutes != 0) result.append(String.format("%s min ", minutes));
        if (seconds != 0) result.append(String.format("%s sec", seconds));

        return result.toString().trim();
    }

}
