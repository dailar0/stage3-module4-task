package com.mjc.school.controller;

import com.mjc.school.service.DTO.NewsInputDTO;
import com.mjc.school.service.DTO.NewsInputFilterDTO;
import com.mjc.school.service.DTO.NewsOutputDTO;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.mapping.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsControllerWebImpl implements BaseController<NewsInputDTO, NewsOutputDTO, Long> {

    private final NewsService newsService;
    private final NewsMapper mapper;

    @GetMapping
    public List<NewsOutputDTO> readAllFiltered(
            @RequestParam(name = "tagName", required = false) String[] tagNames,
            @RequestParam(name = "tagId", required = false) String[] tagIds,
            @RequestParam(name = "authorName", required = false) String authorName,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "content", required = false) String content) {
        NewsInputFilterDTO newsInputFilterDTO = mapper.rawDataToFilteredDTO(authorName, tagNames, tagIds, title, content);
        return newsService.readAllFiltered(newsInputFilterDTO);
    }


    @Override
    public List<NewsOutputDTO> readAll() {
        return newsService.readAll();
    }

    @GetMapping("/{id}")
    @Override
    public NewsOutputDTO readById(@PathVariable Long id) {
        return newsService.readById(id);
    }

    @PostMapping
    @Override
    public NewsOutputDTO create(@RequestBody NewsInputDTO createRequest) {
        return newsService.create(createRequest);
    }

    @PutMapping
    @Override
    public NewsOutputDTO update(@RequestBody NewsInputDTO updateRequest) {
        return newsService.update(updateRequest);
    }

    @Override
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Long id) {
        return newsService.deleteById(id);
    }
}
