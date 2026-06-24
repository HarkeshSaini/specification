package com.specification.service.facade.function;

import com.specification.service.response.function.HomeListingKeyResponse;

public interface IHomeListingFacade {

    HomeListingKeyResponse homeListingKeyWord(String langCode);
}
