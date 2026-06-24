package com.specification.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

	private String accessToken;
	private String tokenType;
	private long expiresInSeconds;
	private String role;
	private String profileId;
	private String redirectPath;
}
