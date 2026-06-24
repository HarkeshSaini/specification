package com.specification.service.controller.profile;

import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.AdminProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/api/admin")
public interface IAdminProfileController {

	@GetMapping("/me")
	Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> me();
}
