package org.social.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @Column(name = "actionID", nullable = false)
    private Integer id;

    @Column(name = "actionName", nullable = false, length = 50)
    private String actionName;

}