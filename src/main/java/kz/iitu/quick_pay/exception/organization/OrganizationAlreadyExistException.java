package kz.iitu.quick_pay.exception.organization;

public class OrganizationAlreadyExistException extends RuntimeException{
    public OrganizationAlreadyExistException(String message) {
        super(message);
    }
}
