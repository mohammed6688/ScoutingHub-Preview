package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "player")
@ToString
@DynamicUpdate
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @Column(name = "creator_id")
    private String creatorId;
    @Column(name = "updater_id")
    private Integer updaterId;
    @Column(name = "name")
    private String name;
    @Column(name = "image")
    private String imagePath;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "country_code")
    private String countryCode;

    public Player(Integer id) {
        this.id = id;
    }
}
