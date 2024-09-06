package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "team")
@ToString
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @Column(name = "creator_id")
    private String creatorId;
    @Column(name = "team_name")
    private String teamName;
    @Column(name = "logo")
    private String logoPath;

    public Team(Integer id) {
        this.id = id;
    }
}
