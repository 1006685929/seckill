package org.exception;

/**
 * Created by hxk
 * 2018/10/22 9:11
 * 重复秒杀异常（运行时异常）
 */

public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
