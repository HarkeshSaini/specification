package com.specification.service.transformer.function;

import com.specification.service.domain.entity.function.HomeListingKey;
import com.specification.service.response.function.HomeListingKeyResponse;

public interface IHomeListingTransformer {

    HomeListingKeyResponse transformResponse(HomeListingKey homeListingKey);
}
