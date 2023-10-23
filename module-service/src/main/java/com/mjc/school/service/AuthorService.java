package com.mjc.school.service;

import com.mjc.school.service.DTO.AuthorInputDTO;
import com.mjc.school.service.DTO.AuthorOutputDTO;

public interface AuthorService extends BaseService<AuthorInputDTO, AuthorOutputDTO, Long>{
    AuthorOutputDTO readByNewsId(Long newsId);
}
