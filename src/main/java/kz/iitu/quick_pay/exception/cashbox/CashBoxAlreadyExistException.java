package kz.iitu.quick_pay.exception.cashbox;

public class CashBoxAlreadyExistException extends RuntimeException {
    public CashBoxAlreadyExistException(String message) {
        super(message);
    }
}
