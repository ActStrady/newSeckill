package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者的角度来设计接口
 * 三个方面：方法定义粒度（多细），参数，返回值类型（return 类型/异常）
 */
public interface SeckillService {
    /**
     * 查询所有秒杀纪录
     * @param offet 起始位置
     * @param limit 长度
     * @return 所有秒杀纪录
     */
    List<Seckill> getSeckillList(int offet, int limit);

    /**
     * 查询单个秒杀纪录
     * @param seckillId 秒杀商品id
     * @return 通过id查找的秒杀商品
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     * @param seckillId 秒杀商品id
     * @return 返回一个封装的Exposer
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId 秒杀商品id
     * @param userPhone 用户手机号
     * @param md5 通过用户秒杀的id随机生成的MD5，防止作弊
     * @return SeckillExecution 封装的秒杀执行后结果
     * @throws SeckillException 秒杀相关业务异常
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException;
}
