package br.ufpb.dcx.dsc.finance_management.DTOs;

import br.ufpb.dcx.dsc.finance_management.types.TransactionTypes;
import br.ufpb.dcx.dsc.finance_management.validation.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Calendar;

public class TransactionDTO {

    private Long id;

    private String description;

    @NotNull
    @NotBlank
    private Calendar date;

    @NotNull
    @NotBlank
    private BigDecimal value;

    @NotNull
    @NotBlank
    @TransactionType
    private TransactionTypes type;
}
