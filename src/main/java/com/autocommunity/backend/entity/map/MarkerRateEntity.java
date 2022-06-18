package com.autocommunity.backend.entity.map;

import com.autocommunity.backend.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "marker_rate")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkerRateEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "marker_id")
    @NotNull
    private MarkerEntity marker;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @NotNull
    private UserEntity user;

    @Setter
    @Column(name = "rate", columnDefinition = "DOUBLE PRECISION CHECK (rate in (0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5))")
    private double rate;
}
