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
import com.iprody08.inquiryservice.test_data.InquiryTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;


import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
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
        Optional<InquiryDto> expected =  inquiryRepository.findById(INQUIRY_ID_1)
                .map(inquiryMapper::inquiryToInquiryDto);
        //then
        assertThat(expected).isNotEmpty();
    }

    @Test
    void getInquiryByIdNotExists() {
        //given
        when(inquiryRepository.findById(NOT_EXIST_ID)).thenReturn(Optional.empty());

        //when
        Optional<InquiryDto> expected =  inquiryRepository.findById(NOT_EXIST_ID)
                .map(inquiryMapper::inquiryToInquiryDto);

        //then
        assertFalse(expected.isPresent());
    }

    @Test
    void findAllInquiries() {
        //given
        List<Inquiry> inquiries = getInquiries();

        //when
        when(inquiryRepository.findAll()).thenReturn(inquiries);

        // then
        List<InquiryDto> expected = inquiryService.findAll();
        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(3);
        List<InquiryDto> actual = inquiries.stream().map(InquiryTestData::getInquiryDto).toList();
        assertEquals(actual, expected);
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
    void updateNameInInquiry() {
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
