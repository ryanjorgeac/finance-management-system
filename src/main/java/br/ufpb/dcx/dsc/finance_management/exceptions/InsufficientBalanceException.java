package br.ufpb.dcx.dsc.finance_management.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
    public InsufficientBalanceException() {
        super("Insufficient balance.");
    }
}
