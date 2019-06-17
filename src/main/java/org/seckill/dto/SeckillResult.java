package org.seckill.dto;

import lombok.Data;

/**
 * 封装json结果, 成功返回数据，否则返回错误信息
 * @author : ActStrady@tom.com
 * @date : 2019/6/16 22:19
 * @fileName : SeckillResult.java
 * @gitHub : https://github.com/ActStrady/newSeckill
 */
@Data
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
