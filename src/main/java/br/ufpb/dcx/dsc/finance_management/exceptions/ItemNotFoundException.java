package br.ufpb.dcx.dsc.finance_management.exceptions;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(String message){
        super(message);
    }
    public ItemNotFoundException(){
        super("Item not found.");
    }
}
