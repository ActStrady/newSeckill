package org.seckill.exception;

/**
 * 数据篡改异常
 * @author : ActStrady@tom.com
 * @date : 2019/6/17 11:27
 * @fileName : RewriteException.java
 * @gitHub : https://github.com/ActStrady/seckill
 */
public class RewriteException extends SeckillException{

    public RewriteException(String message) {
        super(message);
    }

    public RewriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
