package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"major")
@Getter
@Setter
public class Major extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private String image;

    @OneToMany(mappedBy = "major")
    private List<Student> students = new ArrayList<>();

    @PreRemove
    private void removeMajorFromStudent() {
        for (Student student : students) {
            student.setMajor(null);
        }
    }
}
