package com.kunbu.common.util.basic;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author kunbu
 * @date 2021/2/22 12:06
 **/
public class DateLocalUtil {

    public static final String TIME_PATTERN = "HH:mm";

    public static void splitHourMin(String time) {
        String[] startEnd = time.split("-");

        try {
            System.out.println(new SimpleDateFormat(TIME_PATTERN).parse("20:30"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
//        LocalDateTime start = LocalDateTime.parse(startEnd[0], formatter);
//        LocalDateTime end = LocalDateTime.parse(startEnd[1], formatter);

//        System.out.println(start);
//        System.out.println(end);
    }


    public static void main(String[] args) {

        LocalDateTime startLocalTime = LocalDateTime.now();
        startLocalTime = startLocalTime.withHour(1);
        startLocalTime = startLocalTime.withMinute(2);
        LocalDateTime endLocalTime = LocalDateTime.now();
        endLocalTime = endLocalTime.withHour(3);
        endLocalTime = endLocalTime.withMinute(4);
        System.out.println(startLocalTime);
        System.out.println(endLocalTime);

        splitHourMin("22:30-06:30");


        List<LocalDateTime> dates = Lists.newArrayList();
        LocalDateTime current = LocalDateTime.now();
        for (int i = 0; i < 4; i++) {
            current = current.plusMinutes(60);
            dates.add(current);
        }
        System.out.println(dates);
    }
}
