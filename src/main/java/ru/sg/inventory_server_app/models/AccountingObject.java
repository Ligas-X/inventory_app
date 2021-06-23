package ru.sg.inventory_server_app.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class AccountingObject {
    @Id
    @SequenceGenerator(
            name = "accounting_object_sequence",
            sequenceName = "accounting_object_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "accounting_object_sequence"
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    private String barcode; // штрихкод

    @Column(nullable = false)
    private int currentQuantity; // текущее кол-во ОУ
    private int oldQuantity; // старое значение кол-ва ОУ
    private int quantityInOperation; // значение кол-ва ОУ в операции
    private String techReqs; // технические требования
    private String usePurpose; // цель использования (Equipment)

    @ManyToOne
    @JoinColumn(
            name = "accounting_object_type_id",
            foreignKey = @ForeignKey(name = "accounting_object_type_id_fk")
    )
    private AccountingObjectType accountingObjectType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ao_id", referencedColumnName = "id") // refColumnName -> id in AccountingObject class
    List<InfoLink> infoLinks = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "accounting_object_operation",
            joinColumns = @JoinColumn(name = "accounting_object_id"),
            inverseJoinColumns = @JoinColumn(name = "operation_id")
    )
    private Set<Operation> operations = new HashSet<>();

    @OneToOne(mappedBy = "accountingObject")
    private ImageFile imageFile;

    public AccountingObject(String name,
                            String barcode,
                            int currentQuantity,
                            int oldQuantity,
                            int quantityInOperation,
                            String techReqs,
                            String usePurpose,
                            AccountingObjectType accountingObjectType,
                            List<InfoLink> infoLinks,
                            Set<Operation> operations,
                            ImageFile imageFile) {
        this.name = name;
        this.barcode = barcode;
        this.currentQuantity = currentQuantity;
        this.oldQuantity = oldQuantity;
        this.quantityInOperation = quantityInOperation;
        this.techReqs = techReqs;
        this.usePurpose = usePurpose;
        this.accountingObjectType = accountingObjectType;
        this.infoLinks = infoLinks;
        this.operations = operations;
        this.imageFile = imageFile;
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
        operation.getAccountingObjects().add(this);
    }

    public void removeOperation(Operation operation) {
        operations.remove(operation);
        operation.getAccountingObjects().remove(this);
    }

    public void addInfoLink(InfoLink infoLink) {
        infoLink.setAccountingObject(this);
        infoLinks.add(infoLink);
    }

    public void removeInfoLink(InfoLink infoLink) {
        infoLinks.remove(infoLink);
        infoLink.setAccountingObject(null);
    }

    @Override
    public String toString() {
        return "AccountingObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                ", currentQuantity=" + currentQuantity +
                ", oldQuantity=" + oldQuantity +
                ", quantityInOperation=" + quantityInOperation +
                ", techReqs='" + techReqs + '\'' +
                ", usePurpose='" + usePurpose + '\'' +
                ", accountingObjectType=" + accountingObjectType +
                ", infoLinks=" + infoLinks +
                ", operations=" + operations +
                ", imageFile=" + imageFile +
                '}';
    }
}
