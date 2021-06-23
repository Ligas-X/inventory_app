package ru.sg.inventory_server_app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class InfoLink {
    @Id
    @SequenceGenerator(
            name = "info_link_sequence",
            sequenceName = "info_link_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "info_link_sequence"
    )
    private Long id;

    @Column
    private String link;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(
            name = "accounting_object_id",
            foreignKey = @ForeignKey(name = "info_link_accounting_object_id_fk"))
    private AccountingObject accountingObject;

    public InfoLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "InfoLink{" +
                "id=" + id +
                ", link='" + link + '\'' +
                '}';
    }
}
