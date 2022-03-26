package com.autocommunity.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Table(name = "\"session\"")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionEntity {
    public enum Status {
        ACTIVE,
        EXPIRED,
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    private UUID id;
    @Column(name = "user_id")
    @NotNull
    private String user;
    @Column(name = "creation_time")
    @NotNull
    private Date creationTime;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status status;
    @Column(name = "expiration_time")
    @NotNull
    private Date expirationTime;
    @NotBlank
    @Column(name = "session")
    private String session;

}
