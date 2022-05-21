package com.autocommunity.backend.entity;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Table(name = "session")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionEntity extends BaseEntity {
    public enum Status {
        ACTIVE,
        EXPIRED,
    }

    @ManyToOne
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

}
