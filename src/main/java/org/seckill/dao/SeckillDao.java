package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId 秒杀商品id
     * @param killTime 秒杀时间
     * @return 如果影响行数>1,表示更新的行数，如果为0，则代表更新未成功
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 通过Id查询秒杀商品
     * @param seckillId 秒杀商品id
     * @return 秒杀商品
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * java没有不能保存形参，会识别成arg0，arg1
     * @param offet 起始位置
     * @param limit 长度
     * @return 秒杀商品列表
     */
    List<Seckill> queryAll(@Param("offet") int offet, @Param("limit") int limit);
}
