package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {
}