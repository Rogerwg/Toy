package com.toy.test;

import java.util.List;

import lombok.extern.log4j.Log4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.toy.component.user.domain.AddressCluster;
import com.toy.component.user.domain.UserVo;
import com.toy.component.user.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
// 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {
        "classpath:spring-mybatis.xml"
})
@Log4j
public class MyBatisTest {

    // private ApplicationContext ac = null;
    @Autowired
    private IUserService userService = null;

    // @Before
    // public void before() {
    // ac = new ClassPathXmlApplicationContext("applicationContext.xml");
    // userService = (IUserService) ac.getBean("userService");
    // }

    @Test
    public void testBatis() {
        log.info("test--OK");
        UserVo user = userService.getUserById(1);
        // System.out.println(user.getUserName());
        // logger.info("值："+user.getUserName());
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void testAddress() {
        List<AddressCluster> result = userService.parseAddressCluster();
        userService.saveCluster(result);
        Assert.assertNotNull(result);
    }

}
