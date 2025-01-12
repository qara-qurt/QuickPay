package kz.iitu.quick_pay.exception.product;

public class ProductWithThisRfidAlreadyExist extends RuntimeException {
    public ProductWithThisRfidAlreadyExist(String message) {
        super(message);
    }
}
