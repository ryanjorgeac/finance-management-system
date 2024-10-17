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
    private Long userId;

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

    @NotNull
    @NotBlank
    private Long categoryId;

    public TransactionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull @NotBlank Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull @NotBlank Long userId) {
        this.userId = userId;
    }

    public @NotNull @NotBlank Calendar getDate() {
        return date;
    }

    public void setDate(@NotNull @NotBlank Calendar date) {
        this.date = date;
    }

    public @NotNull @NotBlank BigDecimal getValue() {
        return value;
    }

    public void setValue(@NotNull @NotBlank BigDecimal value) {
        this.value = value;
    }

    public @NotNull @NotBlank TransactionTypes getType() {
        return type;
    }

    public void setType(@NotNull @NotBlank TransactionTypes type) {
        this.type = type;
    }

    public @NotNull @NotBlank Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull @NotBlank Long categoryId) {
        this.categoryId = categoryId;
    }


}
