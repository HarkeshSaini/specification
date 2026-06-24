package com.specification.service.controller.function;

import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@RequestMapping("/api/public/blog")
public interface IPublicBlogController {

    @GetMapping
    Mono<ResponseEntity<APIResponse<PageResponse<BlogSummaryResponse>>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size);

    @GetMapping("/{slug}")
    Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> getBySlug(@PathVariable String slug);
}
