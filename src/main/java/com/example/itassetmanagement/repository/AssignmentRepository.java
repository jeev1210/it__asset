package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByReturnedFalse();
    List<Assignment> findByReturnedTrue();

    @Query("SELECT a FROM Assignment a JOIN FETCH a.employee e JOIN FETCH a.asset ast")
    List<Assignment> findAllWithEmployeeAndAsset();

    // employee active assignments
    List<Assignment> findByEmployeeIdAndReturnedFalse(Long employeeId);

    @Query("SELECT a FROM Assignment a WHERE a.employee.id = :employeeId AND a.returned = false")
    List<Assignment> findActiveByEmployeeId(@Param("employeeId") Long employeeId);
}
