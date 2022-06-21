package com.autocommunity.backend.entity.map;

import com.autocommunity.backend.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "event")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    public enum PrivacyType {
        PUBLIC,
        SUBSCRIBERS,
        PRIVATE
    }

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        FINISHED,
        DELETED
    }

    @Transient
    public boolean isOwnedBy(UserEntity user) {
        return user.getId().equals(owner.getId());
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "marker_id")
    @NotNull
    private MarkerEntity marker;

    @Column(name = "starting_date")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    @NotNull
    private Date endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @Column(name = "privacy_type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private PrivacyType privacyType;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id")
    @NotNull
    private UserEntity owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_visitor")
    private Set<UserEntity> eventVisitors = new HashSet<>();

}
