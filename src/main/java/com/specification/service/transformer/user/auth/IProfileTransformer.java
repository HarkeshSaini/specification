package com.specification.service.transformer.user.auth;


import com.specification.service.domain.entity.user.AdminUserDetail;
import com.specification.service.domain.entity.user.ManagerUserDetail;
import com.specification.service.domain.entity.user.SuperAdminUserDetail;
import com.specification.service.domain.entity.user.WebUserDetail;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;

public interface IProfileTransformer {

	AdminProfileResponse toAdminResponse(AdminUserDetail user);

	ManagerProfileResponse toManagerResponse(ManagerUserDetail user);

	SuperAdminProfileResponse toSuperAdminResponse(SuperAdminUserDetail user);

	WebsiteProfileResponse toWebsiteResponse(WebUserDetail user);
}
