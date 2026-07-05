package org.example.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo_list.dto.TodoFormDto;
import org.example.todo_list.entity.Todo;
import org.example.todo_list.entity.TodoStatus;
import org.example.todo_list.exception.ResourceNotFoundException;
import org.example.todo_list.repository.TodoRepository;
import org.example.todo_list.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Implementation của TodoService chứa toàn bộ business logic.
 *
 * @Transactional: đảm bảo tính toàn vẹn dữ liệu khi ghi (write operations).
 * @Slf4j: tích hợp logging qua Lombok.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    /**
     * Lấy danh sách Todo với tìm kiếm, lọc theo status và phân trang.
     * Logic:
     * - Nếu keyword rỗng và status null → lấy tất cả.
     * - Nếu có status và không có keyword → lọc theo status.
     * - Nếu có keyword và không có status → search theo title.
     * - Nếu có cả hai → kết hợp search + filter.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Todo> findAll(String keyword, TodoStatus status, Pageable pageable) {
        boolean hasKeyword = StringUtils.hasText(keyword);
        boolean hasStatus = status != null;

        if (hasKeyword && hasStatus) {
            // Kết hợp tìm kiếm theo title + lọc theo status
            return todoRepository.findByTitleContainingIgnoreCaseAndStatus(keyword, status, pageable);
        } else if (hasKeyword) {
            // Chỉ tìm kiếm theo title
            return todoRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        } else if (hasStatus) {
            // Chỉ lọc theo status: dùng keyword rỗng để tái sử dụng query method
            return todoRepository.findByTitleContainingIgnoreCaseAndStatus("", status, pageable);
        } else {
            // Lấy tất cả không lọc
            return todoRepository.findAll(pageable);
        }
    }

    /**
     * Tìm Todo theo ID, ném exception nếu không tồn tại.
     */
    @Override
    @Transactional(readOnly = true)
    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", id));
    }

    /**
     * Tạo mới Todo từ form data.
     * - Status mặc định là PENDING nếu không truyền.
     */
    @Override
    @Transactional
    public void save(TodoFormDto formDto) {
        Todo todo = Todo.builder()
                .title(formDto.getTitle().trim())
                .description(formDto.getDescription())
                .status(TodoStatus.PENDING) // Mặc định khi tạo mới là PENDING
                .build();

        todoRepository.save(todo);
        log.info("Tạo mới Todo thành công: title='{}'", todo.getTitle());
    }

    /**
     * Cập nhật thông tin Todo: title, description, status.
     * Dùng setter thay vì tạo object mới để Hibernate chỉ update các field thay đổi.
     */
    @Override
    @Transactional
    public void update(Long id, TodoFormDto formDto) {
        Todo todo = findById(id);

        todo.setTitle(formDto.getTitle().trim());
        todo.setDescription(formDto.getDescription());

        // Chỉ cập nhật status nếu giá trị được truyền vào (tránh null overwrite)
        if (formDto.getStatus() != null) {
            todo.setStatus(formDto.getStatus());
        }

        todoRepository.save(todo);
        log.info("Cập nhật Todo ID={} thành công", id);
    }

    /**
     * Xóa Todo theo ID. Kiểm tra tồn tại trước khi xóa.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại → ném ResourceNotFoundException nếu không có
        Todo todo = findById(id);
        todoRepository.delete(todo);
        log.info("Đã xóa Todo ID={}, title='{}'", id, todo.getTitle());
    }

    /**
     * Toggle trạng thái: PENDING → COMPLETED, COMPLETED → PENDING.
     */
    @Override
    @Transactional
    public void toggleStatus(Long id) {
        Todo todo = findById(id);

        TodoStatus newStatus = (todo.getStatus() == TodoStatus.PENDING)
                ? TodoStatus.COMPLETED
                : TodoStatus.PENDING;

        todo.setStatus(newStatus);
        todoRepository.save(todo);
        log.info("Toggle trạng thái Todo ID={}: {} → {}", id, todo.getStatus(), newStatus);
    }
}
