package com.specification.service.service.user.auth;

 import com.specification.service.domain.entity.user.AuthAccountDetail;
import com.specification.service.request.LoginRequest;

import reactor.core.publisher.Mono;

public interface IAuthService {

	Mono<AuthAccountDetail> authenticate(LoginRequest request);
}
