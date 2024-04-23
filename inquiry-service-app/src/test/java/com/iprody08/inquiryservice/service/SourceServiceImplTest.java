package com.iprody08.inquiryservice.service;


import static com.iprody08.inquiryservice.test_data.SourceTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.iprody08.inquiryservice.dao.SourceRepository;
import com.iprody08.inquiryservice.dto.SourceDto;
import com.iprody08.inquiryservice.dto.mapper.SourceMapper;
import com.iprody08.inquiryservice.entity.Source;

import com.iprody08.inquiryservice.test_data.InquiryTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SourceServiceImplTest {
    @Mock
    private SourceRepository sourceRepository;

    @Spy
    private SourceMapper sourceMapper = Mappers.getMapper(SourceMapper.class);

    @InjectMocks
    private SourceServiceImpl sourceService;

    @Test
    void getSourceByIdExists() {

        //given
        when(sourceRepository.findById(SOURCE_ID_1)).thenReturn(Optional.of(SOURCE_1));

        //when
        Optional<SourceDto> result = sourceService.findById(SOURCE_ID_1);

        //then
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(sourceMapper.sourceToSourceDto(SOURCE_1));
        assertEquals(result.get().getId(), SOURCE_1.getId());
        assertEquals(result.get().getName(), SOURCE_1.getName());
    }

    @Test
    void getSourceByIdNotExists() {
        //given
        when(sourceRepository.findById(NOT_EXIST_ID)).thenReturn(Optional.empty());

        //when
        Optional<SourceDto> expected = sourceService.findById(NOT_EXIST_ID);

        //then
        assertFalse(expected.isPresent());
    }

    @Test
    void findAllSources() {
        //given
        when(sourceRepository.findAll()).thenReturn(getSources());

        // when
        List<SourceDto> expected = sourceService.findAll();

        //then
        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(3);
        for (int i = 0; i < expected.size(); i++) {
            assertThat(expected.get(i).getId()).isEqualTo(getSources().get(i).getId());
            assertThat(expected.get(i).getName()).isEqualTo(getSources().get(i).getName());
        }
    }

    @Test
    void createNewSource() {
        //given
        Source source = getNewSource();
        source.setId(SOURCE_ID_1);
        source.setInquiries(InquiryTestData.getInquiries());
        SourceDto sourceDto = getSourceDto(source);

        when(sourceRepository.save(any(Source.class))).thenReturn(source);

        //when
        SourceDto expected = sourceService.save(sourceDto);

        //then
        assertThat(expected).isNotNull();
        assertEquals(expected, sourceDto);

    }

    @Test
    void updateNameInSource() {
        //given
        Source source = getNewSource();
        source.setId(SOURCE_ID_1);
        source.setInquiries(InquiryTestData.getInquiries());
        String oldName = source.getName();
        source.setName("NewNameForUpdate");
        SourceDto sourceDto = getSourceDto(source);

        when(sourceRepository.save(any(Source.class))).thenReturn(source);

        //when
        String newName = sourceService.save(sourceDto).getName();

        //then
        assertNotEquals(oldName, newName);
    }

}
