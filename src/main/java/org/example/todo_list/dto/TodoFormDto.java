package org.example.todo_list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.todo_list.entity.TodoStatus;

/**
 * DTO dùng để nhận dữ liệu từ form HTML (thêm mới / chỉnh sửa Todo).
 * Tách biệt khỏi Entity để tránh expose trực tiếp cấu trúc DB ra ngoài.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoFormDto {

    /**
     * Tên công việc.
     * - Không được để trống.
     * - Độ dài từ 3 đến 100 ký tự.
     */
    @NotBlank(message = "Tên công việc không được để trống")
    @Size(min = 3, max = 100, message = "Tên công việc phải từ 3 đến 100 ký tự")
    private String title;

    /**
     * Mô tả công việc.
     * - Không bắt buộc.
     * - Tối đa 500 ký tự.
     */
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    /**
     * Trạng thái công việc: PENDING hoặc COMPLETED.
     * Dùng khi chỉnh sửa để thay đổi trạng thái qua form.
     */
    private TodoStatus status;
}
