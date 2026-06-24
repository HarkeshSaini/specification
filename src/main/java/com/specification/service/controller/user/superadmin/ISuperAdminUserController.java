package com.specification.service.controller.user.superadmin;

import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/super-admin")
public interface ISuperAdminUserController {

	@PostMapping("/createSuperAdmin")
	Mono<ResponseEntity<APIResponse<SuperAdminProfileResponse>>> create(@Valid @RequestBody SuperAdminProfileRequest request);

	@PutMapping("/updateById/{userId}")
	Mono<ResponseEntity<APIResponse<SuperAdminProfileResponse>>> update(@PathVariable String userId, @Valid @RequestBody SuperAdminProfileRequest request);

	@GetMapping("/findById/{userId}")
	Mono<ResponseEntity<APIResponse<SuperAdminProfileResponse>>> getById(@PathVariable String userId);

	@GetMapping("/getAllSuperAdmin")
	Mono<ResponseEntity<APIResponse<PageResponse<SuperAdminProfileResponse>>>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);

	@DeleteMapping("/deleteById/{userId}")
	Mono<ResponseEntity<APIResponse<Void>>> delete(@PathVariable String userId);
}
