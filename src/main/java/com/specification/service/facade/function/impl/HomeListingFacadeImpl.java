package com.specification.service.facade.function.impl;

import com.specification.service.domain.entity.function.HomeListingKey;
import com.specification.service.facade.function.IHomeListingFacade;
import com.specification.service.response.function.HomeListingKeyResponse;
import com.specification.service.service.function.IHomeListingService;
import com.specification.service.transformer.function.IHomeListingTransformer;
import org.springframework.stereotype.Component;

@Component
public class HomeListingFacadeImpl implements IHomeListingFacade {

    private final IHomeListingService iHomeListingService;
    private final IHomeListingTransformer iHomeListingTransformer;

    public HomeListingFacadeImpl(IHomeListingService iHomeListingService, IHomeListingTransformer iHomeListingTransformer) {
        this.iHomeListingService = iHomeListingService;
        this.iHomeListingTransformer = iHomeListingTransformer;
    }

    @Override
    public HomeListingKeyResponse homeListingKeyWord(String langCode) {
        HomeListingKey homeListingKey= this.iHomeListingService.homeListingKeyWord(langCode);
        return this.iHomeListingTransformer.transformResponse(homeListingKey);
    }
}
