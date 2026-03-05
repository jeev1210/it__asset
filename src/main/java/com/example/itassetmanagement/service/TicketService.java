package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.*;
import com.example.itassetmanagement.model.enums.TicketStatus;
import com.example.itassetmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EmployeeRepository employeeRepository;
    private final TicketCommentRepository commentRepository;
    private final TicketAttachmentRepository attachmentRepository;

    // Upload directory - creates "uploads" folder in project root
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public TicketService(TicketRepository ticketRepository,
                         EmployeeRepository employeeRepository,
                         TicketCommentRepository commentRepository,
                         TicketAttachmentRepository attachmentRepository) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
    }

    // ========================
    // Existing methods (unchanged)
    // ========================

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketWithDetails(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Force lazy loading of collections
        if (ticket.getComments() != null) {
            ticket.getComments().size();
        }
        if (ticket.getAttachments() != null) {
            ticket.getAttachments().size();
        }
        return ticket;
    }

    public void prepareCreateForm(org.springframework.ui.Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("employees", employeeRepository.findAll());
    }

    public List<Ticket> getTicketsByCreatedByEmail(String email) {
        return ticketRepository.findByCreatedByEmail(email);
    }

    public List<Ticket> getTicketsByEmail(String email) {
        return ticketRepository.findByCreatedByEmail(email);
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<Ticket> getOpenTickets() {
        return ticketRepository.findByStatus(TicketStatus.OPEN);
    }

    // ========================
    // CREATE TICKET - EMPLOYEE (now returns the saved ticket!)
    // ========================

    /**
     * Creates a ticket for the current logged-in employee
     * @return the persisted Ticket entity (with ID)
     */
    @Transactional
    public Ticket createTicket(Employee creator, String title, String description, String priority) {
        Ticket ticket = Ticket.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .createdBy(creator)
                .build();

        return ticketRepository.save(ticket); // ← Now returns the saved ticket!
    }

    // Admin version (kept unchanged)
    @Transactional
    public void createTicket(String title, String description, String priority, Long employeeId) {
        Employee creator = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Ticket ticket = Ticket.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .createdBy(creator)
                .build();

        ticketRepository.save(ticket);
    }

    // ========================
    // ADD COMMENT
    // ========================

    @Transactional
    public void addComment(Ticket ticket, Employee author, String content) {
        if (content == null || content.trim().isEmpty()) {
            return;
        }
        TicketComment comment = TicketComment.builder()
                .ticket(ticket)
                .author(author)
                .content(content.trim())
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    @Transactional
    public void addComment(Long ticketId, String content, Long employeeId) {
        Ticket ticket = findById(ticketId);
        Employee author = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        addComment(ticket, author, content);
    }

    // ========================
    // UPLOAD SINGLE ATTACHMENT (existing)
    // ========================

    @Transactional
    public void uploadAttachment(Ticket ticket, Employee uploader, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return;
        }

        // Ensure upload directory exists
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = Objects.requireNonNullElse(file.getOriginalFilename(), "unknown_file");
        String fileName = LocalDateTime.now()
                .toString()
                .replace(":", "-")
                .replace(".", "-") + "_" + originalName;

        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());

        TicketAttachment attachment = TicketAttachment.builder()
                .ticket(ticket)
                .uploadedBy(uploader)
                .fileName(originalName)
                .filePath("/uploads/" + fileName)
                .uploadedAt(LocalDateTime.now())
                .build();

        attachmentRepository.save(attachment);
    }

    // ========================
    // NEW: UPLOAD MULTIPLE ATTACHMENTS AT ONCE (used during ticket creation)
    // ========================

    @Transactional
    public void uploadAttachments(Ticket ticket, Employee uploader, MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return;
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                uploadAttachment(ticket, uploader, file); // Reuse existing logic
            }
        }
    }

    // Optional: Admin overload
    @Transactional
    public void uploadAttachments(Ticket ticket, Employee uploader, List<MultipartFile> files) throws IOException {
        if (files != null) {
            uploadAttachments(ticket, uploader, files.toArray(new MultipartFile[0]));
        }
    }
}