package br.ufpb.dcx.dsc.finance_management.models;

import br.ufpb.dcx.dsc.finance_management.types.TransactionTypes;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;


@Entity
@Table(name = "tb_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Calendar date;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "type")
    private TransactionTypes type;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    public Transaction() {
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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public TransactionTypes getType() {
        return type;
    }

    public void setType(TransactionTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "description='" + description + '\'' +
                ", date=" + date +
                ", value=" + NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(value) +
                ", type=" + type +
                '}';
    }
}
