package br.ufpb.dcx.dsc.finance_management.exceptions;

public class InvalidTransactionValue extends RuntimeException {
    public InvalidTransactionValue(String message) {
        super(message);
    }
}
