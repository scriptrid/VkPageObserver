package com.example.vkpageobserver.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pages")
@Getter
@Setter
public class PageEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "location")
    private String location;


    @OneToMany(mappedBy = "page", orphanRemoval = true)
    private Set<ChangeEntity> changes = new LinkedHashSet<>();


    @ManyToMany(mappedBy = "observingPages")
    private Set<UserEntity> users = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PageEntity that = (PageEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public void addUser(UserEntity user) {
        users.add(user);
    }
}