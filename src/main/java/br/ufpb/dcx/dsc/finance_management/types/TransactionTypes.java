package br.ufpb.dcx.dsc.finance_management.types;

public enum TransactionTypes {
    INCOMING(1),
    OUTCOMING(-1);

    private final Integer value;
    TransactionTypes(Integer value) {
        this.value =  value;
    }

    public Integer getValue(){
        return this.value;
    }
}
