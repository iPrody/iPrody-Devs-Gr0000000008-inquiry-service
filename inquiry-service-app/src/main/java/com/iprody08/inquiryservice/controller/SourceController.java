package com.iprody08.inquiryservice.controller;

import com.iprody08.inquiryservice.dto.SourceDto;
import com.iprody08.inquiryservice.exception_handlers.NotFoundException;
import com.iprody08.inquiryservice.service.SourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public final class SourceController {

    private static final String NO_SOURCE_WITH_ID_MESSAGE = "There is no Source with id";
    private final SourceService sourceService;


    public SourceController(final SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping("/sources")
    public List<SourceDto> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "25") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc")String sortDirection,
            @RequestParam(required = false) String filterBy) {
        return sourceService.findAll(pageNo, pageSize, sortBy, sortDirection, filterBy);
    }

    @GetMapping("/sources/id/{id}")
    public SourceDto findById(@PathVariable long id) {
        return sourceService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NO_SOURCE_WITH_ID_MESSAGE, id)));
    }

    @DeleteMapping("/sources/id/{id}")
    public void deleteById(@PathVariable long id) {
        sourceService.deleteById(id);
    }

    @PostMapping("/sources")
    public ResponseEntity<Void> save(@RequestBody SourceDto entity) {
        sourceService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/sources/id/{id}")
    public ResponseEntity<SourceDto> update(@PathVariable long id, @RequestBody SourceDto sourceDto) {
        return sourceService.update(sourceDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException(String.format(NO_SOURCE_WITH_ID_MESSAGE, id)));
    }

}
