package org.seckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.RewriteException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 当不知道是什么时用@Component,具体知道时可以使用 @Controller@ @Service @Repository
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {
    private final SeckillDao seckillDao;
    private final SuccessKilledDao successKilledDao;
    private final RedisDao redisDao;

    /**
     * 注入service依赖 还可以使用 @Resource@Inject
     * 这里使用的是构造器注入
     * 当有多个时应该使用构造器注入
     */
    @Autowired
    public SeckillServiceImpl(SeckillDao seckillDao, SuccessKilledDao successKilledDao, RedisDao redisDao) {
        this.seckillDao = seckillDao;
        this.successKilledDao = successKilledDao;
        this.redisDao = redisDao;
    }

    @Override
    public List<Seckill> getSeckillList(int offset, int limit) {
        return seckillDao.queryAll(offset, limit);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Date nowTime = new Date();
        // redis缓存里取
        Seckill seckill = redisDao.getSeckill(seckillId);
        // 缓存里没有就从数据库里取，并放到缓存里
        if (null == seckill) {
            seckill = seckillDao.queryById(seckillId);
            // 没有该商品
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                // 写入缓存
                redisDao.putSeckill(seckill);
            }

        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // md5:转换特定字符串的过程，不可逆
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 使用注解控制事物方法的优点
     * 1、团队编程风格一致
     * 2、事物操作时间要短，不要穿插其他网络操作RPC、HTTP请求，实在要有就剥离到事物方法外部
     * 3、不是所有的方法都需要事物，只有一条修改操作和只读的不需要事物控制
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException {
        Date nowTime = new Date();
        try {
            if (md5 == null || !md5.equals(getMd5(seckillId))) {
                throw new RewriteException("seckill data rewrite");
            }
            // 执行秒杀逻辑：减库存 + 纪录购买行为
            // 减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没库存了，秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                // 纪录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException | RewriteException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 所有编译期异常 转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    /**
     * 生成MD5串
     *
     * @param seckillId 秒杀商品id
     * @return MD5串
     */
    private String getMd5(long seckillId) {
        String slat = "wiuib%$^+LKLoeisui378oiU*Y*(hhHU)(0O?>:M>?<NJSHUAT^_)_)I(__++";
        String base = seckillId + "/" + slat;
        //spring的一个生成MD5的方法
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
