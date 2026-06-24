package com.specification.service.service.user.website;

import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface IWebsiteUserService {

	Mono<WebsiteProfileResponse> create(WebsiteProfileRequest request, String actor);

	Mono<WebsiteProfileResponse> update(String userId, WebsiteProfileRequest request, String actor);

	Mono<WebsiteProfileResponse> getById(String userId);

	Mono<PageResponse<WebsiteProfileResponse>> list(int page, int size);

	Mono<Void> softDelete(String userId, String actor);
}
