package com.specification.service.facade.user.superadmin;

import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface ISuperAdminUserFacade {

	Mono<SuperAdminProfileResponse> create(SuperAdminProfileRequest request);

	Mono<SuperAdminProfileResponse> update(String userId, SuperAdminProfileRequest request);

	Mono<SuperAdminProfileResponse> getById(String userId);

	Mono<PageResponse<SuperAdminProfileResponse>> list(int page, int size);

	Mono<Void> softDelete(String userId);
}
