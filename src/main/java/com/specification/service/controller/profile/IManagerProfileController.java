package com.specification.service.controller.profile;

import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/manager")
public interface IManagerProfileController {

	@GetMapping("/me")
	Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> me();

	@GetMapping("/website-users")
	Flux<ResponseEntity<APIResponse<WebsiteProfileResponse>>> listWebsiteUsers();
}
