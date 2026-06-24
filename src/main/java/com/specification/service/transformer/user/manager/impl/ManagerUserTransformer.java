package com.specification.service.transformer.user.manager.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.entity.user.ManagerUserDetail;
import com.specification.service.request.profile.ManagerProfileRequest;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.transformer.support.BaseProfileResponseMapper;
import com.specification.service.transformer.user.manager.IManagerUserTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Locale;

@Slf4j
@Component
public class ManagerUserTransformer implements IManagerUserTransformer {

	@Override
	public ManagerUserDetail toNewEntity(ManagerProfileRequest request, String actor, Instant now) {
		return ManagerUserDetail.builder()
				.email(normalizeEmail(request.getEmail()))
				.username(request.getUsername())
				.fullName(request.getFullName())
				.phoneNumber(request.getPhoneNumber())
				.profileImageUrl(request.getProfileImageUrl())
				.address(request.getAddress())
				.managedRegion(request.getManagedRegion())
				.managedDepartments(request.getManagedDepartments())
				.managedTeams(request.getManagedTeams())
				.designation(request.getDesignation())
				.department(request.getDepartment())
				.reportingDirector(request.getReportingDirector())
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
	public void applyUpdates(ManagerUserDetail user, ManagerProfileRequest request, String actor, Instant now) {
		user.setEmail(normalizeEmail(request.getEmail()));
		user.setUsername(request.getUsername());
		user.setFullName(request.getFullName());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setProfileImageUrl(request.getProfileImageUrl());
		user.setAddress(request.getAddress());
		user.setManagedRegion(request.getManagedRegion());
		user.setManagedDepartments(request.getManagedDepartments());
		user.setManagedTeams(request.getManagedTeams());
		user.setDesignation(request.getDesignation());
		user.setDepartment(request.getDepartment());
		user.setReportingDirector(request.getReportingDirector());
		user.setAdditionalAttributes(request.getAdditionalAttributes());
		user.setActive(resolveActive(request.getActive()));
		user.setUpdatedBy(actor);
		user.setUpdateTimestamp(now);
	}

	@Override
	public ManagerProfileResponse toProfileResponse(ManagerUserDetail user) {
		log.trace(ApplicationConstant.MAPPING_MANAGER_USER_TO_RESPONSE, user.getId());
		ManagerProfileResponse.ManagerProfileResponseBuilder<?, ?> builder = ManagerProfileResponse.builder();
		BaseProfileResponseMapper.applyBaseFields(user, builder);
		return builder
				.managedRegion(user.getManagedRegion())
				.managedDepartments(user.getManagedDepartments())
				.managedTeams(user.getManagedTeams())
				.designation(user.getDesignation())
				.department(user.getDepartment())
				.reportingDirector(user.getReportingDirector())
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
