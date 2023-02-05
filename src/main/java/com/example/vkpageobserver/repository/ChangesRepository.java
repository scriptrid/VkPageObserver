package com.example.vkpageobserver.repository;

import com.example.vkpageobserver.model.entity.ChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangesRepository extends JpaRepository<ChangeEntity, Integer> {
}
