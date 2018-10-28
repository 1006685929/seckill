package org.exception;

/**
 * 秒杀相关业务异常
 * Created by hxk
 * 2018/10/22 9:16
 */

public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
