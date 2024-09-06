package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shadow_list")
//@ToString
@DynamicUpdate
public class ShadowList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
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

    @OneToMany(mappedBy = "shadowList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShadowListPlayer> shadowListPlayerList;
}
