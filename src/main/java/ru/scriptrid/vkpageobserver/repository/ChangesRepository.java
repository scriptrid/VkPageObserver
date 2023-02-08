package ru.scriptrid.vkpageobserver.repository;

import ru.scriptrid.vkpageobserver.model.entity.ChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangesRepository extends JpaRepository<ChangeEntity, Integer> {
}
