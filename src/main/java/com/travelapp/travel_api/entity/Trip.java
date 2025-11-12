package com.travelapp.travel_api.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.OffsetDateTime;


@Entity // สร้างตารางในฐานข้อมูล
@Table(name = "trips") // กำหนดชื่อตารางเป็น "trips"
@Getter 
@Setter
@EqualsAndHashCode(of = "id") // เปรียบเทียบ Object โดยใช้ฟิลด์ "id"
@ToString(exclude = "author") // ไม่รวมฟิลด์ "author" ในการสร้างสตริง
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // กำหนดให้ฟิลด์นี้เป็น Primary Key
    private Long id; 

    @Column(nullable = false) // กำหนดให้ฟิลด์นี้ไม่สามารถเป็นค่า null ได้
    private String title;

    @Column(columnDefinition = "TEXT") // กำหนดชนิดข้อมูลเป็น TEXT ในฐานข้อมูล
    private String description;

    @ElementCollection 
    @CollectionTable(name = "trip_photos", joinColumns = @JoinColumn(name = "trip_id")) // สร้างตารางย่อยสำหรับเก็บรูปภาพ
    @Column(name = "photo_url") 
    private java.util.List<String> photos = new java.util.ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "trip_tags", joinColumns = @JoinColumn(name = "trip_id")) // สร้างตารางย่อยสำหรับเก็บแท็ก
    @Column(name = "tag")
    private java.util.List<String> tags = new java.util.ArrayList<>();

    private Double latitude; 
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY) // ความสัมพันธ์แบบ Many-to-One กับ Entity User
    @JoinColumn(name = "author_id") 
    private User author;

    @CreationTimestamp 
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime updatedAt;

    public void setPhotos(java.util.List<String> photos) { 
    this.photos = (photos != null) ? new java.util.ArrayList<>(photos) : new java.util.ArrayList<>();
} 

public void setTags(java.util.List<String> tags) {
    this.tags = (tags != null) ? new java.util.ArrayList<>(tags) : new java.util.ArrayList<>();
}

}