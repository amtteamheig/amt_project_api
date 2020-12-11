package ch.heigvd.amt.api.exceptions;

public class NotFoundException extends ApiException {
    private final int code;

    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
