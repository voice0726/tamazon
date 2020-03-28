package jp.akinori.ecsite.exception;

public class AddressNotFoundException extends Exception {
    public AddressNotFoundException(String msg) {
        super(msg);
    }

    public AddressNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
