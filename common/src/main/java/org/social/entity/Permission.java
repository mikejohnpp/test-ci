package org.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "permissionID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "featureID", nullable = false)
    private Feature featureID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actionID", nullable = false)
    private Action actionID;

}