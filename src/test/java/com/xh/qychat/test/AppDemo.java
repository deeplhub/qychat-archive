package com.xh.qychat.test;

import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
public class AppDemo {


    public static void main(String[] args) throws Exception {
        Set<MemberEntity> memberSet = new HashSet<>();


        MemberEntity entity = new MemberEntity();
        entity.setUserId("111111111111111");
        entity.setName("22222");

        memberSet.add(entity);

        entity = new MemberEntity();
        entity.setUserId("111111111111111");
        entity.setName("22222");

        memberSet.add(entity);

        entity = new MemberEntity();
        entity.setUserId("111111111111111");
        entity.setName("22222");

        memberSet.add(entity);

        entity = new MemberEntity();
        entity.setUserId("111111111111111");
        entity.setName("22222");

        memberSet.add(entity);

        entity = new MemberEntity();
        entity.setUserId("1111111111111112");
        entity.setName("22222");

        memberSet.add(entity);


        System.out.println();
    }


}
