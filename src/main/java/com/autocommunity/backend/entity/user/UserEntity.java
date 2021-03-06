package com.autocommunity.backend.entity.user;

import com.autocommunity.backend.entity.map.EventEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode
@Table(name = "user_t")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    private UUID id;

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


    @ManyToMany(fetch = FetchType.EAGER,
    mappedBy = "eventVisitors")
    private Set<EventEntity> events = new HashSet<>();
}
