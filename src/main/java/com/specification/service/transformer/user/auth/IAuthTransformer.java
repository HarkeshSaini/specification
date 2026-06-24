package com.specification.service.transformer.user.auth;

import com.specification.service.domain.entity.user.AuthAccountDetail;
import com.specification.service.response.TokenResponse;

public interface IAuthTransformer {

	TokenResponse toTokenResponse(AuthAccountDetail account);
}
