package com.mjc.school.repository.dao;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.annotation.validation.Valid;
import com.mjc.school.repository.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorRepositoryJPAImpl implements AuthorRepository {
    private final EntityManager entityManager;


    @Override
    public List<Author> readAll() {
        return entityManager.createQuery("from Author", Author.class).getResultList();
    }

    @Override
    public Optional<Author> readById(Long id) {
        Author author = entityManager.find(Author.class, id);
        return Optional.ofNullable(author);
    }

    @Override
    public Author create(@Valid Author entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Author update(@Valid Author entity) {
        return entityManager.merge(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<Author> authorOptional = readById(id);
        authorOptional.ifPresent(entityManager::remove);
        return authorOptional.isPresent();
    }

    @Override
    public boolean existById(Long id) {
        Optional<Author> authorOptional = readById(id);
        return authorOptional.isPresent();
    }

    @Override
    public Optional<Author> readByNewsId(Long newsId) {
        Optional<Author> authorOptional;
        try {
            Author author = entityManager.createQuery("select a from News n join n.author a where n.id=:newsId", Author.class)
                    .setParameter("newsId", newsId)
                    .getSingleResult();
            authorOptional = Optional.of(author);
        } catch (NoResultException e) {
            authorOptional = Optional.empty();
        }
        return authorOptional;
    }

    @Override
    public Author getReference(Long id) {
        return entityManager.getReference(Author.class, id);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }
}
