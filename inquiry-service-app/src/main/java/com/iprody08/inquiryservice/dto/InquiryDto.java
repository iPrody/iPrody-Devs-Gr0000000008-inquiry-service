package com.iprody08.inquiryservice.dto;

import com.iprody08.inquiryservice.entity.enums.InquiryStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDto {

    private Long id;

    private SourceDto sourceId;

    private String comment;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    private String note;

    public InquiryDto(SourceDto sourceId, String comment, InquiryStatus status, String note) {
        this.sourceId = sourceId;
        this.comment = comment;
        this.status = status;
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InquiryDto inquiryDto = (InquiryDto) o;
        return Objects.equals(id, inquiryDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
