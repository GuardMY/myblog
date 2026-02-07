package com.blog.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.dao.UserDao;
import com.blog.entity.User;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Resource
    private UserDao userDao;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已存在admin用户
        QueryWrapper<User> adminWrapper = new QueryWrapper<>();
        adminWrapper.eq("username", "admin");
        if (userDao.count(adminWrapper) == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");
            userDao.save(admin);
            System.out.println("Admin user created successfully: username=admin, password=admin123");
        }

        // 检查是否已存在测试用户
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", "user");
        if (userDao.count(userWrapper) == 0) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setRole("USER");
            userDao.save(user);
            System.out.println("Test user created successfully: username=user, password=user123");
        }
    }
}
