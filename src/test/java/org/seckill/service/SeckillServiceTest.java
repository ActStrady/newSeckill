package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {


    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() {
    }

    @Test
    public void getById() {
    }

    @Test
    public void exportSeckillUrl() {
    }

    @Test
    public void executeSeckill() {
    }
}