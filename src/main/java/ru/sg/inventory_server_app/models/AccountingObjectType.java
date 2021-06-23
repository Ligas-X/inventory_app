package ru.sg.inventory_server_app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class AccountingObjectType {
    @JsonIgnore
    @Id
    @SequenceGenerator(
            name = "accounting_object_type_sequence",
            sequenceName = "accounting_object_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "accounting_object_type_sequence"
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountingObjectTypeName;

    public AccountingObjectType(String accountingObjectTypeName) {
        this.accountingObjectTypeName = accountingObjectTypeName;
    }
}
