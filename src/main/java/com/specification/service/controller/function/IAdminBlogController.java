package com.specification.service.controller.function;

import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/admin/blog")
public interface IAdminBlogController {

    @PostMapping
    Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> create(@Valid @RequestBody BlogCreateRequest request);

    @PutMapping("/{id}")
    Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> update(@PathVariable String id, @Valid @RequestBody BlogUpdateRequest request);

    @GetMapping("/{id}")
    Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> getById(@PathVariable String id);

    @GetMapping
    Mono<ResponseEntity<APIResponse<PageResponse<BlogSummaryResponse>>>> list(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size);

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<APIResponse<Void>>> delete(@PathVariable String id);
}
