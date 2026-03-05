package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.model.*;
import com.example.itassetmanagement.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TicketService ticketService;
    private final AssetService assetService;
    private final AssignmentService assignmentService;
    private final AssetHistoryService assetHistoryService;

    public EmployeeController(EmployeeService employeeService,
                              TicketService ticketService,
                              AssetService assetService,
                              AssignmentService assignmentService,
                              AssetHistoryService assetHistoryService) {
        this.employeeService = employeeService;
        this.ticketService = ticketService;
        this.assetService = assetService;
        this.assignmentService = assignmentService;
        this.assetHistoryService = assetHistoryService;
    }

    private Employee getCurrentEmployee(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return null;

        Employee emp = employeeService.getByEmail(username);
        if (emp != null && model != null) {
            model.addAttribute("username", username);
            model.addAttribute("fullName", emp.getFullName());
            model.addAttribute("department", emp.getDepartment());
            model.addAttribute("profile", emp);
        }
        return emp;
    }

    @GetMapping({"", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        Employee emp = getCurrentEmployee(session, model);
        if (emp == null) return "redirect:/auth/login";

        model.addAttribute("openTickets", employeeService.countOpenTicketsByEmail(emp.getEmail()));
        model.addAttribute("assignedAssets", assetService.getAssetsByAssignedEmployee(emp).size());
        model.addAttribute("pendingAssignments", assignmentService.getPendingByEmployeeId(emp.getId()).size());
        model.addAttribute("feedbacks", employeeService.getFeedbacksByEmployeeId(emp.getId()));

        return "employee/dashboard";
    }

    @GetMapping("/my-tickets")
    public String myTickets(HttpSession session, Model model) {
        Employee emp = getCurrentEmployee(session, model);
        if (emp == null) return "redirect:/auth/login";
        model.addAttribute("tickets", ticketService.getTicketsByCreatedByEmail(emp.getEmail()));
        return "employee/my-tickets";
    }

    @GetMapping("/tickets/create")
    public String createTicketPage(HttpSession session, Model model) {
        if (getCurrentEmployee(session, model) == null) return "redirect:/auth/login";
        return "employee/create-ticket";
    }

    @PostMapping("/tickets/create")
    public String saveTicket(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String priority,
                             @RequestParam(required = false) String initial_comment,
                             @RequestParam(required = false) MultipartFile[] attachments,
                             HttpSession session) throws IOException {

        Employee emp = getCurrentEmployee(session, null);
        if (emp == null) return "redirect:/auth/login";

        Ticket ticket = ticketService.createTicket(emp, title, description, priority);

        if (initial_comment != null && !initial_comment.trim().isEmpty()) {
            ticketService.addComment(ticket, emp, initial_comment.trim());
        }

        if (attachments != null && attachments.length > 0) {
            ticketService.uploadAttachments(ticket, emp, attachments);
        }

        return "redirect:/employee/ticket/" + ticket.getId() + "?success=true";
    }

    @GetMapping("/ticket/{id}")
    public String viewTicket(@PathVariable Long id, HttpSession session, Model model) {
        Employee emp = getCurrentEmployee(session, model);
        if (emp == null) return "redirect:/auth/login";

        Ticket ticket = ticketService.getTicketWithDetails(id);

        if (ticket == null || !ticket.getCreatedBy().getId().equals(emp.getId())) {
            return "redirect:/employee/my-tickets?error=not_allowed";
        }

        model.addAttribute("ticket", ticket);
        return "employee/ticket-view";
    }

    @PostMapping("/ticket/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             HttpSession session) {
        Employee emp = getCurrentEmployee(session, null);
        if (emp == null) return "redirect:/auth/login";

        Ticket ticket = ticketService.findById(id);
        if (ticket == null || !ticket.getCreatedBy().getId().equals(emp.getId())) {
            return "redirect:/employee/my-tickets?error=not_authorized";
        }

        ticketService.addComment(ticket, emp, content);
        return "redirect:/employee/ticket/" + id + "?success=comment_added";
    }

    @PostMapping("/ticket/{id}/upload")
    public String uploadAttachment(@PathVariable Long id,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session) throws IOException {
        Employee emp = getCurrentEmployee(session, null);
        if (emp == null) return "redirect:/auth/login";

        Ticket ticket = ticketService.findById(id);
        if (ticket == null || !ticket.getCreatedBy().getId().equals(emp.getId())) {
            return "redirect:/employee/my-tickets?error=not_authorized";
        }

        ticketService.uploadAttachment(ticket, emp, file);
        return "redirect:/employee/ticket/" + id + "?success=file_uploaded";
    }

    @GetMapping("/my-assets")
    public String myAssets(HttpSession session, Model model) {
        Employee emp = getCurrentEmployee(session, model);
        if (emp == null) return "redirect:/auth/login";
        model.addAttribute("assets", assetService.getAssetsByAssignedEmployee(emp));
        return "employee/my-assets";
    }

    @GetMapping("/asset/{id}")
    public String viewAsset(@PathVariable Long id, HttpSession session, Model model) {
        Employee emp = getCurrentEmployee(session, model);
        if (emp == null) return "redirect:/auth/login";

        Asset asset = assetService.findById(id);

        if (asset == null || !asset.getAssignedTo().getId().equals(emp.getId())) {
            return "redirect:/employee/my-assets?error=not_allowed";
        }

        model.addAttribute("asset", asset);

        List<AssetHistory> history = assetHistoryService.getByAssetId(id);
        model.addAttribute("history", history);

        return "employee/asset-details";
    }

    @GetMapping("/my-assignments")
    public String myAssignments(HttpSession session, Model model) {
        Employee emp = getCurrentEmployee(session, model);
        if (emp == null) return "redirect:/auth/login";
        model.addAttribute("assignments", assignmentService.getPendingByEmployeeId(emp.getId()));
        return "employee/my-assignments";
    }

    @PostMapping("/assignments/return/{id}")
    public String returnAsset(@PathVariable Long id, HttpSession session) {
        Employee emp = getCurrentEmployee(session, null);
        if (emp == null) return "redirect:/auth/login";

        Assignment assignment = assignmentService.findById(id);
        if (assignment == null || !assignment.getEmployee().getId().equals(emp.getId())) {
            return "redirect:/employee/my-assignments?error=not_authorized";
        }

        assignmentService.returnAsset(assignment, emp);
        return "redirect:/employee/my-assignments?success=returned";
    }
}