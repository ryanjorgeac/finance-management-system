package br.ufpb.dcx.dsc.finance_management.DTOs.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTOUpdate {

    @NotBlank
    @NotNull
    private String name;

    public UserDTOUpdate() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
