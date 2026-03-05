package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.model.*;
import com.example.itassetmanagement.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final AssetService assetService;
    private final AssignmentService assignmentService;
    private final MaintenanceService maintenanceService;
    private final FeedbackService feedbackService;
    private final TicketService ticketService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(EmployeeService employeeService,
                           AssetService assetService,
                           AssignmentService assignmentService,
                           MaintenanceService maintenanceService,
                           FeedbackService feedbackService,
                           TicketService ticketService,
                           PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.assetService = assetService;
        this.assignmentService = assignmentService;
        this.maintenanceService = maintenanceService;
        this.feedbackService = feedbackService;
        this.ticketService = ticketService;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================= DASHBOARD =========================
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("employeeCount", employeeService.countAll());
        model.addAttribute("assetCount", assetService.countAll());
        model.addAttribute("assignmentCount", assignmentService.countAll());

        model.addAttribute("totalTickets", ticketService.getAllTickets().size());
        model.addAttribute("openTickets", ticketService.getOpenTickets().size());
        model.addAttribute("totalEmployees", employeeService.countAll());
        model.addAttribute("totalAssets", assetService.countAll());

        return "admin/dashboard";
    }

    // ========================= EMPLOYEES =========================
    @GetMapping("/employees")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "admin/employees/list";
    }

    @GetMapping({"/employees/create", "/employees/add"})
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("isEdit", false);
        return "admin/employees/form";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute Employee employee, Model model) {
        employeeService.saveEmployee(employee, passwordEncoder);
        return "redirect:/admin/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("isEdit", true);
        return "admin/employees/form";
    }

    @GetMapping("/employees/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "admin/employees/view";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return "redirect:/admin/employees";
    }

    // ======================= PENDING USERS (SUPPORTS BOTH OLD & NEW URL) =======================
    @GetMapping({"/users/pending", "/employees/pending"})
    public String listPendingUsers(Model model) {
        model.addAttribute("employees", employeeService.getPendingEmployees());
        return "admin/employees/pending";  // Always loads your beautiful dynamic template
    }

    @PostMapping({"/users/pending/approve/{id}", "/employees/pending/approve/{id}"})
    public String approveUser(@PathVariable Long id) {
        employeeService.approveEmployee(id);
        return "redirect:/admin/employees/pending?approved=true";  // Clean redirect
    }

    // ========================= ASSETS =========================
    @GetMapping("/assets")
    public String listAssets(Model model) {
        model.addAttribute("assets", assetService.getAllAssets());
        return "admin/assets/list";
    }

    @GetMapping({"/assets/create", "/assets/add"})
    public String createAssetForm(Model model) {
        model.addAttribute("asset", new Asset());
        return "admin/assets/form";
    }

    @PostMapping("/assets/create")
    public String createAsset(@ModelAttribute Asset asset) {
        assetService.createAsset(asset);
        return "redirect:/admin/assets";
    }

    @GetMapping("/assets/edit/{id}")
    public String editAssetForm(@PathVariable Long id, Model model) {
        model.addAttribute("asset", assetService.findById(id));
        return "admin/assets/form";
    }

    @PostMapping("/assets/edit/{id}")
    public String updateAsset(@PathVariable Long id, @ModelAttribute Asset asset) {
        asset.setId(id);
        assetService.updateAsset(asset);
        return "redirect:/admin/assets";
    }

    @GetMapping("/assets/delete/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return "redirect:/admin/assets";
    }

    @GetMapping("/assets/details/{id}")
    public String assetDetails(@PathVariable Long id, Model model) {
        model.addAttribute("asset", assetService.findById(id));
        model.addAttribute("history", assetService.getHistoryByAssetId(id));
        return "admin/assets/details";
    }

    // ========================= ASSIGNMENTS =========================
    @GetMapping("/assignments")
    public String listAssignments(Model model) {
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        return "admin/assignments/list";
    }

    @GetMapping({"/assignments/create", "/assignments/add"})
    public String createAssignmentForm(Model model) {
        model.addAttribute("assignment", new Assignment());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("assets", assetService.getAvailableAssets());
        return "admin/assignments/form";
    }

    @PostMapping("/assignments/create")
    public String createAssignment(@ModelAttribute Assignment assignment) {
        assignmentService.createAssignment(assignment);
        return "redirect:/admin/assignments";
    }

    @GetMapping("/assignments/delete/{id}")
    public String deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return "redirect:/admin/assignments";
    }

    @GetMapping("/assignments/return/{id}")
    public String returnAssetAdmin(@PathVariable Long id) {
        assignmentService.returnAssetFromAdmin(id);
        return "redirect:/admin/assignments";
    }

    // ========================= FEEDBACK =========================
    @GetMapping("/feedbacks")
    public String listFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacksOrdered());
        return "admin/feedback/list";
    }

    @GetMapping("/feedbacks/delete/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteById(id);
        return "redirect:/admin/feedbacks";
    }

    // ========================= MAINTENANCE =========================
    @GetMapping("/maintenance")
    public String listMaintenance(Model model) {
        model.addAttribute("maintenances", maintenanceService.getAllOrderedByDate());
        return "admin/maintenance/list";
    }

    @GetMapping("/maintenance/new")
    public String createMaintenanceForm(Model model) {
        model.addAttribute("maintenance", new Maintenance());
        model.addAttribute("assets", assetService.getAllAssets());
        return "admin/maintenance/form";
    }

    @GetMapping("/maintenance/edit/{id}")
    public String editMaintenanceForm(@PathVariable Long id, Model model) {
        model.addAttribute("maintenance", maintenanceService.findById(id));
        model.addAttribute("assets", assetService.getAllAssets());
        return "admin/maintenance/form";
    }

    @PostMapping("/maintenance/save")
    public String saveMaintenance(@ModelAttribute Maintenance maintenance,
                                  @RequestParam("asset.id") Long assetId,
                                  Model model) {
        maintenanceService.saveMaintenance(maintenance, assetId, model);
        return "redirect:/admin/maintenance?success";
    }

    @GetMapping("/maintenance/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteById(id);
        return "redirect:/admin/maintenance";
    }

    // ========================= DEBUG =========================
    @GetMapping("/debug/users")
    public String debugUsers(Model model) {
        model.addAttribute("users", employeeService.getAllEmployees());
        return "admin/debug/users";
    }

    @PostMapping("/debug/reset-users")
    public String resetUsers() {
        employeeService.resetAllUsers(passwordEncoder);
        return "redirect:/admin/debug/users?reset=success";
    }

    // ========================= TICKETS (ADMIN) =========================
    @GetMapping("/tickets")
    public String listAllTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "admin/tickets/list";
    }

    @GetMapping("/tickets/{id}")
    public String viewTicketDetail(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.getTicketWithDetails(id));
        model.addAttribute("comment", new TicketComment());
        return "admin/tickets/view";
    }
}