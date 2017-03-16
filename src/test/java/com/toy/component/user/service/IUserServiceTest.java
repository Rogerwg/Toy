package com.toy.component.user.service;


import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.toy.component.user.domain.UserVo;

/**
 * Created by roger on 2017/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {
        "classpath:spring-mybatis.xml"
})
public class IUserServiceTest {
    @Resource
    private IUserService userService;

    @Test
    public void getUserById() throws Exception {
        UserVo orderVo = userService.getOrderById(1);
        Assert.assertNotNull(orderVo);

    }

    @Test
    public void getOrderById() throws Exception {

    }

    @Test
    public void getAllOrder() throws Exception {
        List<UserVo> orderVo = userService.getAllOrder();
        Assert.assertNotNull(orderVo);

    }

}