package kz.iitu.quick_pay.exception.product;

public class ProductAlreadyExist extends RuntimeException {
    public ProductAlreadyExist(String message) {
        super(message);
    }
}
