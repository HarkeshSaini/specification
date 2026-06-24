package com.specification.service.service.function.impl;

import com.specification.service.domain.entity.function.HomeListingKey;
import com.specification.service.domain.repository.HomeListingRepository;
import com.specification.service.exception.ErrorConstant;
import com.specification.service.response.apires.ServiceException;
import com.specification.service.service.function.IHomeListingService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class HomeListingServiceImpl implements IHomeListingService {

    private final HomeListingRepository homeListingRepository;

    public HomeListingServiceImpl(HomeListingRepository homeListingRepository) {
        this.homeListingRepository = homeListingRepository;
    }

    @Override
    public HomeListingKey homeListingKeyWord(String langCode) {
        Optional<HomeListingKey> homeListingKey =this.homeListingRepository.findByLanguageCode(langCode);
        if(ObjectUtils.isEmpty(homeListingKey)){
            String message="No listing keywords found.";
            throw new ServiceException("No listing keywords found.", message, ErrorConstant.CATEGORY.BV, ErrorConstant.SEVERITY.I);
        }
        return homeListingKey.orElse(null);
    }
}
