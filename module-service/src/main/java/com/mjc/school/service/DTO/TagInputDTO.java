package com.mjc.school.service.DTO;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class TagInputDTO {
    @ConstructorProperties({"id", "name"})
    public TagInputDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private final String name;

}
