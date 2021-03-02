package com.kunbu.common.util.basic;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.CronExpression;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 1. Seconds Minutes Hours Day Month Week Year （7个占位符）
 * 2. Seconds Minutes Hours Day Month Week （6个占位符）
 *
 * 秒    分钟   小时  日期      月份  星期  年(可选)
 * 0-59  0-59  0-23  1-30(31) 1-12  1-7
 *
 * @author kunbu
 * @date 2020/12/17 14:08
 **/
public class CronUtil {

    private static final String CRON_DATE_PATTERN = "ss mm HH dd MM ? yyyy";

    private static final String CRON_INTERVAL_SPLITTER = "-";

    private static final String CRON_ITEM_SPLITTER = ",";

    private static final String HOUR_MINUTE_SPLITTER = ":";

    public static String date2Cron(Date date) {
        if (date != null) {
            return new SimpleDateFormat(CRON_DATE_PATTERN).format(date);
        } else {
            return null;
        }
    }

    public static Date cron2Date(String cron) {
        try {
            return new SimpleDateFormat(CRON_DATE_PATTERN).parse(cron);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean checkDateByCron(Date date, String cron) {
        try {
            CronExpression cronExpression = new CronExpression(cron);
            return cronExpression.isSatisfiedBy(date);
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean checkDateByCron(Date date, List<String> cronList) {
        try {
            for (String cron : cronList) {
                CronExpression cronExpression = new CronExpression(cron);
                if (cronExpression.isSatisfiedBy(date)) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 时间点转cron
     *
     * 08:23 执行一次                 >>> * 23 08 19 12 ? 2020
     * 08:23 工作日                   >>> * 23 08 ? * 2,3,4,5,6 *
     * 08:23 周末                     >>> * 23 08 ? * 1,7 *
     * 08:23 每天                     >>> * 23 08 * * ? *
     * 08:23 自定义（周日，周一，周二）  >>> * 23 08 ? * 1,2,3 *
     *
     **/
    public static String timePoint2Cron(String pointTime, Integer timeType, List<Integer> dayOfWeek) {
        String[] cronItemArr = cronItemArr(pointTime, null, null, timeType, dayOfWeek);
        String[] hourMinute = pointTime.split(HOUR_MINUTE_SPLITTER);
        cronItemArr[2] = hourMinute[0];
        cronItemArr[1] = hourMinute[1];
        return convertCronItemArr(cronItemArr);
    }

    /**
     * 时间段转cron
     **/
    public static List<String> timeRange2Cron(String startTime, String endTime, Integer timeType, List<Integer> dayOfWeek) {
        List<String> cronList = new ArrayList<>();
        String[] cronItemArr = cronItemArr(null, startTime, endTime, timeType, dayOfWeek);
        String[] hourMinuteStart = startTime.split(HOUR_MINUTE_SPLITTER);
        String[] hourMinuteEnd = endTime.split(HOUR_MINUTE_SPLITTER);
        int hourStart = Integer.parseInt(hourMinuteStart[0]);
        int hourEnd = Integer.parseInt(hourMinuteEnd[0]);
        int hourDelta = hourEnd - hourStart;
        if (hourDelta > 2) {
            /**
             * 08:23 - 12:23
             *
             * >>> * 23-59 08 19 12 ? 2020
             * >>> * 0-59 9-11 19 12 ? 2020
             * >>> * 0-23 12 19 12 ? 2020
             *
             **/
            cronItemArr[2] = hourMinuteStart[0];
            cronItemArr[1] = hourMinuteStart[1] + CRON_INTERVAL_SPLITTER + "59";
            cronList.add(convertCronItemArr(cronItemArr));
            cronItemArr[2] = Integer.toString(hourStart + 1) + CRON_INTERVAL_SPLITTER + Integer.toString(hourEnd - 1);
            cronItemArr[1] = "0-59";
            cronList.add(convertCronItemArr(cronItemArr));
            cronItemArr[2] = hourMinuteEnd[0];
            cronItemArr[1] = "0" + CRON_INTERVAL_SPLITTER + hourMinuteEnd[1];
            cronList.add(convertCronItemArr(cronItemArr));
        } else if (hourDelta == 1) {
            // 08:23 - 09:47
            cronItemArr[2] = hourMinuteStart[0];
            cronItemArr[1] = hourMinuteStart[1] + CRON_INTERVAL_SPLITTER + "59";
            cronList.add(convertCronItemArr(cronItemArr));
            cronItemArr[2] = hourMinuteEnd[0];
            cronItemArr[1] = "0" + CRON_INTERVAL_SPLITTER + hourMinuteEnd[1];
            cronList.add(convertCronItemArr(cronItemArr));
        } else if (hourDelta == 0) {
            // 08:23 - 08:47
            cronItemArr[2] = hourMinuteStart[0];
            cronItemArr[1] = hourMinuteStart[1] + CRON_INTERVAL_SPLITTER + hourMinuteEnd[1];
            cronList.add(convertCronItemArr(cronItemArr));
        } else {
            throw new RuntimeException("bad time params");
        }
        return cronList;
    }

    private static String[] cronItemArr(String pointTime, String startTime, String endTime, Integer timeType, List<Integer> dayOfWeek) {
        String[] cronItemArr = {"*", "*", "*", "?", "*", "?", "*"};
        SceneTimeTypeEnum timeTypeEnum = SceneTimeTypeEnum.of(timeType);
        switch (timeTypeEnum) {
            case ONCE:
                Calendar calendar = Calendar.getInstance();
                cronItemArr[6] = Integer.toString(calendar.get(Calendar.YEAR));
                cronItemArr[4] = Integer.toString(calendar.get(Calendar.MONTH) + 1);
                // 判断传参分钟小时在当前之前还是之后，若之前取当天日期，否则下一天
                if (pointTime != null) {
                    if (compareHourMinute(pointTime, calendar.getTime())) {
                        cronItemArr[3] = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                    } else {
                        cronItemArr[3] = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH) + 1);
                    }
                } else {
                    // 如果是时间段，当前早于起始时间/当前处于起始结束中间=当天，否则第二天（当前大于结束时间）
                    if (!compareHourMinute(endTime, calendar.getTime())) {
                        cronItemArr[3] = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH) + 1);
                    } else {
                        cronItemArr[3] = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
                break;
            case WORK_DAY:
                cronItemArr[5] = "2,3,4,5,6";
                break;
            case WEEKEND:
                cronItemArr[5] = "1,7";
                break;
            case EVERYDAY:
                cronItemArr[3] = "*";
                break;
            case DAY_OF_WEEK:
                if (CollectionUtils.isNotEmpty(dayOfWeek)) {
                    StringJoiner joiner = new StringJoiner(CRON_ITEM_SPLITTER, "", "");
                    dayOfWeek.stream().forEach(x -> joiner.add(x.toString()));
                    cronItemArr[5] = joiner.toString();
                }
                break;
            default:
        }
        return cronItemArr;
    }

    private static String convertCronItemArr(String[] cronItemArr) {
        StringJoiner joiner = new StringJoiner(" ", "", "");
        for (String cronItem : cronItemArr) {
            joiner.add(cronItem);
        }
        return joiner.toString();
    }

    private static boolean compareHourMinute(String hourMinute, Date date) {
        String hm = new SimpleDateFormat("HH:mm").format(date);
        if (hm.compareTo(hourMinute) > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        // 50 16 14 17 12 ? 2020
        String cron = date2Cron(new Date());
        System.out.println(cron);
        // Thu Dec 17 14:16:50 CST 2020
        Date date = cron2Date("0000 00 000 17 12 ? 2020");
        System.out.println(date);
        // 12,13,14
        System.out.println(checkDateByCron(date, "* * 12-14 * * ?"));
        System.out.println(checkDateByCron(new Date(), "* 53 15 * 12 ? 2020"));
        System.out.println(checkDateByCron(date, "* * 14 17 12 ? 2020"));

        System.out.println(timePoint2Cron("08:23", 1, null));
        System.out.println(timePoint2Cron("08:23", 2, null));
        System.out.println(timePoint2Cron("08:23", 3, null));
        System.out.println(timePoint2Cron("08:23", 4, null));
        System.out.println(timePoint2Cron("08:23", 5, Lists.newArrayList(1,2,3)));
        List<String> cronListOnce = timeRange2Cron("08:23", "17:23", 1, null);
        System.out.println(cronListOnce);
        System.out.println(checkDateByCron(new Date(), cronListOnce));
        System.out.println(timeRange2Cron("08:23", "12:23", 2, null));
        System.out.println(timeRange2Cron("08:23", "12:23", 3, null));
        List<String> cronList = timeRange2Cron("08:23", "16:23", 4, null);
        System.out.println(cronList);
        System.out.println(timeRange2Cron("08:23", "12:23", 5, Lists.newArrayList(1,2,3)));
    }
}


enum SceneTimeTypeEnum{
    ONCE(1),
    WORK_DAY(2),
    WEEKEND(3),
    EVERYDAY(4),
    DAY_OF_WEEK(5),
    ;

    private Integer type;

    public Integer getType() {
        return type;
    }

    SceneTimeTypeEnum(Integer type) {
        this.type = type;
    }

    public static SceneTimeTypeEnum of(Integer timeType) {
        for (SceneTimeTypeEnum e : values()) {
            if (e.type.equals(timeType)) {
                return e;
            }
        }
        return null;
    }
}