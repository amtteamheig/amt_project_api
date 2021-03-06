package ch.heigvd.amt.api.exceptions;

public class ApiException extends Exception {
    private final int code;

    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
