package com.specification.service.facade.user.website;

import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface IWebsiteUserFacade {

	Mono<WebsiteProfileResponse> create(WebsiteProfileRequest request);

	Mono<WebsiteProfileResponse> update(String userId, WebsiteProfileRequest request);

	Mono<WebsiteProfileResponse> getById(String userId);

	Mono<PageResponse<WebsiteProfileResponse>> list(int page, int size);

	Mono<Void> softDelete(String userId);
}
