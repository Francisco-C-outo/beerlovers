package com.objectiveplatform.beerlovers.datamodel.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;
    @NotBlank
    @Size(max = 120)
    private String password;

    //For simplicity of implementation purposes let's allow hibernate to generate this association by us,
    //we will just state the desired table name and the desired column name
    @ElementCollection
    @CollectionTable(name = "favorites", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "external_id")
    private Set<Integer> favoriteBeers = new HashSet<>();
}
