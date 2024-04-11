package com.iprody08.inquiryservice.service;

import com.iprody08.inquiryservice.dao.SourceRepository;
import com.iprody08.inquiryservice.dao.SourceSpecification;
import com.iprody08.inquiryservice.dto.SourceDto;
import com.iprody08.inquiryservice.dto.mapper.SourceMapper;
import com.iprody08.inquiryservice.entity.Source;
import com.iprody08.inquiryservice.filter.SourceFilter;
import com.iprody08.inquiryservice.pagination.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SourceServiceImpl implements SourceService {
    private final SourceRepository sourceRepository;
    private final SourceMapper sourceMapper;

    @Autowired
    public SourceServiceImpl(final SourceRepository sourceRepository, final SourceMapper sourceMapper) {
        this.sourceRepository = sourceRepository;
        this.sourceMapper = sourceMapper;
    }

    @Override
    public List<SourceDto> findAll(Integer pageNo, Integer pageSize, String sortBy,
                                   String sortDirection, SourceFilter filterBy) {

        Pageable paging = PaginationUtils.getPageable(pageNo, pageSize, sortBy, sortDirection);
        Page<Source> sourcePage;
        Specification<Source> spec = Specification.where(null);

        if (filterBy != null && filterBy.checkFilterExists()) {
            if (filterBy.getName() != null) {
                spec = spec.and(SourceSpecification.hasName(filterBy.getName()));
            }
            sourcePage = sourceRepository.findAll(spec, paging);
        } else {
            sourcePage = sourceRepository.findAllWithInquiry(paging);
        }

        return sourcePage.getContent()
                .stream()
                .map(sourceMapper::sourceToSourceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SourceDto> findAll() {
        return sourceRepository.findAll()
                .stream()
                .map(sourceMapper::sourceToSourceDto)
                .collect(Collectors.toList());
    }

    @Override
    public SourceDto save(SourceDto entity) {
        final Source source = sourceMapper.sourceDtoToSource(entity);
        return  sourceMapper.sourceToSourceDto(sourceRepository.save(source));
    }

    @Override
    public Optional<SourceDto> findById(long id) {
        return sourceRepository.findById(id).map(sourceMapper::sourceToSourceDto);
    }

    @Override
    public void deleteById(long id) {
        sourceRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        sourceRepository.deleteAll();
    }

    @Override
    public Optional<SourceDto> update(SourceDto sourceDto) {
        return sourceRepository.findById(sourceDto.getId())
                .map(source -> {
                    sourceMapper.updateSourceFromDto(sourceDto, source);
                    sourceRepository.save(source);
                    return sourceMapper.sourceToSourceDto(source);
                });
    }
}
