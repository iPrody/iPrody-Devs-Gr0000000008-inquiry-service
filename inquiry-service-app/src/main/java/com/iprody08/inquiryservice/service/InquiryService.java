package com.iprody08.inquiryservice.service;

import com.iprody08.inquiryservice.dto.InquiryDto;
import com.iprody08.inquiryservice.filter.InquiryFilter;

import java.util.List;


public interface InquiryService extends BaseService<InquiryDto> {
    List<InquiryDto> findAll(Integer pageNo, Integer pageSize, String sortBy,
                             String sortDirection, InquiryFilter filterBy);
}
