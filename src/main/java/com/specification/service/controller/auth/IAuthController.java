package com.specification.service.controller.auth;

import com.specification.service.request.LoginRequest;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.TokenResponse;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/api/auth")
public interface IAuthController {

    @PostMapping("/login")
    Mono<ResponseEntity<APIResponse<TokenResponse>>> login(@Valid @RequestBody LoginRequest request);

    @PostMapping("/register")
    Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> register(@Valid @RequestBody WebsiteProfileRequest request);
}
