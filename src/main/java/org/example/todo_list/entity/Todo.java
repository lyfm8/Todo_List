package org.example.todo_list.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity đại diện cho bảng "todo" trong database.
 * Dùng Code First: Hibernate sẽ tự tạo/update bảng dựa trên class này.
 */
@Entity
@Table(name = "todo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên công việc: bắt buộc, 3-100 ký tự.
     * nullable=false → NOT NULL ở DB.
     * length=100 → VARCHAR(100).
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * Mô tả công việc: tùy chọn, tối đa 500 ký tự.
     * columnDefinition="TEXT" để hỗ trợ chuỗi dài.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Trạng thái công việc: PENDING hoặc COMPLETED.
     * EnumType.STRING → lưu dưới dạng chuỗi thay vì số nguyên, dễ đọc hơn trong DB.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TodoStatus status = TodoStatus.PENDING;

    /**
     * Thời điểm tạo: tự động gán khi insert, không thay đổi sau đó.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Thời điểm cập nhật lần cuối: tự động cập nhật mỗi khi record thay đổi.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
