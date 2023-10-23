package com.mjc.school.controller;

import com.mjc.school.service.AuthorServiceImpl;
import com.mjc.school.service.DTO.AuthorInputDTO;
import com.mjc.school.service.DTO.AuthorOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorControllerWebImpl implements BaseController<AuthorInputDTO, AuthorOutputDTO, Long> {
    private final AuthorServiceImpl authorService;

    @GetMapping
    @Override
    public List<AuthorOutputDTO> readAll() {
        return authorService.readAll();
    }

    @GetMapping("/{id}")
    @Override
    public AuthorOutputDTO readById(@PathVariable Long id) {
        return authorService.readById(id);
    }

    @GetMapping(params = "newsId")
    public AuthorOutputDTO readByNewsId(@RequestParam(name = "newsId") Long newsId) {
        return authorService.readByNewsId(newsId);
    }

    @PostMapping
    @Override
    public AuthorOutputDTO create(@RequestBody AuthorInputDTO createRequest) {
        return authorService.create(createRequest);
    }

    @PutMapping
    @Override
    public AuthorOutputDTO update(@RequestBody AuthorInputDTO updateRequest) {
        return authorService.update(updateRequest);
    }

    @DeleteMapping("/{id}")
    @Override
    public boolean deleteById(@PathVariable Long id) {
        return authorService.deleteById(id);
    }
}
