package com.autocommunity.backend.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode
@Table(name = "session")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private UserEntity user;

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

    @Transient
    private Boolean firstRegistration;

}
