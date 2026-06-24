package com.specification.service.transformer.user.superadmin;

import com.specification.service.domain.entity.user.SuperAdminUserDetail;
import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.profile.SuperAdminProfileResponse;

import java.time.Instant;

public interface ISuperAdminUserTransformer {

	SuperAdminUserDetail toNewEntity(SuperAdminProfileRequest request, String actor, Instant now);

	void applyUpdates(SuperAdminUserDetail user, SuperAdminProfileRequest request, String actor, Instant now);

	SuperAdminProfileResponse toProfileResponse(SuperAdminUserDetail user);
}
