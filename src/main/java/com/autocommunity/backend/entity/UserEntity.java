package com.autocommunity.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Table(name = "\"user\"")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {


    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    @Size(max = 50)
    private String id;

    @Column(name = "email", unique = true)
    @NotNull
    @Size(max = 200)
    private String email;

    @Column(name = "username", unique = true)
    @NotNull
    @Size(max = 50)
    private String username;

    @Column(name = "password_hash")
    @NotNull
    @Size(max = 300)
    private String passwordHash;

    @Column(name = "created")
    @CreationTimestamp
    private Date created;

    @Column(name = "changed")
    @UpdateTimestamp
    private Date changed;
}
