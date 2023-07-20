package xyz.hashdog.rdm.redis.exceptions;

/**
 * @Author th
 * @Date 2023/7/20 10:30
 */
public class RedisException extends RuntimeException {

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}
