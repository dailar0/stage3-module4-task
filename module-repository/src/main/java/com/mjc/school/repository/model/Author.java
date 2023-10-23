package com.mjc.school.repository.model;

import com.mjc.school.repository.annotation.validation.Length;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Author implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Length(min = 3,max = 15)
    private String name;
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private Set<News> news;

    public boolean addNews(News news) {
        news.setAuthor(this);
        return this.news.add(news);
    }

    public boolean deleteNews(News news) {
        news.setAuthor(null);
        return this.news.remove(news);
    }
}
