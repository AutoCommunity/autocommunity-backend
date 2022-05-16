package com.autocommunity.backend.entity;

import com.autocommunity.backend.security.MyUserDetails;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends BaseEntity{

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
    private String password;

    @Column(name = "created")
    @CreationTimestamp
    private Date created;

    @Column(name = "changed")
    @UpdateTimestamp
    private Date changed;

    public UserDetails toUserDetails(){
        return MyUserDetails.builder().username(username).password(password).build();
    }
}
