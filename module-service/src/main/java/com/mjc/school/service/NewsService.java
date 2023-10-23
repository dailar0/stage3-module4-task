package com.mjc.school.service;

import com.mjc.school.service.DTO.NewsInputDTO;
import com.mjc.school.service.DTO.NewsInputFilterDTO;
import com.mjc.school.service.DTO.NewsOutputDTO;

import java.util.List;

public interface NewsService extends BaseService<NewsInputDTO, NewsOutputDTO, Long> {
    List<NewsOutputDTO> readAllFiltered(NewsInputFilterDTO filterDTO);
}
