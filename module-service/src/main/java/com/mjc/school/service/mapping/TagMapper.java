package com.mjc.school.service.mapping;

import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.DTO.TagInputDTO;
import com.mjc.school.service.DTO.TagOutputDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag mapDtoToTag(TagInputDTO dto);

    TagOutputDTO mapTagToDTO(Tag tag);

    List<TagOutputDTO> mapTagListToOutList(List<Tag> tags);

    List<Tag> mapInputListToTagList(List<TagInputDTO> tagDTOs);

    List<Long> mapTagListToIdList(List<Tag> tags);

    default Long getTagId(Tag tag) {
        return tag != null ? tag.getId() : null;
    }
}
