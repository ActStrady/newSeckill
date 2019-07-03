package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import org.seckill.entity.Seckill;
import org.springframework.stereotype.Repository;
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
     * @return 秒杀对象
     */
    public Seckill getSeckill(Long seckillId) {
        // 初始化一个jedis对象
        try (Jedis jedis = jedisPool.getResource()){
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
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 存入redis缓存
     *
     * @param seckill 秒杀对象
     * @return 成功就返回OK
     */
    public String putSeckill(Seckill seckill) {
        // 初始化一个jedis对象
        try (Jedis jedis = jedisPool.getResource()){
            // 定义一个键
            String key = "seckill:" + seckill.getSeckillId();
            // 序列化，第三个参数是缓存器
            byte[] bytes = ProtobufIOUtil.toByteArray(seckill, schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            // 超时缓存，为键设置过期时间为一小时
            int timeout = 60 * 60;
            return jedis.setex(key.getBytes(), timeout, bytes);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
