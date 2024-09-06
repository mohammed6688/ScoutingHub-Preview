package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "player_match_report")
public class PlayerMatchReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "match_report_id")
    private MatchReport matchReport;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
    @Column(name = "final_rating")
    private Integer finalRating;
    @Column(name = "comments")
    private String comments;

    @OneToMany(mappedBy = "playerMatchReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Rating> ratingsList;
}
