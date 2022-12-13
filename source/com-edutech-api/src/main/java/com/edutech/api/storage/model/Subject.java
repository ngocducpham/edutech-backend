package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"subject")
@Getter
@Setter
public class Subject extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private String code;

    @ManyToMany(mappedBy = "subjects")
    List<Teacher> teachers = new ArrayList<>();
}
