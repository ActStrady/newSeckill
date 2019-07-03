package org.seckill.dao.cache;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import org.seckill.entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis缓存dao, 采用protostuff来序列化
 *
 * @author : ActStrady@tom.com
 * @date : 2019/7/3 16:20
 * @fileName : RedisDao.java
 * @gitHub : https://github.com/ActStrady/seckill
 */
@Slf4j
public class RedisDao {
    private final JedisPool jedisPool;
    /**
     * 业务对象所有信息的类，包括类信息、字段信息，类必须是pojo
     */
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    /**
     * 初始化redis连接池
     *
     * @param ip   IP地址
     * @param port 端口
     */
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    /**
     * 从redis缓存里取Seckill记录
     *
     * @param seckillId 秒杀id
     * @return Seckill
     */
    public Seckill getSeckill(Long seckillId) {
        // try (Jedis jedis = jedisPool.getResource()) {
        //
        // }catch (Exception e) {
        //
        // }
        // 初始化一个jedis对象
        Jedis jedis = jedisPool.getResource();
        // 定义一个键
        String key = "seckill:" + seckillId;
        // 通过键取值
        byte[] bytes = jedis.get(key.getBytes());
        // 取到就反序列化后返回
        if (null != bytes) {
            // 一个空的Seckill
            Seckill seckill = schema.newMessage();
            // 反序列化
            ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
            return seckill;
        }
        return null;
    }


    public String putSeckill(Seckill seckill) {
        return null;
    }
}
