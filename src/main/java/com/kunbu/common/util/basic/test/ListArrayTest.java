package com.kunbu.common.util.basic.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 题外话：判断是否是数组用 obj.getClass().isArray()
 *
 *
 * @author: kunbu
 * @create: 2019-11-21 15:29
 **/
public class ListArrayTest {

    public static void main(String[] args) {

        testArray2List();
        testList2Array();
    }

    public static void testArray2List() {
        String[] array = {"hello", "world"};

        //1. for

        //2. Arrays.asList(T[])（注意：不能直接使用asList返回的ArrayList，因为那是内部类，大小固定，功能不全等问题）
        List<String> list2 = new ArrayList<>(Arrays.asList(array));
        System.out.println(list2);

        //3. Collections.addAll(...)
        List<String> list3 = new ArrayList<>();
        Collections.addAll(list3, array);
        System.out.println(list3);

        //4. Stream.of(...)
        List<String> list4 = Stream.of(array).collect(Collectors.toList());
        System.out.println(list4);
    }

    public static void testList2Array() {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("kunbu");

        //1. for

        //2. List.toArray(T[])  不能用list.toArray()，返回的是Object[]数组
        String[] array =  list.toArray(new String[list.size()]);
        System.out.println(Arrays.toString(array));
    }

}
