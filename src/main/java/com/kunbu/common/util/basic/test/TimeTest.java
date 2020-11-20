package com.kunbu.common.util.basic.test;

import com.google.common.collect.Lists;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author kunbu
 * @date 2020/11/3 14:42
 **/
public class TimeTest {

    public static void main(String[] args) {

        String currentYM = new SimpleDateFormat("yyyy-MM").format(new Date());
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


}
