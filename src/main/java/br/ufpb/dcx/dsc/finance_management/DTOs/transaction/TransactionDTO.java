package br.ufpb.dcx.dsc.finance_management.DTOs.transaction;

import br.ufpb.dcx.dsc.finance_management.types.TransactionTypes;
import br.ufpb.dcx.dsc.finance_management.validation.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Calendar;

public class TransactionDTO {

    private Long id;

    private String description;

    @NotNull
    private Long userId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Calendar date;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 7, fraction = 2)
    private BigDecimal value;

    @NotNull
    @TransactionType
    private String type;

    @NotNull
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

    public @NotNull Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public @NotNull Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull Long categoryId) {
        this.categoryId = categoryId;
    }

    public @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 7, fraction = 2) BigDecimal getValue() {
        return value;
    }

    public void setValue(@DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 7, fraction = 2) BigDecimal value) {
        this.value = value;
    }

    public @NotNull Calendar getDate() {
        return date;
    }

    public void setDate(@NotNull Calendar date) {
        this.date = date;
    }

    public @NotNull String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }
}
