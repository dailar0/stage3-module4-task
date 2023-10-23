package com.mjc.school.service.mapping;

import com.mjc.school.repository.model.Author;
import com.mjc.school.service.DTO.AuthorInputDTO;
import com.mjc.school.service.DTO.AuthorOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "news",ignore = true)
    Author mapCreateToAuthor(AuthorInputDTO createDTO);

    AuthorOutputDTO mapAuthorToOutput(Author author);

    List<AuthorOutputDTO> mapAuthorToOutputList(Collection<Author> authors);

    List<Long> mapAuthorListToIdList(List<Author> authors);

    default Long getAuthorId(Author author) {
        return author != null ? author.getId() : null;
    }

}
