package org.example.todo_list.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.TodoFormDto;
import org.example.todo_list.entity.Todo;
import org.example.todo_list.entity.TodoStatus;
import org.example.todo_list.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý các HTTP request liên quan đến Todo.
 * Không chứa business logic – chỉ điều phối giữa View và Service.
 */
@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // Số bản ghi mặc định trên mỗi trang
    private static final int DEFAULT_PAGE_SIZE = 5;

    // =========================================================
    // Redirect root về /todos
    // =========================================================

    @GetMapping("/")
    public String redirectRoot() {
        return "redirect:/todos";
    }

    // =========================================================
    // DANH SÁCH TODO (kết hợp search + filter + sort + phân trang)
    // =========================================================

    /**
     * Hiển thị danh sách Todo với các tham số:
     * @param keyword  từ khóa tìm kiếm theo title
     * @param status   lọc theo trạng thái (PENDING / COMPLETED / null = All)
     * @param page     số trang hiện tại (bắt đầu từ 0)
     * @param sortBy   field sắp xếp: createdAt | updatedAt | title
     * @param sortDir  chiều sắp xếp: asc | desc
     */
    @GetMapping
    public String listTodos(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) TodoStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        // Xây dựng Sort theo chiều và field được chọn
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sort);

        // Gọi Service để lấy dữ liệu phân trang
        Page<Todo> todoPage = todoService.findAll(keyword, status, pageable);

        // Đẩy dữ liệu vào Model cho Thymeleaf render
        model.addAttribute("todoPage", todoPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("statuses", TodoStatus.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", todoPage.getTotalPages());

        return "todo/list";
    }

    // =========================================================
    // THÊM MỚI
    // =========================================================

    /**
     * Hiển thị form thêm mới Todo.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("todoForm", new TodoFormDto());
        model.addAttribute("statuses", TodoStatus.values());
        model.addAttribute("formTitle", "Thêm công việc mới");
        model.addAttribute("isEdit", false);
        return "todo/form";
    }

    /**
     * Xử lý lưu Todo mới.
     * @Valid trigger Jakarta Validation, BindingResult chứa lỗi nếu có.
     */
    @PostMapping
    public String createTodo(
            @Valid @ModelAttribute("todoForm") TodoFormDto formDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Nếu có lỗi validation → quay lại form, giữ dữ liệu đã nhập
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", TodoStatus.values());
            model.addAttribute("formTitle", "Thêm công việc mới");
            model.addAttribute("isEdit", false);
            return "todo/form";
        }

        todoService.save(formDto);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm công việc thành công!");
        return "redirect:/todos";
    }

    // =========================================================
    // CHỈNH SỬA
    // =========================================================

    /**
     * Hiển thị form chỉnh sửa Todo với dữ liệu hiện tại điền sẵn.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Todo todo = todoService.findById(id);

        // Map Entity → FormDto để điền sẵn dữ liệu vào form
        TodoFormDto formDto = TodoFormDto.builder()
                .title(todo.getTitle())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .build();

        model.addAttribute("todoForm", formDto);
        model.addAttribute("todoId", id);
        model.addAttribute("statuses", TodoStatus.values());
        model.addAttribute("formTitle", "Chỉnh sửa công việc");
        model.addAttribute("isEdit", true);
        return "todo/form";
    }

    /**
     * Xử lý cập nhật Todo.
     */
    @PostMapping("/{id}")
    public String updateTodo(
            @PathVariable Long id,
            @Valid @ModelAttribute("todoForm") TodoFormDto formDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todoId", id);
            model.addAttribute("statuses", TodoStatus.values());
            model.addAttribute("formTitle", "Chỉnh sửa công việc");
            model.addAttribute("isEdit", true);
            return "todo/form";
        }

        todoService.update(id, formDto);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật công việc thành công!");
        return "redirect:/todos";
    }

    // =========================================================
    // XÓA
    // =========================================================

    /**
     * Xóa Todo theo ID.
     * Dùng POST thay vì DELETE để tương thích với HTML form (HTML5 chỉ hỗ trợ GET/POST).
     */
    @PostMapping("/{id}/delete")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa công việc thành công!");
        return "redirect:/todos";
    }

    // =========================================================
    // TOGGLE TRẠNG THÁI
    // =========================================================

    /**
     * Chuyển đổi trạng thái PENDING ↔ COMPLETED.
     */
    @PostMapping("/{id}/toggle")
    public String toggleStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái!");
        return "redirect:/todos";
    }
}
