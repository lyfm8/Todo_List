# 📝 Quản lý công việc (Todo List)

Đây là một ứng dụng Web Quản lý Công việc (Todo List) được xây dựng bằng hệ sinh thái **Java & Spring Boot**. Ứng dụng cung cấp giao diện người dùng đơn giản, trực quan để quản lý các công việc hàng ngày với đầy đủ các tính năng tìm kiếm, phân trang và trạng thái.

## 🚀 Công Nghệ Sử Dụng

**Backend:**
- **Java 17**
- **Spring Boot 3.3.5** (Web MVC, Data JPA, Validation)
- **Hibernate / JPA** (Code-first approach)
- **Lombok** (Giảm thiểu code boilerplate)
- **spring-dotenv** (Quản lý biến môi trường)

**Frontend:**
- **Thymeleaf** (Template Engine rendering UI từ Server)
- **HTML5 / CSS3**
- **Bootstrap 5** (Thiết kế Responsive, Modal, UI Components)
- **Bootstrap Icons**

**Database & Deploy:**
- **MySQL 8.0** (Database chính thức triển khai trên Aiven Cloud)
- **Docker** (Multi-stage build cho việc deploy)

---

## 🛠️ Cấu Trúc Project

Ứng dụng tuân thủ kiến trúc chuẩn **MVC Layered Architecture**:

```text
src/main/java/org/example/todo_list/
├── config/        # Cấu hình Spring (ví dụ: Redirect routes)
├── controller/    # Tầng giao tiếp HTTP (Xử lý request, điều hướng dữ liệu ra View)
├── service/       # Tầng Business Logic (Service Interfaces & Implementations)
├── repository/    # Tầng Data Access (Spring Data JPA giao tiếp Database)
├── entity/        # Tầng Model (JPA Entities mapping với các bảng MySQL)
├── dto/           # Data Transfer Objects (Hứng dữ liệu từ HTML forms, validations)
└── exception/     # Tầng xử lý lỗi tuỳ chỉnh (ResourceNotFoundException)

src/main/resources/
├── templates/     # Các file HTML Thymeleaf (Views)
└── application.properties # Cấu hình ứng dụng Spring Boot
```

---

## ✨ Danh Sách Chức Năng

- [x] **Hiển thị danh sách công việc**: Danh sách hiển thị dưới dạng bảng trực quan.
- [x] **Thêm / Chỉnh sửa công việc**: Validate dữ liệu đầu vào chuẩn (tên không rỗng, giới hạn ký tự).
- [x] **Xóa công việc**: Có Modal xác nhận an toàn trước khi xóa (tránh xóa nhầm).
- [x] **Đổi trạng thái công việc**: Nút nhấn (Toggle) chuyển đổi nhanh giữa *Pending* và *Completed*.
- [x] **Tìm kiếm & Lọc**: 
  - Ô search theo tiêu đề công việc.
  - Dropdown lọc theo trạng thái (Pending / Completed / All).
- [x] **Phân trang & Sắp xếp**: Phân trang mặc định 5 bản ghi/trang, hỗ trợ sắp xếp theo Ngày tạo, Cập nhật, hoặc Tên.

---

## ⚙️ Hướng Dẫn Chạy Local (Môi trường phát triển)

Dự án này sử dụng biến môi trường (Environment Variables) để bảo mật thông tin kết nối Database. Vui lòng làm theo các bước sau để chạy trên máy tính cá nhân của bạn:

### Bước 1: Chuẩn bị Môi trường
- Cài đặt **Java JDK 17** trở lên.
- Cài đặt **Maven** (hoặc dùng bộ Maven Wrapper `./mvnw` có sẵn trong project).

### Bước 2: Cấu hình Database Credentials
File chứa mật khẩu DB thực tế (`.env`) không được đưa lên Git. Để ứng dụng chạy được, bạn cần tạo file này:

1.
2. Mở file `.env` và điền thông tin Database được cung cấp:

### Bước 3: Chạy ứng dụng

Mở Terminal (hoặc Command Prompt / PowerShell) tại thư mục gốc của project và chạy lệnh:

```bash
# Trên terminal
.\mvnw spring-boot:run
```

### Bước 4: Truy cập ứng dụng
Sau khi Terminal báo `Started TodoListApplication in ... seconds`, hãy mở trình duyệt web và truy cập vào:
👉 **[http://localhost:8081](http://localhost:8081)**

*(Lưu ý: Ứng dụng đã được cấu hình chạy ở cổng 8081 trong quá trình phát triển để tránh xung đột cổng).*
