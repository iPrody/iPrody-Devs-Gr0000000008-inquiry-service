package com.iprody08.inquiryservice.controller;

import com.iprody08.inquiryservice.dto.InquiryDto;
import com.iprody08.inquiryservice.entity.enums.InquiryStatus;
import com.iprody08.inquiryservice.exception_handlers.NotFoundException;
import com.iprody08.inquiryservice.filter.InquiryFilter;
import com.iprody08.inquiryservice.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public final class InquiryController {
    private final InquiryService inquiryService;

    public InquiryController(final InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping("/inquiries/id/{id}")
    public InquiryDto findById(@PathVariable long id) {
        return inquiryService.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no inquiry with id " + id));
    }

    @GetMapping("/inquiries")
    public List<InquiryDto> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "25") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc")String sortDirection,
            @RequestParam(required = false) InquiryStatus status,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false) String note) {
        InquiryFilter filter = new InquiryFilter();
        filter.setStatus(status);
        filter.setComment(comment);
        filter.setNote(note);
        return inquiryService.findAll(pageNo, pageSize, sortBy, sortDirection, filter);
    }

    @DeleteMapping("/inquiries/id/{id}")
    public void deleteById(@PathVariable long id) {
        inquiryService.deleteById(id);
    }

    @PostMapping("/inquiries")
    public ResponseEntity<Void> save(@RequestBody InquiryDto inquiryDto) {
        inquiryService.save(inquiryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/inquiries/id/{id}")
    public ResponseEntity<InquiryDto> update(@PathVariable long id, @RequestBody InquiryDto inquiryDto) {
        return inquiryService.update(inquiryDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("There is no Source with id " + id));
    }

}
