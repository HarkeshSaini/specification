package com.specification.service.controller.user.admin;

import com.specification.service.request.profile.AdminProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/admin")
public interface IAdminUserController {

	@PostMapping("/createAdmin")
	Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> create(@Valid @RequestBody AdminProfileRequest request);

	@PutMapping("/updateById/{userId}")
	Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> update(@PathVariable String userId, @Valid @RequestBody AdminProfileRequest request);

	@GetMapping("/findById/{userId}")
	Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> getById(@PathVariable String userId);

    @GetMapping("/getAllAdmin")
	Mono<ResponseEntity<APIResponse<PageResponse<AdminProfileResponse>>>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);

	@DeleteMapping("/deleteById/{userId}")
	Mono<ResponseEntity<APIResponse<Void>>> delete(@PathVariable String userId);
}
