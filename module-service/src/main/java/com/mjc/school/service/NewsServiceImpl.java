package com.mjc.school.service;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.DTO.NewsInputDTO;
import com.mjc.school.service.DTO.NewsInputFilterDTO;
import com.mjc.school.service.DTO.NewsOutputDTO;
import com.mjc.school.service.exception.EntityNotFoundException;
import com.mjc.school.service.mapping.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final NewsMapper mapper;

    @Transactional
    @Override
    public NewsOutputDTO create(@NonNull NewsInputDTO createDTO) {
        if (createDTO.getId() != null)
            createDTO.setId(null);

        Collection<Long> tagIds = createDTO.getTagIds();
        Set<Tag> tags = tagIds != null ? getTagReferencesByIds(tagIds) : null;

        Long authorId = createDTO.getAuthorId();
        Author author = authorId != null ? authorRepository.getReference(authorId) : null;

        News news = mapper.mapCreateToNews(createDTO, tags, author);
        News savedNews = newsRepository.create(news);

        try {
            newsRepository.flush();
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw generateEntityNotFoundException((ConstraintViolationException) e.getCause());
            }
            throw e;
        }

        return mapper.mapNewsToOutput(savedNews);
    }

    @Transactional
    public NewsOutputDTO update(@NonNull NewsInputDTO updateRequest) {
        News fetched = newsRepository
                .readNewsWithTagsByNewsId(updateRequest.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("News with ID %d not found.", updateRequest.getId())));

        Set<Tag> tags = getTagReferencesByIds(updateRequest.getTagIds());
        Author author = authorRepository.getReference(updateRequest.getAuthorId());

        News mapped = mapper.mapCreateToNews(updateRequest, tags, author);

        fetched.setContent(mapped.getContent());
        fetched.setTitle(mapped.getTitle());

        Set<Tag> fetchedTag = fetched.getTags();
        fetchedTag.clear();
        fetchedTag.addAll(tags);

        fetched.setAuthor(author);

        try {
            newsRepository.flush();
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw generateEntityNotFoundException((ConstraintViolationException) e.getCause());
            }
            throw e;
        }

        return mapper.mapNewsToOutput(fetched);


    }

    @Override
    public List<NewsOutputDTO> readAll() {
        return mapper.mapNewsToOutputDTOList(newsRepository.readAll());
    }

    @Override
    public NewsOutputDTO readById(Long id) {
        return newsRepository.readById(id)
                .map(mapper::mapNewsToOutput)
                .orElseThrow(() -> new EntityNotFoundException(String.format("News with ID %d not found.", id)));
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return newsRepository.deleteById(id);
    }


    @Override
    public List<NewsOutputDTO> readAllFiltered(NewsInputFilterDTO filterDTO) {

        if (filterDTO == null) {

            List<News> news = newsRepository.readAll();
            return mapper.mapNewsToOutputDTOList(news);
        }

        Specification<News> specification = (root, query, criteriaBuilder) ->
        {
            List<String> tagNames = filterDTO.getTagNames();
            List<String> tagIds = filterDTO.getTagIds();
            String authorName = filterDTO.getAuthorName();
            String title = filterDTO.getTitle();
            String content = filterDTO.getContent();

            List<Predicate> predicates = new ArrayList<>();

            if (tagNames != null && tagNames.size() > 0) {
                Join<News, Tag> tagJoin = root.join("tags");
                predicates.add(tagJoin.get("name").in((tagNames)));
            }

            if (tagIds != null && tagIds.size() > 0) {
                Join<News, Tag> tagJoin = root.join("tags");
                predicates.add(tagJoin.get("id").in(tagIds));
            }

            if (authorName != null) {
                Join<News, Author> authorJoin = root.join("author");
                predicates.add(criteriaBuilder.equal(authorJoin.get("name"), authorName));
            }

            if (title != null) {
                predicates.add(criteriaBuilder.equal(root.get("title"), title));
            }

            if (content != null) {
                predicates.add(criteriaBuilder.equal(root.get("content"), content));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };

        List<News> news = newsRepository.readAllByFilter(specification);
        return mapper.mapNewsToOutputDTOList(news);

    }

    private Set<Tag> getTagReferencesByIds(Collection<Long> tagIds) {
        Set<Tag> tags = new HashSet<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            tagIds.stream()
                    .map(tagRepository::getReference)
                    .forEach(tags::add);
        }
        return tags;
    }

    private EntityNotFoundException generateEntityNotFoundException(ConstraintViolationException e) {
        SQLException sqlException = e.getSQLException();
        String message = sqlException.getMessage();
        if (message.contains("tag_id") || message.contains("author_id")) {
            Pattern pattern = Pattern.compile("\\(\\d+\\)");
            Matcher matcher = pattern.matcher(message);

            String id;
            if (matcher.find()) {
                id = matcher.group().substring(1, matcher.group().length() - 1);
            } else {
                throw new IllegalStateException("pg error parse trouble: description format changed", e);
            }

            if (message.contains("tag_id"))
                return new EntityNotFoundException(
                        String.format("Tag with ID %s not found.", id));
            if (message.contains("author_id"))
                return new EntityNotFoundException(
                        String.format("Author with ID %s not found.", id));
        }
        throw e;
    }

}
