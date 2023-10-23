package com.mjc.school.repository;

import com.mjc.school.repository.model.News;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends BaseRepository<News,Long> {
    News getReference(Long id);
    Optional<News> readNewsWithTagsByNewsId(Long id);

    void flush();

    List<News> readAllByFilter(Specification<News> specification);

}
