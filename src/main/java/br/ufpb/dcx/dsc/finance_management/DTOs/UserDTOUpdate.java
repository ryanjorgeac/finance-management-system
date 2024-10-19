package br.ufpb.dcx.dsc.finance_management.DTOs;

public class UserDTOUpdate {

    private String name;

    public UserDTOUpdate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
