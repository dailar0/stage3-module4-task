package com.mjc.school.repository.dao;

import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.annotation.validation.Valid;
import com.mjc.school.repository.model.News;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NewsRepositoryJpaImpl implements NewsRepository {
    private final EntityManager entityManager;

    @Override
    public List<News> readAll() {
        return entityManager.createQuery("from News", News.class).getResultList();
    }

    @Override
    public Optional<News> readById(Long id) {
        News news = entityManager.find(News.class, id);
        return Optional.ofNullable(news);
    }

    @Override
    public News create(@Valid News entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public News update(@Valid News entity) {
        return entityManager.merge(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<News> newsOptional = readById(id);
        newsOptional.ifPresent(entityManager::remove);
        return newsOptional.isPresent();
    }

    @Override
    public boolean existById(Long id) {
        Query query = entityManager.createQuery("select count (n) from News n where id=:id");
        query.setParameter("id", id);
        return ((long) query.getSingleResult()) > 0;
    }

    @Override
    public News getReference(Long id) {
        return entityManager.getReference(News.class, id);
    }

    @Override
    public Optional<News> readNewsWithTagsByNewsId(Long id) {
        try {
            News result = entityManager.createQuery("from News n left join fetch n.tags where n.id=:id", News.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<News> readAllByFilter(Specification<News> specification) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> cq = cb.createQuery(News.class);
        Root<News> root = cq.from(News.class);
        Predicate predicate = specification.toPredicate(root, cq, cb);
        cq.distinct(true).select(root).where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }
}
