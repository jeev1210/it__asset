package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.Ticket;
import com.example.itassetmanagement.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // 🔥 Get all tickets created by a specific employee (by email)
    @Query("SELECT t FROM Ticket t WHERE t.createdBy.email = :email ORDER BY t.createdAt DESC")
    List<Ticket> findByCreatedByEmail(@Param("email") String email);

    // 🔥 Get all tickets with a specific status
    @Query("SELECT t FROM Ticket t WHERE t.status = :status ORDER BY t.createdAt DESC")
    List<Ticket> findByStatus(@Param("status") TicketStatus status);

    // 🔥 Get all open tickets (you can pass OPEN, IN_PROGRESS, etc.)
    @Query("SELECT t FROM Ticket t WHERE t.status IN :openStatuses ORDER BY t.createdAt DESC")
    List<Ticket> findOpenTickets(@Param("openStatuses") List<TicketStatus> openStatuses);

    // 🔥 Count of open tickets
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status IN :openStatuses")
    Long countOpenTickets(@Param("openStatuses") List<TicketStatus> openStatuses);

    // 🔥 Recent tickets of an employee (ordered by creation date)
    @Query("SELECT t FROM Ticket t WHERE t.createdBy.email = :email ORDER BY t.createdAt DESC")
    List<Ticket> findRecentTicketsByEmail(@Param("email") String email);

}
