package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString //causing stack overflow
@Table(name = "match_report")
public class MatchReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "first_team_id")
    private Team firstTeam;
    @ManyToOne
    @JoinColumn(name = "second_team_id")
    private Team secondTeam;
    @Column(name = "first_team_score")
    private Integer firstTeamScore;
    @Column(name = "second_team_score")
    private Integer secondTeamScore;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @Column(name = "match_date")
    private Date matchDate;
    @Column(name = "creator_id")
    private String creatorId;
    @Column(name = "updater_id")
    private Integer updaterId;
    @Column(name = "season")
    private String season;
    @Column(name = "general_notes")
    private String generalNotes;

    @OneToMany(mappedBy = "matchReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerMatchReport> playerMatchReportList;
}
