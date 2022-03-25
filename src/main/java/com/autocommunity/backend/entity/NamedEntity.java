package com.autocommunity.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class NamedEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return this.getName();
    }
}
