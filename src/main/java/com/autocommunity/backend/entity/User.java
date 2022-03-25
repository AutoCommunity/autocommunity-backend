package com.autocommunity.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Table(name = "\"user\"")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity{

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
