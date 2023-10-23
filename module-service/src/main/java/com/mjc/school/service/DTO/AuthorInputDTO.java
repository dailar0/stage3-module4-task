package com.mjc.school.service.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@EqualsAndHashCode
public class AuthorInputDTO {
    @Setter
    private Long id;
    private final String name;

    @ConstructorProperties({"id", "name"})
    public AuthorInputDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
