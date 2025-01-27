package kz.iitu.quick_pay.exception.cashbox;

public class CashBoxNotFoundException extends RuntimeException {
    public CashBoxNotFoundException(String message) {
        super(message);
    }
}
