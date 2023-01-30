package com.example.vkpageobserver.repository;

import com.example.vkpageobserver.model.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {
    @Override
    Optional<PageEntity> findById(Integer integer);

    @Override
    boolean existsById(Integer integer);
}