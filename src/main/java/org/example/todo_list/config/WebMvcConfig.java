package org.example.todo_list.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình Web MVC.
 * Đăng ký redirect đơn giản từ "/" về "/todos" mà không cần tạo Controller riêng.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Khi truy cập root path "/" → redirect về "/todos"
        registry.addRedirectViewController("/", "/todos");
    }
}
