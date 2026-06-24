package com.specification.service.transformer.user.website.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.entity.user.WebUserDetail;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.transformer.support.BaseProfileResponseMapper;
import com.specification.service.transformer.user.website.IWebsiteUserTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Locale;

@Slf4j
@Component
public class WebsiteUserTransformer implements IWebsiteUserTransformer {

	@Override
	public WebUserDetail toNewEntity(WebsiteProfileRequest request, String actor, Instant now) {
		return WebUserDetail.builder()
				.email(normalizeEmail(request.getEmail()))
				.username(request.getUsername())
				.fullName(request.getFullName())
				.phoneNumber(request.getPhoneNumber())
				.profileImageUrl(request.getProfileImageUrl())
				.address(request.getAddress())
				.language(request.getLanguage())
				.timezone(request.getTimezone())
				.theme(request.getTheme())
				.preferences(request.getPreferences())
				.active(resolveActive(request.getActive()))
				.deleted(false)
				.createdTimestamp(now)
				.updateTimestamp(now)
				.createdBy(actor)
				.updatedBy(actor)
				.build();
	}

	@Override
	public void applyUpdates(WebUserDetail user, WebsiteProfileRequest request, String actor, Instant now) {
		user.setEmail(normalizeEmail(request.getEmail()));
		user.setUsername(request.getUsername());
		user.setFullName(request.getFullName());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setProfileImageUrl(request.getProfileImageUrl());
		user.setAddress(request.getAddress());
		user.setLanguage(request.getLanguage());
		user.setTimezone(request.getTimezone());
		user.setTheme(request.getTheme());
		user.setPreferences(request.getPreferences());
		user.setActive(resolveActive(request.getActive()));
		user.setUpdatedBy(actor);
		user.setUpdateTimestamp(now);
	}

	@Override
	public WebsiteProfileResponse toProfileResponse(WebUserDetail user) {
		log.trace(ApplicationConstant.MAPPING_WEBSITE_USER_TO_RESPONSE, user.getId());
		WebsiteProfileResponse.WebsiteProfileResponseBuilder<?, ?> builder = WebsiteProfileResponse.builder();
		BaseProfileResponseMapper.applyBaseFields(user, builder);
		return builder
				.language(user.getLanguage())
				.timezone(user.getTimezone())
				.theme(user.getTheme())
				.preferences(user.getPreferences())
				.build();
	}

	private static Boolean resolveActive(Boolean active) {
		return active == null ? Boolean.TRUE : active;
	}

	private static String normalizeEmail(String email) {
		return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
	}
}
