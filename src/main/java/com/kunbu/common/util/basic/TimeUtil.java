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
 * <br/> 4.获取当前用get，获取最后用getActualMaximum
 *
 * @author scosyf
 */
public class TimeUtil {

    /**
     * 计算指定月份天数（不传year,和month默认获取当前月份现有天数）
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDayCount(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        int days;
        if (year != null && month != null) {
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.YEAR, year);
            days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } else {
            days = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return days;
    }


    /**
     * 计算指定日期0或24点（不传day默认获取当天）
     *
     * @param day
     * @param ifZero 是否计算0点
     * @return
     */
    public static Date getDayBeginOrEnd(Date day, boolean ifZero) {
        Calendar calendar = Calendar.getInstance();
        if (day != null) {
            calendar.setTime(day);
        }
        if (ifZero) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
        return calendar.getTime();
    }

    /**
     * 计算指定月份第一天0点或最后一天24点（不传year和month默认获取当前月）
     *
     * @param year
     * @param month
     * @param ifFirstDay 是否计算第一天
     * @return
     */
    public static Date getMonthBeginOrEnd(Integer year, Integer month, boolean ifFirstDay) {
        Calendar calendar = Calendar.getInstance();
        if (year != null && month != null) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
        }
        if (ifFirstDay) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            // 该月实际最后一天
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
        return calendar.getTime();
    }

    /**
     * 计算指定月份的首尾日期
     *
     * @param beginYear
     * @param beginMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static List<Date> getBeginEndBetweenMonths(int beginYear, int beginMonth, int endYear, int endMonth) {
        Calendar calendar = Calendar.getInstance();
        List<Date> dates = new ArrayList<>(2);

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

        return dates;
    }

    /**
     * 计算指定日期一天剩余毫秒数（或已用毫秒数）
     *
     * @param day
     * @param ifLeft    是否计算剩余时间
     * @return
     */
    public static long getDayLeftMillions(Date day, boolean ifLeft) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 000);

        long start = day.getTime();
        if (ifLeft) {
            calendar.add(Calendar.DATE, 1);
            return calendar.getTimeInMillis() - start;
        } else {
            return start - calendar.getTimeInMillis();
        }
    }

    /**
     * 给定日期加上指定小时数
     *
     * @param date
     * @param hours
     * @return
     **/
    public static Date plusHours(Date date, int hours) {
        if (date == null) {
            date = new Date();
        }
        long time = date.getTime() + hours * 3600 * 1000L;
        return new Date(time);
    }


    public static void main(String[] args) {

        System.out.println("---------------- getMonthDayCount -----------------");
        System.out.println("2017-12 >>> " + getMonthDayCount(2017, 12));
        System.out.println("2018-06 >>> " + getMonthDayCount(2018, 6));
        System.out.println("2019-01 >>> " + getMonthDayCount(2019, 1));
        System.out.println("current >>> " + getMonthDayCount(null, null));

        System.out.println("---------------- getMonthBeginOrEnd -----------------");
        System.out.println("2017-02 >>> " + getMonthBeginOrEnd(2017, 2, true));
        System.out.println("2018-02 >>> " + getMonthBeginOrEnd(2018, 2, false));
        System.out.println("2019-02 >>> " + getMonthBeginOrEnd(2019, 2, true));
        System.out.println("current >>> " + getMonthBeginOrEnd(null, null, false));

        System.out.println("-----------------getBeginEndBetweenMonths-------------------");
        System.out.println("2018-02 >>> 2019-05: " + getBeginEndBetweenMonths(2018, 2, 2019, 5));

        System.out.println("-----------------getDayLeftMillions-------------------");
        System.out.println("used: " + getDayLeftMillions(new Date(), false));
        System.out.println("left: " + getDayLeftMillions(new Date(), true));

        System.out.println("-----------------getDayBeginOrEnd-------------------");
        System.out.println("day 0': " + getDayBeginOrEnd(new Date(), true));
        System.out.println("day 24': " + getDayBeginOrEnd(null, false));

        System.out.println("-----------------printCalendarInfo----------------------");
        Calendar calendar = Calendar.getInstance();
        System.out.println("current time: " + calendar.getTime());
        System.out.println("current year: " + calendar.get(Calendar.YEAR));
        System.out.println("current month: " + (calendar.get(Calendar.MONTH) + 1));
        System.out.println("current day: " + calendar.get(Calendar.DATE));
        System.out.println("day of the month: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("max day of the month: " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

}
