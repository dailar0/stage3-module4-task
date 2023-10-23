package com.mjc.school.service.DTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AuthorOutputDTO {
    private final Long id;
    private final String name;
    private final LocalDateTime createDate;
    private final LocalDateTime lastUpdateDate;
}
