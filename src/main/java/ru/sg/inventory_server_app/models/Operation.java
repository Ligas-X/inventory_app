package ru.sg.inventory_server_app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Operation {
    @Id
    @SequenceGenerator(
            name = "operation_sequence",
            sequenceName = "operation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "operation_sequence"
    )
    private Long id;

    @Column(columnDefinition = "DATE", nullable = false)
    private Date date;

    @Column(nullable = false)
    private int quantity;
    private Long cost;
    private String additionalInfo;

    @ManyToOne
    @JoinColumn(
            name = "operation_type_id",
            foreignKey = @ForeignKey(name = "operation_type_id_fk"))
    private OperationType operationType;

    @ManyToOne
    private UserInfo userInfo;

    @ManyToMany(mappedBy = "operations")
    private Set<AccountingObject> accountingObjects = new HashSet<>();

    public Operation(Date date, int quantity, Long cost, String additionalInfo) {
        this.date = date;
        this.quantity = quantity;
        this.cost = cost;
        this.additionalInfo = additionalInfo;
    }

    public void addAccountingObject(AccountingObject accountingObject) {
        accountingObjects.add(accountingObject);
        accountingObject.getOperations().add(this);
    }

    public void removeAccountingObject(AccountingObject accountingObject) {
        accountingObjects.remove(accountingObject);
        accountingObject.getOperations().remove(this);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", date=" + date +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", operationType=" + operationType +
                ", userInfo=" + userInfo +
                ", accountingObjects=" + accountingObjects +
                '}';
    }
}
