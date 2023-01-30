package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.entity.WorkShedule;


public interface WorkSheduleRepository extends JpaRepository<WorkShedule, Long> {
}
