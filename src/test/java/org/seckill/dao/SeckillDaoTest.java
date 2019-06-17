package org.seckill.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置spring与junit的整合，junit启动时加载springIOC容器
 * spring-test，junit
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    // 注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        log.info("seckill={}",seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            log.info("seckill={}",seckill);
        }
    }

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        // 1就代表是成功
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        log.info("updateCount={}", updateCount);
    }
}