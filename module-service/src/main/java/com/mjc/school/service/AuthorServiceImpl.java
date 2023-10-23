package com.mjc.school.service;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.service.DTO.AuthorInputDTO;
import com.mjc.school.service.DTO.AuthorOutputDTO;
import com.mjc.school.service.exception.EntityNotFoundException;
import com.mjc.school.service.mapping.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    @Override
    public List<AuthorOutputDTO> readAll() {
        return mapper.mapAuthorToOutputList(repository.readAll());
    }

    @Override
    public AuthorOutputDTO readById(Long id) {
        return repository.readById(id)
                .map(mapper::mapAuthorToOutput)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Author with ID %d not found.", id)));
    }

    @Transactional
    @Override
    public AuthorOutputDTO create(AuthorInputDTO createRequest) {
                if (createRequest.getId() != null)
            createRequest.setId(null);

        Author input = mapper.mapCreateToAuthor(createRequest);
        Author created = repository.create(input);
        return mapper.mapAuthorToOutput(created);
    }

    @Transactional
    @Override
    public AuthorOutputDTO update(AuthorInputDTO updateRequest) {
        Author author = repository
                .readById(updateRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Author with ID %s not found.", updateRequest.getId())));
        Author input = mapper.mapCreateToAuthor(updateRequest);
        author.setName(input.getName());
        repository.flush();
        return mapper.mapAuthorToOutput(author);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Transactional
    public boolean deleteByIdCascadeSetNull(Long id){
        repository
                .readById(id)
                .ifPresent(entity-> entity.getNews().forEach(entity::deleteNews));
        return repository.deleteById(id);
    }

    @Override
    public AuthorOutputDTO readByNewsId(Long newsId) {
        Author author = repository.readByNewsId(newsId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Author for News with ID %d not found.", newsId)));
        return mapper.mapAuthorToOutput(author);
    }
}
