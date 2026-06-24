package com.specification.service.facade.user.manager;

import com.specification.service.request.profile.ManagerProfileRequest;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface IManagerUserFacade {

	Mono<ManagerProfileResponse> create(ManagerProfileRequest request);

	Mono<ManagerProfileResponse> update(String userId, ManagerProfileRequest request);

	Mono<ManagerProfileResponse> getById(String userId);

	Mono<PageResponse<ManagerProfileResponse>> list(int page, int size);

	Mono<Void> softDelete(String userId);
}
