package com.specification.service.transformer.user.website;

import com.specification.service.domain.entity.user.WebUserDetail;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.profile.WebsiteProfileResponse;

import java.time.Instant;

public interface IWebsiteUserTransformer {

    WebUserDetail toNewEntity(WebsiteProfileRequest request, String actor, Instant now);

	void applyUpdates(WebUserDetail user, WebsiteProfileRequest request, String actor, Instant now);

	WebsiteProfileResponse toProfileResponse(WebUserDetail user);
}
