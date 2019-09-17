package org.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml",
        "classpath:spring/spring-aop.xml"})
public class SeckillServiceTest {
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        int offet = 0;
        int limit = 5;
        List<Seckill> seckillList = seckillService.getSeckillList(offet, limit);
        // log.info("seckillList={}", seckillList);
    }

    @Test
    public void getById() {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        log.info("seckill={}", seckill);
    }

    @Test
    public void SeckillLogin() {
        long id = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            log.info("exposer={}", exposer);
            long phone = 18735689687L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
                log.info("result={}", seckillExecution);
            } catch (RepeatKillException | SeckillCloseException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            // 秒杀未开启
            log.warn("exposer={}", exposer);
        }
    }
}