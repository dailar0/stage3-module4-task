package com.mjc.school.service;

import com.mjc.school.service.DTO.NewsInputDTO;
import com.mjc.school.service.DTO.TagInputDTO;
import com.mjc.school.service.DTO.TagOutputDTO;
import com.mjc.school.service.exception.EntityNotFoundException;
import com.mjc.school.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagServiceImplTest {

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private NewsService newsService;

    TagInputDTO getValidInputDTO() {
        return new TagInputDTO(1L, "New Tag");
    }

    @Test
    void testCreate() {
        TagInputDTO inputDTO = getValidInputDTO();
        TagOutputDTO created = tagService.create(inputDTO);

        assertNotEquals(created.getId(), inputDTO.getId());
        assertEquals(inputDTO.getName(), created.getName());
    }

    @Test
    void testReadAll() {
        TagInputDTO dto1 = new TagInputDTO(null, "Tag 1");
        TagInputDTO dto2 = new TagInputDTO(null, "Tag 2");

        tagService.create(dto1);
        tagService.create(dto2);

        List<TagOutputDTO> tags = tagService.readAll();

        assertTrue(tags.stream().anyMatch(t -> t.getName().equals("Tag 1")));
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals("Tag 2")));
    }

    @Test
    void testReadById() {
        TagInputDTO dto = new TagInputDTO(null, "New Tag");
        TagOutputDTO created = tagService.create(dto);

        TagOutputDTO read = tagService.readById(created.getId());

        assertEquals(created.getId(), read.getId());
        assertEquals(created.getName(), read.getName());
    }

    @Test
    void testUpdate() {
        TagInputDTO dto = new TagInputDTO(null, "Original Name");
        TagOutputDTO created = tagService.create(dto);

        TagInputDTO updateDto = new TagInputDTO(created.getId(), "Updated Name");
        TagOutputDTO updated = tagService.update(updateDto);

        assertEquals(created.getId(), updated.getId());
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDelete() {
        TagInputDTO dto = new TagInputDTO(null, "Tag to delete");
        TagOutputDTO created = tagService.create(dto);

        boolean deleted = tagService.deleteById(created.getId());

        assertTrue(deleted);
        assertThrows(EntityNotFoundException.class, () -> tagService.readById(created.getId()));
    }

    @Test
    void testNotFound() {
        assertThrows(EntityNotFoundException.class, () -> tagService.readById(0L));

        TagInputDTO dto = new TagInputDTO(0L, "Tag");
        assertThrows(EntityNotFoundException.class, () -> tagService.update(dto));

        assertFalse(tagService.deleteById(0L));
    }

    @Test
    void readAllByNewsId() {
        TagInputDTO inputDTO1 = new TagInputDTO(null, "ByNewsId1");
        TagInputDTO inputDTO2 = new TagInputDTO(null, "ByNewsId2");
        TagInputDTO inputDTO3 = new TagInputDTO(null, "ByNewsId3");
        TagOutputDTO tagOutputDTO1 = tagService.create(inputDTO1);
        TagOutputDTO tagOutputDTO2 = tagService.create(inputDTO2);
        tagService.create(inputDTO3);
        NewsInputDTO newsInputDTO = new NewsInputDTO(null,
                "validTitle",
                "validContent"
                , null
                , List.of(tagOutputDTO1.getId(), tagOutputDTO2.getId()));
        long newsId = newsService.create(newsInputDTO).getId();

        List<TagOutputDTO> read = tagService.readAllByNewsID(newsId);

        assertTrue(read.stream()
                .map(TagOutputDTO::getId)
                .toList()
                .containsAll(List.of(tagOutputDTO1.getId(),
                        tagOutputDTO2.getId())));

    }

    @Test
    void validationTest() {
        TagInputDTO nullInputDTO = new TagInputDTO(null, null);
        assertThrows(ValidationException.class, () -> tagService.create(nullInputDTO));
        TagInputDTO shortNameInputDTO = new TagInputDTO(null, "as");
        assertThrows(ValidationException.class, () -> tagService.create(shortNameInputDTO));

    }

}