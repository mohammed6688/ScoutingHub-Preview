package com.krnal.products.scoutinghub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "creator")
    private String creator;
    @Column(name = "operation_type")
    private Integer operationType;
    @Column(name = "resource_type")
    private Integer resourceType;
    @Column(name = "resource_id")
    private Integer resourceId;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    public Calendar(Integer id) {
        this.id = id;
    }
}
