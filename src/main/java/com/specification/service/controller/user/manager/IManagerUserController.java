package com.specification.service.controller.user.manager;

import com.specification.service.request.profile.ManagerProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.user.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/managers")
public interface IManagerUserController {

	@PostMapping("/createNewManagers")
	Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> create(@Valid @RequestBody ManagerProfileRequest request);

	@PutMapping("/updateManagersById/{userId}")
	Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> update(@PathVariable String userId, @Valid @RequestBody ManagerProfileRequest request);

	@GetMapping("/getManagersById/{userId}")
	Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> getById(@PathVariable String userId);

	@GetMapping("/getAllManagers")
	Mono<ResponseEntity<APIResponse<PageResponse<ManagerProfileResponse>>>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);

	@DeleteMapping("/deleteManagersById/{userId}")
	Mono<ResponseEntity<APIResponse<Void>>> delete(@PathVariable String userId);
}
