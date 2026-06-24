package com.specification.service.transformer.user.superadmin.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.entity.user.SuperAdminUserDetail;
import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.transformer.support.BaseProfileResponseMapper;
import com.specification.service.transformer.user.superadmin.ISuperAdminUserTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Locale;

@Slf4j
@Component
public class SuperAdminUserTransformer implements ISuperAdminUserTransformer {

	@Override
	public SuperAdminUserDetail toNewEntity(SuperAdminProfileRequest request, String actor, Instant now) {
		return SuperAdminUserDetail.builder()
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
				.allAccess(resolveAllAccess(request.getAllAccess()))
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
	public void applyUpdates(SuperAdminUserDetail user, SuperAdminProfileRequest request, String actor, Instant now) {
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
		user.setAllAccess(resolveAllAccess(request.getAllAccess()));
		user.setAdditionalAttributes(request.getAdditionalAttributes());
		user.setActive(resolveActive(request.getActive()));
		user.setUpdatedBy(actor);
		user.setUpdateTimestamp(now);
	}

	@Override
	public SuperAdminProfileResponse toProfileResponse(SuperAdminUserDetail user) {
		log.trace(ApplicationConstant.MAPPING_SUPER_ADMIN_USER_TO_RESPONSE, user.getId());
		SuperAdminProfileResponse.SuperAdminProfileResponseBuilder<?, ?> builder = SuperAdminProfileResponse.builder();
		BaseProfileResponseMapper.applyBaseFields(user, builder);
		return builder
				.roles(user.getRoles())
				.permissions(user.getPermissions())
				.allAccess(user.getAllAccess())
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

	private static Boolean resolveAllAccess(Boolean allAccess) {
		return allAccess == null ? Boolean.TRUE : allAccess;
	}

	private static String normalizeEmail(String email) {
		return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
	}
}
