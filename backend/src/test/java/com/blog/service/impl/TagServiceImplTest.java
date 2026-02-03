package com.blog.service.impl;

import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    public void testGetAllTags() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Java");
        tags.add(tag1);

        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> foundTags = tagService.getAllTags();

        assertNotNull(foundTags);
        assertEquals(1, foundTags.size());
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    public void testGetTagById() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Java");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Tag foundTag = tagService.getTagById(1L);

        assertNotNull(foundTag);
        assertEquals("Java", foundTag.getName());
        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateTag() {
        Tag tag = new Tag();
        tag.setName("Java");

        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTag(tag);

        assertNotNull(createdTag);
        assertEquals("Java", createdTag.getName());
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    public void testUpdateTag() {
        Tag existingTag = new Tag();
        existingTag.setId(1L);
        existingTag.setName("J2EE");

        Tag updatedTag = new Tag();
        updatedTag.setName("Java EE");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(existingTag));
        when(tagRepository.save(any(Tag.class))).thenReturn(existingTag);

        Tag result = tagService.updateTag(1L, updatedTag);

        assertNotNull(result);
        assertEquals("Java EE", result.getName());
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).save(existingTag);
    }

    @Test
    public void testDeleteTag() {
        doNothing().when(tagRepository).deleteById(1L);

        tagService.deleteTag(1L);

        verify(tagRepository, times(1)).deleteById(1L);
    }
}
