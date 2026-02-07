package com.blog.controller;

import com.blog.entity.User;
import com.blog.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    // 管理员查看所有用户
    @GetMapping
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();
        if (!currentUser.getRole().equals("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 查看用户信息（管理员可查看所有，普通用户只能查看自己）
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();
        User targetUser = userService.findById(id);

        // 检查权限：管理员或用户查看自己
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(targetUser, HttpStatus.OK);
    }

    // 编辑用户信息（管理员可编辑所有，普通用户只能编辑自己）
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();

        // 检查权限：管理员或用户编辑自己
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // 普通用户不能修改角色
        if (!currentUser.getRole().equals("ADMIN")) {
            user.setRole(currentUser.getRole());
        }

        user.setId(id);
        User updatedUser = userService.update(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // 删除用户（只有管理员可以删除）
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();
        if (!currentUser.getRole().equals("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userService.delete(id);
        return new ResponseEntity<>(Map.of("message", "User deleted successfully"), HttpStatus.OK);
    }

    // 查看当前登录用户信息
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
