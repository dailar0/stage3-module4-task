package com.mjc.school.repository.dao;

import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.annotation.validation.Valid;
import com.mjc.school.repository.model.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryJpaImpl implements TagRepository {
    private final EntityManager entityManager;

    public TagRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Tag> readAll() {
        return entityManager.createQuery("from Tag", Tag.class).getResultList();
    }

    @Override
    public Optional<Tag> readById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    @Override
    public Tag create(@Valid Tag entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Tag update(@Valid Tag entity) {
        return entityManager.merge(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<Tag> tagOptional = readById(id);
        tagOptional.ifPresent(entityManager::remove);
        return tagOptional.isPresent();
    }

    @Override
    public boolean existById(Long id) {
        Optional<Tag> tagOptional = readById(id);
        return tagOptional.isPresent();
    }

    @Override
    public Tag getReference(Long id) {
        return entityManager.getReference(Tag.class, id);
    }

    public Collection<Tag> readByIdIn(Collection<Long> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> root = cq.from(Tag.class);
        cq.select(root).where(root.get("id").in(ids));
        TypedQuery<Tag> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Tag> readAllByNewsId(Long newsId) {
        return entityManager.createQuery("select t from News n join n.tags t where n.id=:newsId", Tag.class)
                .setParameter("newsId", newsId)
                .getResultList();
    }
}
