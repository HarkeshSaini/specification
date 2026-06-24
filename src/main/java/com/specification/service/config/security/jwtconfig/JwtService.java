package com.specification.service.config.security.jwtconfig;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import com.specification.service.domain.entity.user.AuthAccountDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
 import com.specification.service.config.security.CustomUser;
import com.specification.service.config.security.RoleAuthorities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class JwtService {

	private final SecretKey secretKey;

	public JwtService() {
		byte[] keyBytes = ApplicationConstant.JWT_SECRET.getBytes(StandardCharsets.UTF_8);
		if (keyBytes.length < 32) {
			throw new IllegalStateException(ApplicationConstant.JWT_SECRET_MIN_LENGTH_ERROR);
		}
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(AuthAccountDetail account) {
		Instant now = Instant.now();
		Instant exp = now.plusMillis(ApplicationConstant.JWT_EXPIRATION_MS);
		return Jwts.builder().subject(account.getId()).claim(ApplicationConstant.JWT_CLAIM_EMAIL, account.getEmail())
				.claim(ApplicationConstant.JWT_CLAIM_PROFILE_ID, account.getProfileId()).claim(ApplicationConstant.JWT_CLAIM_ROLE, account.getRole().name())
				.issuedAt(Date.from(now)).expiration(Date.from(exp)).signWith(secretKey).compact();
	}

	public Mono<Authentication> parseAuthentication(String token) {
		return Mono.fromCallable(() -> parseTokenInternal(token))
				.doOnError(JwtException.class, e -> log.debug(ApplicationConstant.JWT_VALIDATION_FAILED, e.getMessage()));
	}

	private Authentication parseTokenInternal(String token) {
		Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
		String authAccountId = claims.getSubject();
		String email = claims.get(ApplicationConstant.JWT_CLAIM_EMAIL, String.class);
		String profileId = claims.get(ApplicationConstant.JWT_CLAIM_PROFILE_ID, String.class);
		UserRole role = UserRole.valueOf(claims.get(ApplicationConstant.JWT_CLAIM_ROLE, String.class));
		CustomUser user = new CustomUser(authAccountId, profileId, email, role, RoleAuthorities.forRole(role));
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}
}
