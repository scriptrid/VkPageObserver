package com.example.vkpageobserver.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "changes")
@Getter
@Setter
public class ChangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "change_entity_seq")
    @SequenceGenerator(name = "change_entity_seq")
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "page_id", nullable = false)
    private PageEntity page;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChangeEntity that = (ChangeEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}