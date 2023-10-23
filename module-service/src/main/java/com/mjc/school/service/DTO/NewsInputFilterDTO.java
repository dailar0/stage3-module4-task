package com.mjc.school.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class NewsInputFilterDTO {
    private List<String> tagNames;
    private List<String> tagIds;
    private String authorName;
    private String title;
    private String content;
}
