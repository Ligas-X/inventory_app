package ru.sg.inventory_server_app.models;

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
public class OperationType {
    @Id
    @SequenceGenerator(
            name = "operation_type_sequence",
            sequenceName = "operation_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "operation_type_sequence"
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String operationTypeName;

    public OperationType(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }
}
