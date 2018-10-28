package org.exception;



/**
 * Created by hxk
 * 2018/10/22 9:14
 * 秒杀关闭异常
 */

public class SeckillCloseException extends SeckillException{

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
