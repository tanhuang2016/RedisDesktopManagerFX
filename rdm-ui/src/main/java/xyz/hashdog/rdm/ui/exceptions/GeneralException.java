package xyz.hashdog.rdm.ui.exceptions;

/**
 *
 * 一般异常,需要自定义message的异常
 * @Author th
 * @Date 2023/7/20 10:30
 */
public class GeneralException extends RuntimeException {

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralException(Throwable cause) {
        super(cause);
    }
}
