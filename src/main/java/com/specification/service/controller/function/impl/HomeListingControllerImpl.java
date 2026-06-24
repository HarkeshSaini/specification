package com.specification.service.controller.function.impl;

import com.specification.service.controller.function.IHomeListingController;
import com.specification.service.facade.function.IHomeListingFacade;
import com.specification.service.response.function.HomeListingKeyResponse;
import com.specification.service.response.apires.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class HomeListingControllerImpl implements IHomeListingController {

    private final IHomeListingFacade homeListingFacade;

    public HomeListingControllerImpl(IHomeListingFacade homeListingFacade) {
        this.homeListingFacade = homeListingFacade;
    }

    @Override
    public ResponseEntity<APIResponse<HomeListingKeyResponse>> homeListingKeyWord(String langCode) {
        HomeListingKeyResponse homeListingKey = this.homeListingFacade.homeListingKeyWord(langCode);
        return ResponseEntity.ok(APIResponse.<HomeListingKeyResponse>builder()
                        .status("SUCCESS")
                        .httpStatus(HttpStatus.OK.value())
                        .message("Listing keywords have been successfully retrieved.")
                        .data(homeListingKey)
                        .hasMore(true)
                        .code(HttpStatus.OK.getReasonPhrase())
                        .build()
        );
    }
}
