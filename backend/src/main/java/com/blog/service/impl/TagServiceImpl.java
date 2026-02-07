package com.blog.service.impl;

import com.blog.dao.TagDao;
import com.blog.entity.Tag;
import com.blog.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagDao tagDao;

    @Override
    public List<Tag> getAllTags() {
        return tagDao.list();
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag = tagDao.getById(id);
        if (tag == null) {
            throw new RuntimeException("Tag not found");
        }
        return tag;
    }

    @Override
    public Tag createTag(Tag tag) {
        tagDao.save(tag);
        return tag;
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag existingTag = tagDao.getById(id);
        if (existingTag == null) {
            throw new RuntimeException("Tag not found");
        }
        existingTag.setName(tag.getName());
        tagDao.updateById(existingTag);
        return existingTag;
    }

    @Override
    public void deleteTag(Long id) {
        tagDao.removeById(id);
    }
}
