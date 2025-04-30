package com.homeinventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "groups") // "groups" because "group" is a reserved word in SQL
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID groupId;

    @Column(nullable = false, unique = true)
    private String groupName;

    @Column(nullable = false)
    private String createdBy;  // later we can store user_id or username
}
