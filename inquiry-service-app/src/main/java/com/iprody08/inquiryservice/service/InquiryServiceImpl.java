package com.iprody08.inquiryservice.service;

import com.iprody08.inquiryservice.dao.InquiryRepository;
import com.iprody08.inquiryservice.dao.InquirySpecifications;
import com.iprody08.inquiryservice.dto.InquiryDto;
import com.iprody08.inquiryservice.dto.mapper.InquiryMapper;
import com.iprody08.inquiryservice.entity.Inquiry;
import com.iprody08.inquiryservice.entity.enums.InquiryStatus;
import com.iprody08.inquiryservice.filter.InquiryFilter;
import com.iprody08.inquiryservice.pagination.PaginationUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryMapper inquiryMapper;

    public InquiryServiceImpl(final InquiryRepository inquiryRepository, final InquiryMapper inquiryMapper) {
        this.inquiryRepository = inquiryRepository;
        this.inquiryMapper = inquiryMapper;
    }
    @Override
    public List<InquiryDto> findAll() {
        return inquiryRepository.findAll()
                .stream()
                .map(inquiryMapper::inquiryToInquiryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InquiryDto> findAll(Integer pageNo, Integer pageSize, String sortBy,
                                    String sortDirection, InquiryFilter filterBy) {

        Pageable paging = PaginationUtils.getPageable(pageNo, pageSize, sortBy, sortDirection);

        Page<Inquiry> inquiryPage;

        Specification<Inquiry> spec = Specification.where(null);

        if (filterBy != null && filterBy.checkFilterExists()) {

            if (filterBy.getStatus() != null) {
                spec = spec.and(InquirySpecifications.hasStatus(filterBy.getStatus()));
            }
            if (filterBy.getComment() != null) {
                spec = spec.and(InquirySpecifications.hasComment(filterBy.getComment()));
            }
            if (filterBy.getNote() != null) {
                spec = spec.and(InquirySpecifications.hasNote(filterBy.getNote()));
            }
            inquiryPage = inquiryRepository.findAll(spec, paging);

        } else {
            inquiryPage = inquiryRepository.findAll(paging);
        }

        return inquiryPage.getContent()
                .stream()
                .map(inquiryMapper::inquiryToInquiryDto)
                .collect(Collectors.toList());

    }

    @Override
    public InquiryDto save(InquiryDto inquiryDto) {
        final Inquiry inquiry = inquiryMapper.inquiryDtoToInquiry(inquiryDto);
        inquiry.setStatus(InquiryStatus.NEW);
        return inquiryMapper.inquiryToInquiryDto(inquiryRepository.save(inquiry));

    }

    @Override
    public Optional<InquiryDto> findById(long id) {
        return inquiryRepository.findById(id).map(inquiryMapper::inquiryToInquiryDto);

    }


    @Override
    public void deleteById(long id) {
        inquiryRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        inquiryRepository.deleteAll();
    }

    @Override
    public Optional<InquiryDto> update(InquiryDto inquiryDto) {
        return inquiryRepository.findById(inquiryDto.getId())
                .map(inquiry -> {
                    inquiryMapper.updateInquiryFromDto(inquiryDto, inquiry);
                    inquiry.setStatus(inquiryDto.getStatus());
                    return inquiryMapper.inquiryToInquiryDto(inquiryRepository.save(inquiry));
                });
    }
}

