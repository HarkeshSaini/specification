package com.specification.service.controller.function;

import com.specification.service.response.function.HomeListingKeyResponse;
import com.specification.service.response.apires.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/web")
public interface IHomeListingController {

    @GetMapping(value = "/key-dynamic-word")
    ResponseEntity<APIResponse<HomeListingKeyResponse>> homeListingKeyWord(@RequestParam String langCode);
}
