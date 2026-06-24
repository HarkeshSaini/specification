package com.specification.service.transformer.function.impl;

import com.specification.service.domain.entity.function.HomeListingKey;
import com.specification.service.response.function.HomeListingKeyResponse;
import com.specification.service.transformer.function.IHomeListingTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class HomeListingTransformerImpl implements IHomeListingTransformer {

    @Override
    public HomeListingKeyResponse transformResponse(HomeListingKey homeListingKey) {
        HomeListingKeyResponse keyResponse=new HomeListingKeyResponse();
        BeanUtils.copyProperties(homeListingKey,keyResponse);
        return keyResponse;
    }
}
