package com.autocommunity.backend.entity.map;

import com.autocommunity.backend.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "marker")
@Getter
@Setter
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

    public enum Status {
        ACTIVE,
        DELETED
    }


    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarkerType markerType;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "lat")
    @NotNull
    private double lat;

    @Column(name = "lng")
    @NotNull
    private double lng;

    @ManyToOne
    @JoinColumn(name = "owner")
    private UserEntity owner;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "marker_id")
    private Set<EventEntity> events = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "marker_id")
    private Set<MarkerRateEntity> rates = new HashSet<>();
}
