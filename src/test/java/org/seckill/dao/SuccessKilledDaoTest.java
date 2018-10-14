package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SuccessKilledDaoTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() {
        long id = 1001L;
        long phone = 13856422356L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        logger.info("insertCount={}", insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1001L;
        long phone = 13856422356L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        logger.info("successKilled={}", successKilled);
    }
}