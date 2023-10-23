package com.mjc.school.service;

import com.mjc.school.service.DTO.AuthorInputDTO;
import com.mjc.school.service.DTO.AuthorOutputDTO;
import com.mjc.school.service.DTO.NewsInputDTO;
import com.mjc.school.service.DTO.NewsOutputDTO;
import com.mjc.school.service.exception.EntityNotFoundException;
import com.mjc.school.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AuthorServiceTest {
    @Autowired
    private AuthorServiceImpl authorService;
    @Autowired
    private NewsService newsService;
    private final Random random = new Random();

    private AuthorInputDTO getValidDTO() {
        return new AuthorInputDTO(random.nextLong(), "valid name");
    }

    @Test
    void create() {
        AuthorInputDTO validDTO = getValidDTO();

        AuthorOutputDTO authorOutputDTO = authorService.create(validDTO);

        assertNotEquals(validDTO.getId(), authorOutputDTO.getId());
        assertEquals(validDTO.getName(), authorOutputDTO.getName());
        assertNotNull(authorOutputDTO.getCreateDate());
        assertNotNull(authorOutputDTO.getLastUpdateDate());
    }

    @Test
    void update() throws InterruptedException {
        AuthorInputDTO validDTO = getValidDTO();
        AuthorOutputDTO created = authorService.create(validDTO);

        AuthorInputDTO updatedDTO = new AuthorInputDTO(created.getId(), "updated");
        Thread.sleep(30);
        AuthorOutputDTO updated = authorService.update(updatedDTO);

        assertEquals(updatedDTO.getId(), updated.getId());
        assertEquals(updatedDTO.getName(), updated.getName());
        assertNotEquals(created.getLastUpdateDate(), updated.getLastUpdateDate());
    }

    @Test
    void readAll() {
        AuthorInputDTO dto1 = getValidDTO();
        AuthorInputDTO dto2 = getValidDTO();
        AuthorOutputDTO created1 = authorService.create(dto1);
        AuthorOutputDTO created2 = authorService.create(dto2);

        List<AuthorOutputDTO> authorOutputDTOS = authorService.readAll();
        List<Long> readIds = authorOutputDTOS.stream()
                .map(AuthorOutputDTO::getId)
                .toList();

        assertTrue(readIds.containsAll(
                List.of(created1.getId(), created2.getId())));
    }

    @Test
    void readById() {
        AuthorInputDTO validDTO = getValidDTO();
        AuthorOutputDTO authorOutputDTO = authorService.create(validDTO);

        AuthorOutputDTO loaded = authorService.readById(authorOutputDTO.getId());

        assertEquals(authorOutputDTO.getId(), loaded.getId());
        assertEquals(authorOutputDTO.getName(), loaded.getName());
    }

    @Test
    void deleteById() {
        AuthorInputDTO authorInputDTO = getValidDTO();
        AuthorOutputDTO authorOutputDTO = authorService.create(authorInputDTO);
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                "valid title",
                "valid content",
                authorOutputDTO.getId(),
                null);
        NewsOutputDTO newsOutputDTO = newsService.create(newsInputDTO);

        authorService.deleteById(authorOutputDTO.getId());

        assertThrows(EntityNotFoundException.class, () -> authorService.readById(authorOutputDTO.getId()));
        assertThrows(EntityNotFoundException.class, () -> newsService.readById(newsOutputDTO.getId()));
    }

    @Test
    public void testUpdateNonExistingAuthor() {
        AuthorInputDTO authorInputDTO = new AuthorInputDTO(Long.MAX_VALUE, "valid title");
        assertThrows(EntityNotFoundException.class, () -> authorService.update(authorInputDTO));
    }

    @Test
    public void testDeleteNonExistingAuthor() {
        assertFalse(authorService.deleteById(Long.MAX_VALUE));
    }

    @Test
    public void readByNewsId() {
        AuthorInputDTO validDTO = getValidDTO();
        AuthorOutputDTO created = authorService.create(validDTO);
        NewsInputDTO newsInputDTO = new NewsInputDTO(
                null, "validTitle", "validContent", created.getId(), null);
        NewsOutputDTO newsOutputDTO = newsService.create(newsInputDTO);

        AuthorOutputDTO read = authorService.readByNewsId(newsOutputDTO.getId());

        assertEquals(created.getId(), read.getId());
    }

    @Test
    public void readNullByNewsId() {
        AuthorInputDTO validDTO = getValidDTO();
        authorService.create(validDTO);
        NewsInputDTO newsInputDTO = new NewsInputDTO(
                null, "validTitle", "validContent", null, null);
        NewsOutputDTO newsOutputDTO = newsService.create(newsInputDTO);

        assertThrows(EntityNotFoundException.class,() -> authorService.readByNewsId(newsOutputDTO.getId()));
    }

    @Test
    public void testValidation() {
        AuthorInputDTO authorInputDTO = new AuthorInputDTO(1L, "a");
        assertThrows(ValidationException.class, () -> authorService.create(authorInputDTO));
    }
}