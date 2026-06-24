package com.specification.service.service.user.admin;

import com.specification.service.request.profile.AdminProfileRequest;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface IAdminUserService {

	Mono<AdminProfileResponse> create(AdminProfileRequest request, String actor);

	Mono<AdminProfileResponse> update(String userId, AdminProfileRequest request, String actor);

	Mono<AdminProfileResponse> getById(String userId);

	Mono<PageResponse<AdminProfileResponse>> list(int page, int size);

	Mono<Void> softDelete(String userId, String actor);
}
