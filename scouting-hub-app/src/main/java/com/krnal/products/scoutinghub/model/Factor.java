package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "factor")
public class Factor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factor_id")
    private Integer id;
    @Column(name = "factor_name")
    private String name;

    public Factor(Integer id) {
        this.id = id;
    }
}
