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
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "factor_id")
    private Factor factor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_match_report_id")
    private PlayerMatchReport playerMatchReport;
    @Column(name = "value")
    private Integer value;

}
