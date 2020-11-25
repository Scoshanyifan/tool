package com.kunbu.common.util.basic.test;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author kunbu
 * @date 2020/11/24 15:06
 **/
public class MapSetTest {

    public static void main(String[] args) {
        testSetsDiff();
    }

    public static void testSetsDiff() {
        Set<String> save;
        Set<String> del;


        save = Sets.difference(Sets.newHashSet("apple", "orange", "banana"), Sets.newHashSet());
        del = Sets.difference(Sets.newHashSet(), Sets.newHashSet("apple", "orange"));
        System.out.println("save: " + save);
        System.out.println("del: " + del);

        save = Sets.difference(Sets.newHashSet("apple", "orange", "banana"), Sets.newHashSet("apple", "pear"));
        del = Sets.difference(Sets.newHashSet("apple", "pear"), Sets.newHashSet("apple", "orange", "banana"));
        System.out.println("save: " + save);
        System.out.println("del: " + del);

        save = Sets.difference(Sets.newHashSet("apple", "pear", "banana"), Sets.newHashSet("apple", "pear"));
        del = Sets.difference(Sets.newHashSet("apple", "pear"), Sets.newHashSet("apple", "pear", "banana"));
        System.out.println("save: " + save);
        System.out.println("del: " + del);

    }

}
