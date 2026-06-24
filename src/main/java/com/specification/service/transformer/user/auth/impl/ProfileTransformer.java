package com.specification.service.transformer.user.auth.impl;

import com.specification.service.domain.entity.user.AdminUserDetail;
import com.specification.service.domain.entity.user.ManagerUserDetail;
import com.specification.service.domain.entity.user.SuperAdminUserDetail;
import com.specification.service.domain.entity.user.WebUserDetail;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.transformer.user.auth.IProfileTransformer;
import com.specification.service.transformer.user.admin.IAdminUserTransformer;
import com.specification.service.transformer.user.manager.IManagerUserTransformer;
import com.specification.service.transformer.user.superadmin.ISuperAdminUserTransformer;
import com.specification.service.transformer.user.website.IWebsiteUserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileTransformer implements IProfileTransformer {

	private final IAdminUserTransformer adminUserTransformer;
	private final IManagerUserTransformer managerUserTransformer;
	private final ISuperAdminUserTransformer superAdminUserTransformer;
	private final IWebsiteUserTransformer websiteUserTransformer;

	@Override
	public AdminProfileResponse toAdminResponse(AdminUserDetail u) {
		return adminUserTransformer.toProfileResponse(u);
	}

	@Override
	public ManagerProfileResponse toManagerResponse(ManagerUserDetail u) {
		return managerUserTransformer.toProfileResponse(u);
	}

	@Override
	public SuperAdminProfileResponse toSuperAdminResponse(SuperAdminUserDetail u) {
		return superAdminUserTransformer.toProfileResponse(u);
	}

	@Override
	public WebsiteProfileResponse toWebsiteResponse(WebUserDetail u) {
		return websiteUserTransformer.toProfileResponse(u);
	}
}
