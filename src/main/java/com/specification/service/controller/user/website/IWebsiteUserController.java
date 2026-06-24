package com.specification.service.controller.user.website;

import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.response.user.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/webUser")
public interface IWebsiteUserController {

	@PostMapping("/createNewUser")
	Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> create(@Valid @RequestBody WebsiteProfileRequest request);

	@PutMapping("/updateUserById/{userId}")
	Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> update(@PathVariable String userId, @Valid @RequestBody WebsiteProfileRequest request);

	@GetMapping("/getUserById/{userId}")
	Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> getById(@PathVariable String userId);

	@GetMapping("/getAllUser")
	Mono<ResponseEntity<APIResponse<PageResponse<WebsiteProfileResponse>>>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);

	@DeleteMapping("/deleteUserById/{userId}")
	Mono<ResponseEntity<APIResponse<Void>>> delete(@PathVariable String userId);
}
