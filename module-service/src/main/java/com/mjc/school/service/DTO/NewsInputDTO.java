package com.mjc.school.service.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@EqualsAndHashCode
public class NewsInputDTO {
    @Setter
    private Long id;
    private final String title;
    private final String content;
    private final Long authorId;
    private final Collection<Long> tagIds = new ArrayList<>();

    @ConstructorProperties({"id", "title", "content", "authorId", "tagIds"})
    public NewsInputDTO(Long id, String title, String content, Long authorId, Collection<Long> tagIds) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        if (tagIds != null) this.tagIds.addAll(tagIds);
    }
}
