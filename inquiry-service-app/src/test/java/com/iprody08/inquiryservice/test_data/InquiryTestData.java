package com.iprody08.inquiryservice.test_data;

import com.iprody08.inquiryservice.dto.InquiryDto;
import com.iprody08.inquiryservice.dto.mapper.InquiryMapper;
import com.iprody08.inquiryservice.entity.Inquiry;
import com.iprody08.inquiryservice.entity.enums.InquiryStatus;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.iprody08.inquiryservice.test_data.SourceTestData.*;

public class InquiryTestData {
    public static final Long INQUIRY_ID_1 = 1L;

    public static final Long INQUIRY_ID_2 = INQUIRY_ID_1 + 1;

    public static final Long INQUIRY_ID_3 = INQUIRY_ID_2 + 1;

    public static final Long INQUIRY_NEW_ID = INQUIRY_ID_3 + 1;

    public static final Long NOT_EXIST_ID = 999L;

    public static final Inquiry INQUIRY_1 = Inquiry.createNewInquiry(
            INQUIRY_ID_1,
            "comment_Source1",
            InquiryStatus.NEW,
            "note_Source1",
            SOURCE_1
    );

    public static final Inquiry INQUIRY_2 = Inquiry.createNewInquiry(
            INQUIRY_ID_2,
            "comment_INQUIRY_2",
            InquiryStatus.NEW,
            "note_INQUIRY_2",
            SOURCE_2
    );

    public static final Inquiry INQUIRY_3 = Inquiry.createNewInquiry(
            INQUIRY_ID_3,
            "comment_INQUIRY_3",
            InquiryStatus.NEW,
            "note_INQUIRY_3",
            SOURCE_3
    );

    private static final InquiryMapper MAPPER = Mappers.getMapper(InquiryMapper.class);


    public static Inquiry getNewInquiry() {
        return Inquiry.createNewInquiry(
                null,
                "comment_NewInquiry",
                InquiryStatus.NEW,
                "note_NewInquiry",
                getNewSource()
        );
    }
    public static List<Inquiry> getInquiries() {
        return List.of(INQUIRY_1, INQUIRY_2, INQUIRY_3);
    }

    public static InquiryDto getInquiryDto(Inquiry inquiry) {
        return MAPPER.inquiryToInquiryDto(inquiry);
    }

}
