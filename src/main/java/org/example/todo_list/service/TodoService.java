package org.example.todo_list.service;

import org.example.todo_list.dto.TodoFormDto;
import org.example.todo_list.entity.Todo;
import org.example.todo_list.entity.TodoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface định nghĩa các nghiệp vụ (business logic) liên quan đến Todo.
 * Controller chỉ phụ thuộc vào interface này, không phụ thuộc vào implementation.
 */
public interface TodoService {

    /**
     * Lấy danh sách Todo có hỗ trợ tìm kiếm, lọc và phân trang.
     *
     * @param keyword  từ khóa tìm kiếm trong title (có thể rỗng)
     * @param status   trạng thái cần lọc (null = hiển thị tất cả)
     * @param pageable thông tin phân trang và sắp xếp
     * @return trang dữ liệu Todo
     */
    Page<Todo> findAll(String keyword, TodoStatus status, Pageable pageable);

    /**
     * Tìm một Todo theo ID.
     *
     * @param id ID của Todo
     * @return Todo entity
     * @throws org.example.todo_list.exception.ResourceNotFoundException nếu không tìm thấy
     */
    Todo findById(Long id);

    /**
     * Tạo mới một Todo từ form data.
     *
     * @param formDto dữ liệu từ form đã được validate
     */
    void save(TodoFormDto formDto);

    /**
     * Cập nhật một Todo đã tồn tại.
     *
     * @param id      ID của Todo cần cập nhật
     * @param formDto dữ liệu mới từ form đã được validate
     * @throws org.example.todo_list.exception.ResourceNotFoundException nếu không tìm thấy
     */
    void update(Long id, TodoFormDto formDto);

    /**
     * Xóa một Todo theo ID.
     *
     * @param id ID của Todo cần xóa
     * @throws org.example.todo_list.exception.ResourceNotFoundException nếu không tìm thấy
     */
    void delete(Long id);

    /**
     * Chuyển đổi trạng thái Todo: PENDING ↔ COMPLETED.
     *
     * @param id ID của Todo cần toggle
     * @throws org.example.todo_list.exception.ResourceNotFoundException nếu không tìm thấy
     */
    void toggleStatus(Long id);
}
