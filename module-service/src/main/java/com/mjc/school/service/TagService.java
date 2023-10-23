package com.mjc.school.service;

import com.mjc.school.service.DTO.TagInputDTO;
import com.mjc.school.service.DTO.TagOutputDTO;

import java.util.List;

public interface TagService extends BaseService<TagInputDTO, TagOutputDTO,Long>{

    List<TagOutputDTO> readAllByNewsID(long newsId);
}
