package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {

    // Fetch all comments for a ticket, ordered by creation date ascending
    List<TicketComment> findAllByTicketIdOrderByCreatedAtAsc(Long ticketId);

}
