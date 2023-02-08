package ru.scriptrid.vkpageobserver.repository;

import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {
}