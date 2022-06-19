package com.autocommunity.backend.entity.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "marker")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkerEntity {

    public enum MarkerType {
        GAS_STATION,
        CAR_WASH,
        SERVICE_STATION,
        DRIFT,
        DRAG_RACING,
        OTHER
    }


    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarkerType markerType;

    @Column(name = "name")
    private String name;

    @Column(name = "lat")
    @NotNull
    private double lat;

    @Column(name = "lng")
    @NotNull
    private double lng;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "marker_id")
    private Set<EventEntity> events = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "marker_id")
    private Set<MarkerRateEntity> rates = new HashSet<>();
}
