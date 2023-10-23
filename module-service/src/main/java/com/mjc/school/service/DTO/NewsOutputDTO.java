package com.mjc.school.service.DTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class NewsOutputDTO {
    private final long id;
    private final String title;
    private final String content;
    private final LocalDateTime createDate;
    private final LocalDateTime lastUpdateDate;
    private final Long authorId;
    private final List<Long> tagIds;
}
