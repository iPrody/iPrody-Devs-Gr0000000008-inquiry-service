package com.iprody08.inquiryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDto {

    private Long id;

    private SourceDto sourceId;

    private String comment;

    private String status;

    private String note;

    private Long productRefId;

    private Long customerRefId;

    private Long managerRefId;

    public InquiryDto(SourceDto sourceId, String comment, String status, String note) {
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
