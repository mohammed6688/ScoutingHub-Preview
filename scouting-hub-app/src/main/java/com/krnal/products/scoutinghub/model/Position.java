package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String positionName;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "position_factor",
//            joinColumns = @JoinColumn(name = "position_id"),
//            inverseJoinColumns = @JoinColumn(name = "factor_id"))
//    Set<Factor> positionFactors;


    public Position(Integer id) {
        this.id = id;
    }
}
