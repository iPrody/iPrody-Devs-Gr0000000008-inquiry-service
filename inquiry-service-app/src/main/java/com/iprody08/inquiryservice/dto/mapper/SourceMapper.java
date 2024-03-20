package com.iprody08.inquiryservice.dto.mapper;

import com.iprody08.inquiryservice.dto.SourceDto;
import com.iprody08.inquiryservice.entity.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SourceMapper {
    @Mapping(target = "inquiryDto", source = "inquiries")
    SourceDto sourceToSourceDto(Source source);

    @Mapping(target = "inquiries", source = "inquiryDto")
    Source sourceDtoToSource(SourceDto sourceDto);

    @Mapping(target = "inquiries", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateSourceFromDto(SourceDto sourceDto, @MappingTarget Source source);
}