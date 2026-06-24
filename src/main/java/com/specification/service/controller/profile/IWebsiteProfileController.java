package com.specification.service.controller.profile;

import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/api/website")
public interface IWebsiteProfileController {

	@GetMapping("/me")
	Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> me();
}
