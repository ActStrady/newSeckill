package org.seckill.exception;

/**
 * @author : ActStrady@tom.com
 * @date : 2019/9/18 1:01
 * @fileName : SoldOutException.java
 * @gitHub : https://github.com/ActStrady/seckill
 */
public class SoldOutException extends SeckillException {
    public SoldOutException(String message) {
        super(message);
    }

    public SoldOutException(String message, Throwable cause) {
        super(message, cause);
    }
}
