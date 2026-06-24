package com.specification.service.transformer.user.manager;

import com.specification.service.domain.entity.user.ManagerUserDetail;
import com.specification.service.request.profile.ManagerProfileRequest;
import com.specification.service.response.profile.ManagerProfileResponse;

import java.time.Instant;

public interface IManagerUserTransformer {

	ManagerUserDetail toNewEntity(ManagerProfileRequest request, String actor, Instant now);

	void applyUpdates(ManagerUserDetail user, ManagerProfileRequest request, String actor, Instant now);

	ManagerProfileResponse toProfileResponse(ManagerUserDetail user);
}
