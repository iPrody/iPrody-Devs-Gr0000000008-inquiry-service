package com.iprody08.inquiryservice.service;

import static com.iprody08.inquiryservice.test_data.InquiryTestData.*;
import static com.iprody08.inquiryservice.test_data.InquiryTestData.NOT_EXIST_ID;
import static com.iprody08.inquiryservice.test_data.SourceTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.iprody08.inquiryservice.dao.InquiryRepository;
import com.iprody08.inquiryservice.dto.InquiryDto;
import com.iprody08.inquiryservice.dto.mapper.InquiryMapper;
import com.iprody08.inquiryservice.entity.Inquiry;

import com.iprody08.inquiryservice.entity.enums.InquiryStatus;
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
class InquiryServiceImplTest {
    @Mock
    private InquiryRepository inquiryRepository;

    @Spy
    private InquiryMapper inquiryMapper = Mappers.getMapper(InquiryMapper.class);

    @InjectMocks
    private InquiryServiceImpl inquiryService;

    @Test
    void getInquiryByIdExists() {
        //given
        when(inquiryRepository.findById(INQUIRY_ID_1)).thenReturn(Optional.of(INQUIRY_1));

        //when
        Optional<InquiryDto> result = inquiryService.findById(INQUIRY_ID_1);

        //then
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(inquiryMapper.inquiryToInquiryDto(INQUIRY_1));
        assertEquals(result.get().getId(), INQUIRY_1.getId());
        assertEquals(result.get().getComment(), INQUIRY_1.getComment());
        assertEquals(result.get().getStatus(), INQUIRY_1.getStatus());

    }

    @Test
    void getInquiryByIdNotExists() {
        //given
        when(inquiryRepository.findById(NOT_EXIST_ID)).thenReturn(Optional.empty());

        //when
        Optional<InquiryDto> expected =  inquiryService.findById(NOT_EXIST_ID);

        //then
        assertFalse(expected.isPresent());

    }

    @Test
    void findAllInquiries() {
        //given
        when(inquiryRepository.findAll()).thenReturn(getInquiries());

        // when
        List<InquiryDto> expected = inquiryService.findAll();

        //then
        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(3);

        for (int i = 0; i < expected.size(); i++) {
            assertThat(expected.get(i).getId()).isEqualTo(getInquiries().get(i).getId());
            assertThat(expected.get(i).getComment()).isEqualTo(getInquiries().get(i).getComment());
            assertThat(expected.get(i).getStatus()).isEqualTo(getInquiries().get(i).getStatus());
            assertThat(expected.get(i).getNote()).isEqualTo(getInquiries().get(i).getNote());
        }
    }

    @Test
    void createNewInquiry() {
        //given
        Inquiry inquiry = getNewInquiry();
        inquiry.setId(SOURCE_ID_1);
        inquiry.setSource(getNewSource());
        InquiryDto inquiryDto = getInquiryDto(inquiry);

        when(inquiryRepository.save(any(Inquiry.class))).thenReturn(inquiry);

        //when
        InquiryDto expected = inquiryService.save(inquiryDto);

        //then
        assertThat(expected).isNotNull();
        assertEquals(expected, inquiryDto);

    }

    @Test
    void updateStatusInInquiry() {
        //given
        Inquiry inquiry = getNewInquiry();
        inquiry.setId(SOURCE_ID_1);
        inquiry.setSource(getNewSource());
        InquiryStatus oldStatus = inquiry.getStatus();
        inquiry.setStatus(InquiryStatus.REJECTED);
        InquiryDto inquiryDto = getInquiryDto(inquiry);

        when(inquiryRepository.save(any(Inquiry.class))).thenReturn(inquiry);

        //when
        InquiryStatus newStatus = inquiryService.save(inquiryDto).getStatus();

        //then
        assertNotEquals(oldStatus.toString(), newStatus.toString());
    }

}
