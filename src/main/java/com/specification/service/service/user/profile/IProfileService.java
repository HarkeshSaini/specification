package com.specification.service.service.user.profile;



import com.specification.service.domain.entity.user.AdminUserDetail;
import com.specification.service.domain.entity.user.ManagerUserDetail;
import com.specification.service.domain.entity.user.SuperAdminUserDetail;
import com.specification.service.domain.entity.user.WebUserDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProfileService {

	Mono<AdminUserDetail> findAdminById(String id);

	Mono<ManagerUserDetail> findManagerById(String id);

	Mono<WebUserDetail> findWebsiteUserById(String id);

	Mono<SuperAdminUserDetail> findSuperAdminById(String id);

	Flux<WebUserDetail> findAllWebsiteUsers();
}
