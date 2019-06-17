package org.seckill.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() {
        long id = 1000L;
        long phone = 13556422356L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        log.info("insertCount={}", insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1001L;
        long phone = 13856422356L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        log.info("successKilled={}", successKilled);
    }
}