package com.travelapp.travel_api.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // กำหนดชื่อตารางเป็น "users"
@Getter
@Setter
@EqualsAndHashCode(of = "id") // เปรียบเทียบ Object โดยใช้ฟิลด์ "id"
@ToString(of = {"id", "email", "displayName"}) // สร้างสตริงโดยใช้ฟิลด์ที่ระบุ
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // กำหนดให้ฟิลด์นี้เป็น Primary Key
    private Long id;

    @Column(nullable = false, unique = true, length = 255) // กำหนดให้ฟิลด์นี้ไม่สามารถเป็นค่า null ได้ และต้องไม่ซ้ำกัน
    private String email;

    @Column(name = "password_hash", nullable = false) // กำหนดให้ฟิลด์นี้ไม่สามารถเป็นค่า null ได้
    private String passwordHash;

    @Column(name = "display_name", length = 100)  
    private String displayName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false) 
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY) // ความสัมพันธ์แบบ One-to-Many กับ Entity Trip
    private List<Trip> trips = new ArrayList<>();

}