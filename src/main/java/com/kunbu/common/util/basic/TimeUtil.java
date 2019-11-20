package com.kunbu.common.util.basic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期时间工具类
 * <p>
 * <br/> 1.Calendar 的 month 从 0 开始，也就是全年 12 个月由 0 ~ 11 进行表示
 * <br/> 2.为了复用calendar，作为参数传入，但多次操作会导致时间错乱，所以需要在每次用完后重置
 * <br/> 3.Calendar.DAY_OF_MONTH 等价于 Calendar.DATE
 * <br/> 4.获取当前用get，获取最大用getActualMaximum
 *
 * @author scosyf
 */
public class TimeUtil {

    /**
     * 获取月份天数
     * <p> year, month不为空：获取指定月份天数
     * <p> year, month为空：获取当前月份现有天数
     *
     * @param calendar
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDayCount(Calendar calendar, Integer year, Integer month) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        int days;
        if (year != null && month != null) {
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.YEAR, year);
            days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } else {
            days = calendar.get(Calendar.DAY_OF_MONTH);
        }
        calendar.setTime(new Date());
        return days;
    }

    /**
     * 获取指定月份第一天0点（不传year和month获取当前月第一天0点）
     *
     * @param calendar
     * @param year
     * @param month
     * @return
     */
    public static Date getMonthBegin(Calendar calendar, Integer year, Integer month) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        if (year != null && month != null) {
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.YEAR, year);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date begin = calendar.getTime();
        calendar.setTime(new Date());
        return begin;
    }

    /**
     * 获取指定月份最后一天24点（不传year和month获取当天24点）
     *
     * @param calendar
     * @param year
     * @param month
     * @return
     */
    public static Date getMonthEnd(Calendar calendar, Integer year, Integer month) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        if (year != null && month != null) {
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else {
            //get current day of month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        Date end = calendar.getTime();
        calendar.setTime(new Date());
        return end;
    }

    /**
     * 按月查询，获取指定月份间隔
     *
     * @param beginYear
     * @param beginMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static List<Date> getBetweenMonth(Calendar calendar, int beginYear, int beginMonth, int endYear, int endMonth) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        List<Date> dates = new ArrayList<Date>(2);

        calendar.set(Calendar.YEAR, beginYear);
        calendar.set(Calendar.MONTH, beginMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dates.add(calendar.getTime());

        calendar.set(Calendar.YEAR, endYear);
        calendar.set(Calendar.MONTH, endMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        dates.add(calendar.getTime());

        calendar.setTime(new Date());
        return dates;
    }

    /**
     * 获取一天剩余毫秒数（或已用毫秒数）
     *
     * @param nowTime
     * @param left    是否剩余时间
     * @return
     */
    public static long getDayLeftMillions(Date nowTime, boolean left) {
        long start = nowTime.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 000);
        if (left) {
            calendar.add(Calendar.DATE, 1);
            return calendar.getTimeInMillis() - start;
        } else {
            return start - calendar.getTimeInMillis();
        }

    }

    /**
     * 获取当天0点
     *
     * @param calendar
     * @return
     */
    public static Date getDayZero(Calendar calendar, Date date) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date zero = calendar.getTime();
        calendar.setTime(new Date());
        return zero;
    }

    /**
     * 展示calendar的当前时间信息
     */
    private static void showNowTimeInfo() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("current time: " + calendar.getTime());
        System.out.println("current year: " + calendar.get(Calendar.YEAR));
        System.out.println("currnet month: " + (calendar.get(Calendar.MONTH) + 1));
        System.out.println("current day: " + calendar.get(Calendar.DATE));
        System.out.println("day of the month: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("max day of the month: " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();

        System.out.println("---------------- getMonthDayCount -----------------");
        System.out.println("2017-12 >>> " + getMonthDayCount(calendar, 2017, 12));
        System.out.println("2018-06 >>> " + getMonthDayCount(calendar, 2018, 6));
        System.out.println("current >>> " + getMonthDayCount(calendar, null, null));
        System.out.println("2019-01 >>> " + getMonthDayCount(calendar, 2019, 1));

        System.out.println("---------------- getMonthBegin -----------------");
        System.out.println("2017-09 >>> " + getMonthBegin(calendar, 2017, 9));
        System.out.println("2018-06 >>> " + getMonthBegin(calendar, 2018, 6));
        System.out.println("current >>> " + getMonthBegin(calendar, null, null));
        System.out.println("2019-01 >>> " + getMonthBegin(calendar, 2019, 1));

        System.out.println("-----------------getMonthEnd---------------------");
        System.out.println("2017-09: " + getMonthEnd(calendar, 2017, 9));
        System.out.println("2018-06: " + getMonthEnd(calendar, 2018, 6));
        System.out.println("current: " + getMonthEnd(calendar, null, null));
        System.out.println("2019-01: " + getMonthEnd(calendar, 2019, 1));

        System.out.println("-----------------getBetweenMonth-------------------");
        System.out.println("2018-02 >>> 2019-05: " + getBetweenMonth(calendar, 2018, 2, 2019, 5));

        System.out.println("-----------------getDayLeftMillions-------------------");
        System.out.println("before: " + getDayLeftMillions(new Date(), false));
        System.out.println("left: " + getDayLeftMillions(new Date(), true));

        System.out.println("-----------------getDayZero-------------------");
        System.out.println("current: " + getDayZero(calendar, new Date()));

        System.out.println("-----------------showNowTimeInfo----------------------");
        showNowTimeInfo();
    }

    public static Date plusHours(Date date, int hours) {
        long time = date.getTime() + hours * 3600 * 1000L;
        return new Date(time);
    }
}
