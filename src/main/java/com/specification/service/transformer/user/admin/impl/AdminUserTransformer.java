package com.specification.service.transformer.user.admin.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.entity.user.AdminUserDetail;
import com.specification.service.request.profile.AdminProfileRequest;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.transformer.support.BaseProfileResponseMapper;
import com.specification.service.transformer.user.admin.IAdminUserTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Locale;

@Slf4j
@Component
public class AdminUserTransformer implements IAdminUserTransformer {

	@Override
	public AdminUserDetail toNewEntity(AdminProfileRequest request, String actor, Instant now) {
		return AdminUserDetail.builder()
				.email(normalizeEmail(request.getEmail()))
				.username(request.getUsername())
				.fullName(request.getFullName())
				.phoneNumber(request.getPhoneNumber())
				.profileImageUrl(request.getProfileImageUrl())
				.address(request.getAddress())
				.department(request.getDepartment())
				.designation(request.getDesignation())
				.reportingManager(request.getReportingManager())
				.gender(request.getGender())
				.dateOfBirth(request.getDateOfBirth())
				.roles(request.getRoles())
				.permissions(request.getPermissions())
				.additionalAttributes(request.getAdditionalAttributes())
				.active(resolveActive(request.getActive()))
				.deleted(false)
				.createdTimestamp(now)
				.updateTimestamp(now)
				.createdBy(actor)
				.updatedBy(actor)
				.build();
	}

	@Override
	public void applyUpdates(AdminUserDetail user, AdminProfileRequest request, String actor, Instant now) {
		user.setEmail(normalizeEmail(request.getEmail()));
		user.setUsername(request.getUsername());
		user.setFullName(request.getFullName());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setProfileImageUrl(request.getProfileImageUrl());
		user.setAddress(request.getAddress());
		user.setDepartment(request.getDepartment());
		user.setDesignation(request.getDesignation());
		user.setReportingManager(request.getReportingManager());
		user.setGender(request.getGender());
		user.setDateOfBirth(request.getDateOfBirth());
		user.setRoles(request.getRoles());
		user.setPermissions(request.getPermissions());
		user.setAdditionalAttributes(request.getAdditionalAttributes());
		user.setActive(resolveActive(request.getActive()));
		user.setUpdatedBy(actor);
		user.setUpdateTimestamp(now);
	}

	@Override
	public AdminProfileResponse toProfileResponse(AdminUserDetail user) {
		log.trace(ApplicationConstant.MAPPING_ADMIN_USER_TO_RESPONSE, user.getId());
		AdminProfileResponse.AdminProfileResponseBuilder<?, ?> builder = AdminProfileResponse.builder();
		BaseProfileResponseMapper.applyBaseFields(user, builder);
		return builder
				.roles(user.getRoles())
				.permissions(user.getPermissions())
				.department(user.getDepartment())
				.designation(user.getDesignation())
				.reportingManager(user.getReportingManager())
				.gender(user.getGender())
				.dateOfBirth(user.getDateOfBirth())
				.additionalAttributes(user.getAdditionalAttributes())
				.build();
	}

	private static Boolean resolveActive(Boolean active) {
		return active == null ? Boolean.TRUE : active;
	}

	private static String normalizeEmail(String email) {
		return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
	}
}
