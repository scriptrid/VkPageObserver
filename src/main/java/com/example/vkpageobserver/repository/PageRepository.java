package com.example.vkpageobserver.repository;

import com.example.vkpageobserver.model.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {
}