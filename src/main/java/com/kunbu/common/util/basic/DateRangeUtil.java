package com.kunbu.common.util.basic;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author kunbu
 * @date 2021/1/14 13:56
 **/
public class DateRangeUtil {

    public static final String RANGE_FORMAT_MONTH = "yyyy-MM";

    public static final String RANGE_FORMAT_DAY = "yyyy-MM-dd";

    public static void recent12month() {
        String currentYM = new SimpleDateFormat(RANGE_FORMAT_MONTH).format(new Date());
        System.out.println(currentYM);

        List<String> monthList = Lists.newArrayList();

        Calendar calendar = Calendar.getInstance();
        int currMonth = calendar.get(Calendar.MONTH) + 1;
        int currYear = calendar.get(Calendar.YEAR);
        if (currMonth < 12) {
            int tempYear = currYear - 1;
            for (int i = currMonth + 1; i <= 12; i++) {
                if (i < 10) {
                    monthList.add(Integer.toString(tempYear) + "-0" +Integer.toString(i));
                } else {
                    monthList.add(Integer.toString(tempYear) + "-" +Integer.toString(i));
                }
            }
        }
        for (int i = 1; i <= currMonth; i++) {
            if (i < 10) {
                monthList.add(Integer.toString(currYear) + "-0" +Integer.toString(i));
            } else {
                monthList.add(Integer.toString(currYear) + "-" +Integer.toString(i));
            }
        }

        System.out.println(monthList);
    }

    /**
     * limitDay <= 30
     *
     *
     **/
    public static List<String> recentDayByLimit(Date date, int limitDay) {
        String currentYM = new SimpleDateFormat(RANGE_FORMAT_DAY).format(date);
        System.out.println(currentYM);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currMonth = calendar.get(Calendar.MONTH) + 1;
        int currYear = calendar.get(Calendar.YEAR);
        int currDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        System.out.println("currMonth:" + currMonth + ", currYear:" + currYear + ", currDay:" + currDay +  ", currDayOfYear:" + currDayOfYear);

        List<String> dayList = Lists.newArrayList();
        if (limitDay > currDayOfYear) {
            // 跨年
            int countBeforeDay = limitDay - currDayOfYear;
            int beforeYear = currYear - 1;
            for (int i = 31 - countBeforeDay + 1; i <= 31; i++) {
                dayList.add(yyDDmm(Integer.toString(beforeYear), "12", i));
            }
            for (int i = 1; i <= currDayOfYear; i++) {
                dayList.add(yyDDmm(Integer.toString(currYear), "01", i));
            }
        } else {
            String currYearStr = Integer.toString(currYear);
            String currMonthStr = appendZero(currMonth);
            if (currDay > limitDay) {
                // 同月
                for (int i = currDay - limitDay + 1; i <= currDay; i++) {
                    dayList.add(yyDDmm(currYearStr, currMonthStr, i));
                }
            } else {
                // 跨月
                String beforeMonthStr = appendZero(currMonth - 1);
                int countBeforeDay = limitDay - currDay;
                calendar.set(Calendar.MONTH, currMonth - 2);
                int beforeDayMax = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = beforeDayMax - countBeforeDay + 1; i <= beforeDayMax; i++) {
                    dayList.add(yyDDmm(currYearStr, beforeMonthStr, i));
                }
                for (int i = 1; i <= currDay; i++) {
                    dayList.add(yyDDmm(currYearStr, currMonthStr, i));
                }
            }
        }
        System.out.println(dayList);
        return dayList;
    }

    private static String appendZero(int num) {
        if (num < 10) {
            return "0" + num;
        } else {
            return Integer.toString(num);
        }
    }

    private static String yyDDmm(String year, String month, int day) {
        StringBuilder builder = new StringBuilder()
                .append(year)
                .append("-")
                .append(month)
                .append("-")
                .append(appendZero(day));
        return builder.toString();
    }


    public static void main(String[] args) throws ParseException {
        recent12month();

        recentDayByLimit(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-13"), 15);
    }

}
