package com.mjc.school.service;

import com.mjc.school.service.DTO.*;
import com.mjc.school.service.exception.EntityNotFoundException;
import com.mjc.school.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsServiceImplTest {
    @Autowired
    private NewsService newsService;
    @Autowired
    private AuthorServiceImpl authorService;
    @Autowired
    TagServiceImpl tagService;
    private final Random random = new Random();

    private NewsInputDTO getValidDTO() {
        AuthorInputDTO authorInputDTO = new AuthorInputDTO(random.nextLong(), "validName");
        AuthorOutputDTO authorOutputDTO = authorService.create(authorInputDTO);
        return new NewsInputDTO(random.nextLong(),
                "validTitle",
                "validContent",
                authorOutputDTO.getId(),
                null);
    }


    @Test
    void create() {
        NewsInputDTO newsInputDTO = getValidDTO();

        NewsOutputDTO createdNews = newsService.create(newsInputDTO);

        assertNotEquals(createdNews.getId(), newsInputDTO.getId());
        assertEquals(newsInputDTO.getTitle(), createdNews.getTitle());
        assertEquals(newsInputDTO.getContent(), createdNews.getContent());
        assertEquals(newsInputDTO.getAuthorId(), createdNews.getAuthorId());
        assertNotNull(createdNews.getCreateDate());
        assertNotNull(createdNews.getLastUpdateDate());
    }

    @Test
    void getAll() {
        NewsInputDTO newsInputDTO1 = getValidDTO();
        NewsInputDTO newsInputDTO2 = getValidDTO();

        NewsOutputDTO created1 = newsService.create(newsInputDTO1);
        NewsOutputDTO created2 = newsService.create(newsInputDTO2);
        List<Long> idList = newsService.readAll().stream()
                .map(NewsOutputDTO::getId)
                .toList();

        assertTrue(idList.containsAll(
                List.of(created1.getId(), created2.getId())));
    }

    @Test
    void getById() {
        NewsInputDTO newsInputDTO = getValidDTO();
        NewsOutputDTO createdNews = newsService.create(newsInputDTO);

        NewsOutputDTO retrievedNews = newsService.readById(createdNews.getId());

        assertEquals(createdNews.getId(), retrievedNews.getId());
    }

    @Test
    void update() throws InterruptedException {
        NewsInputDTO newsInputDTO = getValidDTO();
        NewsOutputDTO createdNews = newsService.create(newsInputDTO);
        NewsInputDTO updatedNewsInputDTO = new NewsInputDTO(createdNews.getId(),
                "Updated test news",
                "This is an updated test news",
                newsInputDTO.getAuthorId(),
                null);

        Thread.sleep(30);
        NewsOutputDTO updatedNews = newsService.update(updatedNewsInputDTO);

        assertEquals(updatedNewsInputDTO.getId(), updatedNews.getId());
        assertEquals(updatedNewsInputDTO.getTitle(), updatedNews.getTitle());
        assertEquals(updatedNewsInputDTO.getContent(), updatedNews.getContent());
        assertEquals(updatedNewsInputDTO.getAuthorId(), updatedNews.getAuthorId());
        assertNotEquals(createdNews.getLastUpdateDate(), updatedNews.getLastUpdateDate());
    }

    @Test
    void delete() {
        NewsInputDTO newsInputDTO = getValidDTO();
        NewsOutputDTO createdNews = newsService.create(newsInputDTO);

        newsService.deleteById(createdNews.getId());
        List<NewsOutputDTO> newsList = newsService.readAll();

        assertFalse(newsList.contains(createdNews));
        assertThrows(EntityNotFoundException.class, () -> newsService.readById(createdNews.getId()));
    }

    @Test
    public void testCreateNewsWithNonExistingAuthor() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                "validTitle",
                "validContent",
                Long.MAX_VALUE,
                null);
        assertThrows(EntityNotFoundException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void testCreateNewsWithNonExistingTag() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                "validTitle",
                "validContent",
                null,
                List.of(12323213L, 12321321L));
        assertThrows(EntityNotFoundException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void testUpdateNonExistingNews() {
        NewsInputDTO validDTO = getValidDTO();
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                "validTitle",
                "validContent",
                validDTO.getAuthorId(), null);
        assertThrows(EntityNotFoundException.class, () -> newsService.update(newsInputDTO));
    }

    @Test
    public void testDeleteNonExistingNews() {
        assertFalse(newsService.deleteById(Long.MAX_VALUE));
    }

    @Test
    public void testValidation() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(1L, "a", "b", null, null);
        assertThrows(ValidationException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    void testFilterByTitle() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(null, "uniqueTitle", "validContent", null, null);
        NewsInputFilterDTO filter = new NewsInputFilterDTO(null, null, null, "uniqueTitle", null);

        newsService.create(newsInputDTO);
        newsService.create(new NewsInputDTO(null, "Other Title", "Content", null, null));
        List<NewsOutputDTO> results = newsService.readAllFiltered(filter);

        assertEquals(1,results.size());
    }

    @Test
    void testFilterByAuthor() {
        AuthorInputDTO authorInputDTO = new AuthorInputDTO(null, "uniqueAuthor");
        Long authorID = authorService.create(authorInputDTO).getId();
        NewsInputDTO validDTO = new NewsInputDTO(null, "validTitle", "validContent", authorID, null);
        NewsInputFilterDTO filter = new NewsInputFilterDTO(null, null, "uniqueAuthor", null, null);

        newsService.create(validDTO);
        newsService.create(new NewsInputDTO(null, "Other Title", "Content", null, null));
        List<NewsOutputDTO> results = newsService.readAllFiltered(filter);

        assertEquals(1,results.size());

    }

    @Test
    void testFilterByTags() {
        TagInputDTO uniqueTag1 = new TagInputDTO(null, "uniqueTag1");
        TagInputDTO uniqueTag2 = new TagInputDTO(null, "uniqueTag2");
        TagOutputDTO tagOutputDTO1 = tagService.create(uniqueTag1);
        TagOutputDTO tagOutputDTO2 = tagService.create(uniqueTag2);
        NewsInputDTO newsInputDTO = new NewsInputDTO(null, "validTitle", "validContent", null, List.of(tagOutputDTO1.getId(), tagOutputDTO2.getId()));
        NewsInputFilterDTO filter = new NewsInputFilterDTO(List.of("uniqueTag1","uniqueTag2"), null, null, null, null);

        newsService.create(newsInputDTO);
        newsService.create(new NewsInputDTO(null, "Other Title", "Content", null, null));
        List<NewsOutputDTO> results = newsService.readAllFiltered(filter);

        assertEquals(1,results.size());
    }
    @Test
    void testFilterByTagsId() {
        TagInputDTO uniqueTag1 = new TagInputDTO(null, "uniqueTagByID1");
        TagInputDTO uniqueTag2 = new TagInputDTO(null, "uniqueTagByID2");
        TagOutputDTO tagOutputDTO1 = tagService.create(uniqueTag1);
        TagOutputDTO tagOutputDTO2 = tagService.create(uniqueTag2);
        NewsInputDTO newsInputDTO = new NewsInputDTO(null, "validTitle", "validContent", null, List.of(tagOutputDTO1.getId(), tagOutputDTO2.getId()));
        NewsInputFilterDTO filter = new NewsInputFilterDTO(null,
                List.of(tagOutputDTO1.getId().toString(), tagOutputDTO2.getId().toString()),
                null,
                null,
                null);

        newsService.create(newsInputDTO);
        newsService.create(new NewsInputDTO(null, "Other Title", "Content", null, null));
        List<NewsOutputDTO> results = newsService.readAllFiltered(filter);

        assertEquals(1,results.size());
    }


    @Test
    void testFilterMultiple() {
        AuthorInputDTO authorDto = new AuthorInputDTO(null, "MultiAuthor");
        Long authorId = authorService.create(authorDto).getId();
        TagInputDTO tagDto = new TagInputDTO(null, "MultiTag");
        Long tagId = tagService.create(tagDto).getId();
        NewsInputDTO newsDto = new NewsInputDTO(null, "MultiTitle", "Content", authorId, List.of(tagId));
        NewsInputFilterDTO filter = new NewsInputFilterDTO(
                List.of("MultiTag"), null, "MultiAuthor", "MultiTitle", null);

        newsService.create(newsDto);
        newsService.create(new NewsInputDTO(null, "Other Title", "Content", authorId, null));
        List<NewsOutputDTO> results = newsService.readAllFiltered(filter);

        assertEquals(1, results.size());
    }

}