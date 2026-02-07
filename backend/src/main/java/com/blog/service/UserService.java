package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.dao.mapper.UserMapper;
import com.blog.entity.User;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("Username already exists");
        }
        queryWrapper.clear();
        queryWrapper.eq("email", user.getEmail());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }

    public Optional<User> findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }

    public User findById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    public List<User> findAll() {
        return userMapper.selectList(null);
    }
}