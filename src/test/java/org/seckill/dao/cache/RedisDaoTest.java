package org.seckill.dao.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring与junit的整合，junit启动时加载springIOC容器
 * spring-test，junit
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() {
        long seckillId = 1001L;
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (null == seckill) {
            seckill = seckillDao.queryById(seckillId);
            if (null != seckill) {
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(seckillId);
                System.out.println(seckill);
            }
        }
    }
}