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
public class ImageFile {
    @Id
    @SequenceGenerator(
            name = "image_file_sequence",
            sequenceName = "image_file_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "image_file_sequence"
    )
    private Long id;
    private String name;
    private String downloadUrl;
    private String type;
    private long size;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "accounting_object_id")
    private AccountingObject accountingObject;

    public ImageFile(String name,
                     String downloadUrl,
                     String type,
                     long size) {
        this.name = name;
        this.downloadUrl = downloadUrl;
        this.type = type;
        this.size = size;
    }

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", accountingObject=" + accountingObject +
                '}';
    }
}