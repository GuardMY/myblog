package com.blog.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dao.mapper.UserMapper;
import com.blog.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends ServiceImpl<UserMapper, User> {

}
