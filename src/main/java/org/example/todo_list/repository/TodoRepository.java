package org.example.todo_list.repository;

import org.example.todo_list.entity.Todo;
import org.example.todo_list.entity.TodoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository kế thừa JpaRepository để thao tác với bảng "todo".
 * Spring Data JPA tự sinh implementation tại runtime.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * Tìm kiếm theo title (không phân biệt hoa thường) + lọc theo status + phân trang.
     * Dùng khi người dùng vừa search vừa filter theo status.
     *
     * @param keyword  từ khóa tìm kiếm trong title
     * @param status   trạng thái cần lọc
     * @param pageable thông tin phân trang và sắp xếp
     * @return danh sách Todo phân trang
     */
    Page<Todo> findByTitleContainingIgnoreCaseAndStatus(
            String keyword, TodoStatus status, Pageable pageable
    );

    /**
     * Tìm kiếm theo title (không phân biệt hoa thường) + phân trang.
     * Dùng khi người dùng chỉ search mà không filter status (All).
     *
     * @param keyword  từ khóa tìm kiếm trong title
     * @param pageable thông tin phân trang và sắp xếp
     * @return danh sách Todo phân trang
     */
    Page<Todo> findByTitleContainingIgnoreCase(
            String keyword, Pageable pageable
    );
}
