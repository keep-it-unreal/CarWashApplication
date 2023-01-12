package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.entity.CauseInterrupt;

@Repository
public interface CauseInterruptRepository extends JpaRepository<CauseInterrupt,Long> {
}
