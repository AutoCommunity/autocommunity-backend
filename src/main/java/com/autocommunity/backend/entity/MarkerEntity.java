package com.autocommunity.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "marker")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkerEntity extends NamedEntity{
    @ManyToOne
    @JoinColumn(name = "type_id")
    private MarkerType markerType;

    @Column(name = "lat")
    @NotNull
    private double lat;

    @Column(name = "lng")
    @NotNull
    private double lng;

    //todo: coordinates. postgis in my opinion
}
