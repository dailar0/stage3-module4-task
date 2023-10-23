package com.mjc.school.service.mapping;

import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.DTO.NewsInputDTO;
import com.mjc.school.service.DTO.NewsInputFilterDTO;
import com.mjc.school.service.DTO.NewsOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {TagMapper.class, AuthorMapper.class})
public interface NewsMapper {
    //TODO
    @Mapping(source = "tags", target = "tags")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(source = "dto.id", target = "id")
    News mapCreateToNews(NewsInputDTO dto, Set<Tag> tags, Author author);

    @Mapping(source = "tags", target = "tagIds")
    @Mapping(source = "author", target = "authorId")
    @Mapping(source = "createDate", target = "createDate", dateFormat = "yyyy-MM-dd'T'HH:mm'Z'")
    @Mapping(source = "lastUpdateDate", target = "lastUpdateDate", dateFormat = "yyyy-MM-dd'T'HH:mm'Z'")
    NewsOutputDTO mapNewsToOutput(News news);

    List<NewsOutputDTO> mapNewsToOutputDTOList(Collection<News> newsCollection);

    NewsInputFilterDTO rawDataToFilteredDTO(String authorName, String[] tagNames, String[] tagIds, String title, String content);


}

