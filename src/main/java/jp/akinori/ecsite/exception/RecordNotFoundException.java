package jp.akinori.ecsite.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String msg) {
        super(msg);
    }

    public RecordNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
