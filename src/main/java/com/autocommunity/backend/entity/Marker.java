package com.autocommunity.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "marker")
public class Marker extends NamedEntity{
    @ManyToOne
    @JoinColumn(name = "type_id")
    private MarkerType markerType;

    //todo: coordinates. postgis in my opinion
}
