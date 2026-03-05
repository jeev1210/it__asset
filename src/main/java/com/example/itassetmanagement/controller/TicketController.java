package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.model.TicketComment;
import com.example.itassetmanagement.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "tickets/list";
    }

    // FIXED: Use correct method name
    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.getTicketWithDetails(id));
        model.addAttribute("comment", new TicketComment());
        return "tickets/view";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        ticketService.prepareCreateForm(model);
        return "tickets/form";
    }

    @PostMapping("/save")
    public String saveTicket(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String priority,
                             @RequestParam Long employeeId) {
        ticketService.createTicket(title, description, priority, employeeId);
        return "redirect:/tickets";
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             @RequestParam Long employeeId) {
        ticketService.addComment(id, content, employeeId);
        return "redirect:/tickets/" + id;
    }

    @GetMapping("/my")
    public String myTickets(@RequestParam String email, Model model) {
        model.addAttribute("tickets", ticketService.getTicketsByEmail(email));
        return "tickets/list";
    }

    @GetMapping("/open")
    public String openTickets(Model model) {
        model.addAttribute("tickets", ticketService.getOpenTickets());
        return "tickets/list";
    }
}