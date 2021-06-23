package ru.sg.inventory_server_app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class UserInfo {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String lName;

    @Column(nullable = false)
    private String fName;
    private String mName;

    @Column(nullable = false)
    private String position;
    private String phone;

    @Column(unique = true)
    private String email;
    private String additionalData;

    @JsonIgnore
    @OneToMany(mappedBy = "userInfo")
    private List<Operation> operations = new ArrayList<>();

    public UserInfo(String lName,
                    String fName,
                    String mName,
                    String position,
                    String phone,
                    String email,
                    String additionalData) {
        this.lName = lName;
        this.fName = fName;
        this.mName = mName;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.additionalData = additionalData;
    }
}
