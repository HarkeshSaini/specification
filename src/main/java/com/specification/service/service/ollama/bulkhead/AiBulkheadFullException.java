package com.specification.service.service.ollama.bulkhead;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.exception.ErrorConstant;
import com.specification.service.response.apires.ServiceException;
import org.springframework.http.HttpStatus;

public final class AiBulkheadFullException extends ServiceException {

    private AiBulkheadFullException(String poolName) {
        super(
                ApplicationConstant.ERROR_CODE_TOO_MANY_REQUESTS,
                "AI " + poolName + " pool is at capacity. Retry shortly.",
                ErrorConstant.CATEGORY.TH,
                ErrorConstant.SEVERITY.I,
                HttpStatus.TOO_MANY_REQUESTS
        );
    }

    public static AiBulkheadFullException forPool(String poolName) {
        return new AiBulkheadFullException(poolName);
    }
}
