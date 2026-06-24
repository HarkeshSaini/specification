package com.specification.service.service.user.superadmin;

import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface ISuperAdminUserService {

	Mono<SuperAdminProfileResponse> create(SuperAdminProfileRequest request, String actor);

	Mono<SuperAdminProfileResponse> update(String userId, SuperAdminProfileRequest request, String actor);

	Mono<SuperAdminProfileResponse> getById(String userId);

	Mono<PageResponse<SuperAdminProfileResponse>> list(int page, int size);

	Mono<Void> softDelete(String userId, String actor);
}
