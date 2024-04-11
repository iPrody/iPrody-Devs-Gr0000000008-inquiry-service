package com.iprody08.inquiryservice.controller;

import com.iprody08.inquiryservice.dto.SourceDto;
import com.iprody08.inquiryservice.exception_handlers.NotFoundException;
import com.iprody08.inquiryservice.filter.SourceFilter;
import com.iprody08.inquiryservice.service.SourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all list sources", description = "Returns list all sources")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<SourceDto> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "25") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc")String sortDirection,
            @RequestParam(required = false) String name) {
        SourceFilter filter = new SourceFilter();
        filter.setName(name);
        return sourceService.findAll(pageNo, pageSize, sortBy, sortDirection, filter);
    }

    @GetMapping("/sources/id/{id}")
    @Operation(summary = "Find source by ID", description = "Returns find Source Dto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @ResponseStatus(HttpStatus.OK)
    public SourceDto findById(@PathVariable long id) {
        return sourceService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NO_SOURCE_WITH_ID_MESSAGE, id)));
    }

    @DeleteMapping("/sources/id/{id}")
    @Operation(summary = "Delete source by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted")
    })
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable long id) {
        sourceService.deleteById(id);
    }

    @PostMapping("/sources")
    @Operation(summary = "Create a new source", description = "Returns a dto of source created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create successfully")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public SourceDto save(@RequestBody SourceDto entity) {
        return sourceService.save(entity);
    }

    @PutMapping("/sources/id/{id}")
    @Operation(summary = "Update source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated")
    })
    public ResponseEntity<SourceDto> update(@PathVariable long id, @RequestBody SourceDto sourceDto) {
        return sourceService.update(sourceDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException(String.format(NO_SOURCE_WITH_ID_MESSAGE, id)));
    }
}

