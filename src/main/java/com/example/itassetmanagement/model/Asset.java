package com.example.itassetmanagement.model;

import com.example.itassetmanagement.model.enums.AssetStatus;
import com.example.itassetmanagement.model.enums.AssetType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String assetTag;

    @Column(nullable = false, length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private AssetType type;

    @Column(length = 50)
    private String brand;

    @Column(length = 100)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;

    @Column(length = 500)
    private String description;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    // Link to employee who currently holds this asset
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =================== CONSTRUCTORS ===================
    public Asset() {
        this.status = AssetStatus.AVAILABLE;
    }

    // =================== GETTERS & SETTERS ===================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public AssetType getType() { return type; }
    public void setType(AssetType type) { this.type = type; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public AssetStatus getStatus() { return status; }
    public void setStatus(AssetStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public Employee getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Employee assignedTo) { this.assignedTo = assignedTo; }

    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", assetTag='" + assetTag + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", type=" + type +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", status=" + status +
                '}';
    }
}
