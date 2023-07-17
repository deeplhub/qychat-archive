package com.xh.qychat.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
public class AppDemo {


    public static void main(String[] args) throws Exception {
//        demo1();
//        demo2();
//        demo3();

        while (true) {

        }
    }

    public static void demo1() {
        List<String> list = new ArrayList<>();

        list.add("AAA");
        list.add("BBB");
        list.add("CCC");
        list.add("DDD");

        List<String> dbList = new ArrayList<>();

        dbList.add("AAA");
        dbList.add("BBB");
        dbList.add("CCC");
        dbList.add("DDD");

        // TODO 不做任何操作
        if (list.size() == dbList.size()) {
            for (String str : list) {
            }
        }
    }

    public static void demo2() {
        List<String> list = new ArrayList<>();

        list.add("AAA");
        list.add("BBB");
        list.add("CCC");

        List<String> dbList = new ArrayList<>();

        dbList.add("AAA");
        dbList.add("BBB");
        dbList.add("CCC");
        dbList.add("DDD");

        // TODO 删除DDD
        boolean isSuccess = list.retainAll(dbList);
        System.out.println(isSuccess);
        System.out.println(list);

        isSuccess = dbList.retainAll(list);
        System.out.println(isSuccess);
        System.out.println(dbList);

        isSuccess = list.removeAll(dbList);
        System.out.println(isSuccess);
        System.out.println(list);
        System.out.println("==================");
    }

    public static void demo3() {
        List<String> list = new ArrayList<>();

        list.add("AAA");
        list.add("BBB");
        list.add("CCC");
        list.add("DDD");

        List<String> dbList = new ArrayList<>();

        dbList.add("AAA");
        dbList.add("BBB");
        dbList.add("CCC");

        // TODO 添加DDD

        boolean isSuccess = list.retainAll(dbList);
        System.out.println(isSuccess);
        System.out.println(list);

        isSuccess = dbList.retainAll(list);
        System.out.println(isSuccess);
        System.out.println(dbList);

        isSuccess = list.removeAll(dbList);
        System.out.println(isSuccess);
        System.out.println(list);
        System.out.println("==================");
    }

    public static void demo4() {
        List<String> list = new ArrayList<>();

        list.add("AAA");
        list.add("BBB");
        list.add("CCC");
        list.add("DDD");

        List<String> dbList = new ArrayList<>();

        dbList.add("AAA");
        dbList.add("BBB");
        dbList.add("CCC");
        dbList.add("FFF");

        // TODO 添加DDD

        boolean isSuccess = list.retainAll(dbList);
        System.out.println(isSuccess);
        System.out.println(list);

        isSuccess = dbList.retainAll(list);
        System.out.println(isSuccess);
        System.out.println(dbList);

        isSuccess = list.removeAll(dbList);
        System.out.println(isSuccess);
        System.out.println(list);
        System.out.println("==================");
    }

}
