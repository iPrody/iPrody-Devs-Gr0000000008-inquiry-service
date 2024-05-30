package com.iprody08.inquiryservice.controller;

import com.iprody08.inquiryservice.dto.InquiryDto;
import com.iprody08.inquiryservice.entity.enums.InquiryStatus;
import com.iprody08.inquiryservice.exception_handlers.NotFoundException;
import com.iprody08.inquiryservice.filter.InquiryFilter;
import com.iprody08.inquiryservice.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public final class InquiryController {
    private final InquiryService inquiryService;
    private final WebClient.Builder webClientBuilder;

    @Value("${customer.service.url}")
    private String customerServiceUrl;
    @Value("${product.service.url}")
    private String productServiceUrl;

    public InquiryController(InquiryService inquiryService, WebClient.Builder webClientBuilder) {
        this.inquiryService = inquiryService;
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/inquiries/id/{id}")
    @Operation(summary = "Find inquiry by ID", description = "Returns find Inquiry Dto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @ResponseStatus(HttpStatus.OK)
    public InquiryDto findById(@PathVariable long id) {
        return inquiryService.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no inquiry with id " + id));
    }

    @GetMapping("/inquiries")
    @Operation(summary = "Get all list inquiries", description = "Returns list all inquiries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @ResponseStatus(HttpStatus.OK)
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
    @Operation(summary = "Delete inquiry by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted")
    })
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable long id) {
        inquiryService.deleteById(id);
    }

    @PostMapping("/inquiries")
    @Operation(summary = "Create a new inquiry", description = "Returns a dto of inquiry created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create successfully")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public InquiryDto save(@RequestBody InquiryDto inquiryDto) {
        return inquiryService.save(inquiryDto);
    }

    @PutMapping("/inquiries/id/{id}")
    @Operation(summary = "Update inquiry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InquiryDto> update(@PathVariable long id, @RequestBody InquiryDto inquiryDto) {
        return inquiryService.update(inquiryDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("There is no Source with id " + id));
    }

    @GetMapping("/inquiries/id/{id}/customer-info")
    @Operation(summary = "Get customer", description = "Returns customers information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> findCustomerById(@PathVariable long id) {
        Long customerId =  inquiryService.findById(id).orElseThrow().getCustomerRefId();

        String url = customerServiceUrl  + customerId;
        System.out.println(url);
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/inquiries/id/{id}/product-info")
    @Operation(summary = "Get product", description = "Returns products information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> findProductById(@PathVariable long id) {
        Long customerId =  inquiryService.findById(id).orElseThrow().getCustomerRefId();
        String url = productServiceUrl  + customerId;
        System.out.println(url);
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }

}
