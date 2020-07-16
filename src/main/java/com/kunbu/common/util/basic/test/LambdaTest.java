package com.kunbu.common.util.basic.test;

import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaTest {

    private static final List<OrderApprove> list = Lists.newArrayList(
            new OrderApprove("1", LocalDateTime.of(2019, 2, 12, 13, 34, 56)),
            new OrderApprove("2", LocalDateTime.of(2020, 5, 1, 9, 12, 23)),
            new OrderApprove("3", LocalDateTime.of(2020, 7, 3, 22, 54, 9))
    );

    public static void main(String[] args) {

        testStream();
        testSort();
    }

    public static void testStream() {
        List<OrderApprove> list = Lists.newArrayList(
                new OrderApprove("1", null, "收银柜"),
                new OrderApprove("1", null, "卧式冷柜"),
                new OrderApprove("1", null, "卧式冷柜"),
                new OrderApprove("1", null, "卧式冷柜"),
                new OrderApprove("1", null, "卧式冷柜")
        );
        list.stream().map(x -> x.getName()).distinct().forEach(x -> {
            System.out.println(x);
        });
    }

    /**
     * 方法引用
     */
    public static void testMethodRef() {
        // 引用实例/静态方法
        String[] array = {"apple", "lemon", "orange"};
        // TODO 比较器 Comparator<? super T> int compare(T o1, T o2); 其中需要两个参数，但是String的compareTo只有一个
        // TODO 因为实例方法有一个隐含的this参数，compareTo方法在实际调用时，第一个隐含参数总是传入this，相当于静态方法
        // TODO public int compareTo(this, String anotherString)
        Arrays.sort(array, String::compareTo);

        // 引用构造函数
        List<String> orderIds = Lists.newArrayList("123", "456");
        List<OrderApprove> orderApproves = orderIds.stream().map(OrderApprove::new).collect(Collectors.toList());
    }

    public static void testSort() {

        // TODO 默认排序需要当前类型支持 java.lang.ClassCastException: OrderApprove cannot be cast to java.lang.Comparable
        OrderApprove oa = list.stream().sorted().findFirst().get();
        System.out.println(oa);

        oa = list.stream().sorted(Comparator.comparing(OrderApprove::getCreateTime)).findFirst().get();
        System.out.println(oa);

        oa = list.stream().sorted(Comparator.comparing(OrderApprove::getCreateTime).reversed()).findFirst().get();
        System.out.println(oa);

        oa = list.stream().sorted((o1, o2) -> o2.getCreateTime().compareTo(o2.getCreateTime())).findFirst().get();
        System.out.println(oa);
    }

}

class OrderApprove {

    private String orderId;
    private LocalDateTime createTime;
    private String name;

    public OrderApprove(String orderId) {
        this.orderId = orderId;
        this.createTime = LocalDateTime.now();
    }

    public OrderApprove(String orderId, LocalDateTime createTime) {
        this.orderId = orderId;
        this.createTime = createTime;
    }

    public OrderApprove(String orderId, LocalDateTime createTimem, String name) {
        this.orderId = orderId;
        this.createTime = createTime;
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}