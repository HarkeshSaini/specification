package com.specification.service.transformer.user.admin;

import com.specification.service.domain.entity.user.AdminUserDetail;
import com.specification.service.request.profile.AdminProfileRequest;
import com.specification.service.response.profile.AdminProfileResponse;

import java.time.Instant;

public interface IAdminUserTransformer {

	AdminUserDetail toNewEntity(AdminProfileRequest request, String actor, Instant now);

	void applyUpdates(AdminUserDetail user, AdminProfileRequest request, String actor, Instant now);

	AdminProfileResponse toProfileResponse(AdminUserDetail user);
}
