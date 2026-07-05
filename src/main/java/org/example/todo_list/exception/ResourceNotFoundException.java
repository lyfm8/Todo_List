package org.example.todo_list.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception ném ra khi không tìm thấy resource theo ID.
 * @ResponseStatus(HttpStatus.NOT_FOUND) → Spring tự trả về HTTP 404 nếu không có handler riêng.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s không tồn tại với ID: %d", resourceName, id));
    }
}
