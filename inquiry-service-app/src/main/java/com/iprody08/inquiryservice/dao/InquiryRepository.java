package com.iprody08.inquiryservice.dao;

import com.iprody08.inquiryservice.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, JpaSpecificationExecutor<Inquiry> {

    @EntityGraph(attributePaths = {"source"})
    Page<Inquiry> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"source"})
    Page<Inquiry> findAll(Specification<Inquiry> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"source"})
    Optional<Inquiry> findById(Long id);

}
