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
        demo2();
        demo3();
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
        // list1 只保留在 lists2 中的元素
        list.retainAll(dbList);
        System.out.println(list);

        list.removeAll(dbList);
        System.out.println(list);
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
        list.removeAll(dbList);
        System.out.println(list);


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
    }

}
