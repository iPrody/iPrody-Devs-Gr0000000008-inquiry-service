package com.iprody08.inquiryservice.test_data;

import com.iprody08.inquiryservice.dto.SourceDto;
import com.iprody08.inquiryservice.dto.mapper.SourceMapper;
import com.iprody08.inquiryservice.entity.Source;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class SourceTestData {
    public static final Long SOURCE_ID_1 = 1L;

    public static final Long SOURCE_ID_2 = SOURCE_ID_1 + 1;

    public static final Long SOURCE_ID_3 = SOURCE_ID_2 + 1;

    public static final Long SOURCE_NEW_ID = SOURCE_ID_3 + 1;

    public static final Long NOT_EXIST_ID = 999L;

    public static final Source SOURCE_1 = Source.createNewProduct(
            SOURCE_ID_1,
            "Source1"
    );

    public static final Source SOURCE_2 = Source.createNewProduct(
            SOURCE_ID_2,
            "Source2"
    );

    public static final Source SOURCE_3 = Source.createNewProduct(
            SOURCE_ID_3,
            "Source3"
    );

    private static final SourceMapper MAPPER = Mappers.getMapper(SourceMapper.class);


    public static Source getNewSource() {
        return Source.createNewProduct(
                null,
                "NewSource"
        );
    }

    public static List<Source> getSources() {
        return List.of(SOURCE_1, SOURCE_2, SOURCE_3);
    }

    public static SourceDto getSourceDto(Source source) {
        return MAPPER.sourceToSourceDto(source);
    }

}
